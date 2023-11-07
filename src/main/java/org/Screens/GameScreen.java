package main.java.org.Screens;

import main.java.org.InputManagement.InputManager;
import main.java.org.Main;
import main.java.org.Physics.CollisionDetection;
import main.java.org.Render.Camera.Camera;
import main.java.org.Render.Drawables.Cube;
import main.java.org.Updateable.Updateable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class GameScreen extends JPanel{
    public static Camera mainCamera;
    public static CollisionDetection physics;

    private static boolean paused=false;
    private static boolean justPaused=false;
    private static boolean justUnpaused=false;

    public ArrayList<Updateable> updateables;

    private PauseMenu pauseMenu=null;

    private int screenWidth,screenHeight;


    public GameScreen(int width, int height){
        mainCamera=new Camera(width,height);
        physics=new CollisionDetection();

        updateables=new ArrayList<>();

        paused=false;

        screenWidth=width;
        screenHeight=height;

        this.setLayout(new GridLayout(1,1));
    }


    public void frame(double deltaTime){
        if(!paused){
            InputManager.fetchMousePosition();

            repaint();

            physics.CalculatePhysics(deltaTime);

            for(Updateable u : updateables)
                u.Update(deltaTime);
        }
        else if(justPaused){
            justPaused=false;
            InputManager.showCursor(MainFrame.currentFrame);


            pauseMenu=new PauseMenu(screenWidth,screenHeight);
            this.add(pauseMenu);

            MainFrame.currentFrame.setVisible(true);
        }
        else if(justUnpaused){
            if(pauseMenu!=null){
                this.remove(pauseMenu);
                pauseMenu=null;
                justUnpaused=false;

                MainFrame.currentFrame.requestFocus();
                InputManager.hideCursor(MainFrame.currentFrame);
            }
            paused=false;
        }
    }

    @Override
    public void paint(Graphics g){
        Image image = createImage(getWidth(),getHeight());
        Graphics graphics = image.getGraphics();

        mainCamera.render(graphics);

        g.drawImage(image,0,0,this);
    }

    public void addUpdateable(Updateable u){
        updateables.add(u);
    }

    //static
    public static boolean isPaused(){
        return paused;
    }

    public static void pause(){
        GameScreen.paused=true;
        GameScreen.justPaused=true;
    }

    public static void unpause(){
        GameScreen.justUnpaused=true;
    }

    private static class PauseMenu extends JPanel{
        public PauseMenu(int screenWidth, int screenHeight){
            super();

            //this.setPreferredSize(new Dimension(screenWidth,screenHeight));
            this.setBackground(new Color(0,0,0,100));
            this.setLayout(new BorderLayout());

            var butt=new JButton("amogus");
            butt.setFont(new Font("Arial", Font.PLAIN, 500));

            butt.addActionListener(e->{
                GameScreen.unpause();
            });
            this.add(butt,BorderLayout.CENTER);
            this.grabFocus();
        }
    }
}
