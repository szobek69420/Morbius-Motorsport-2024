package main.java.org.Screens;

import main.java.org.InputManagement.InputManager;
import main.java.org.Main;
import main.java.org.Physics.CollisionDetection;
import main.java.org.Render.Camera.Camera;
import main.java.org.Render.Drawables.Cube;
import main.java.org.Updateable.Player;
import main.java.org.Updateable.Updateable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class GameScreen extends JPanel{
    public static Camera mainCamera;
    public static CollisionDetection physics;

    private static boolean paused=false;
    private static boolean justPaused=false;
    private static boolean justUnpaused=false;

    private static boolean justDied=false;
    private static boolean justUndied=false;

    public ArrayList<Updateable> updateables;

    private Player player=null;

    private PauseMenu pauseMenu=null;
    private DeathScreen deathScreen=null;

    private int screenWidth,screenHeight;

    private double time;

    public GameScreen(int width, int height){
        mainCamera=new Camera(width,height);
        physics=new CollisionDetection();

        updateables=new ArrayList<>();

        paused=false;

        screenWidth=width;
        screenHeight=height;

        this.time=0;

        this.setLayout(new GridLayout(1,1));

        player=new Player();
        this.addUpdateable(player);
    }


    public void frame(double deltaTime){
        if(!paused){
            time+=deltaTime;

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
        else if(justDied){
            justDied=false;
            InputManager.showCursor(MainFrame.currentFrame);

            deathScreen=new DeathScreen(screenWidth,screenHeight);
            this.add(deathScreen);

            MainFrame.currentFrame.setVisible(true);
        }
        else if(justUndied){
            if(deathScreen!=null){
                this.remove(deathScreen);
                deathScreen=null;
                justUndied=false;

                player.respawn();

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

    public static void die(){
        GameScreen.paused=true;
        GameScreen.justDied=true;
    }

    public static void undie(){
        GameScreen.justUndied=true;
    }

    private static class PauseMenu extends JPanel{
        public PauseMenu(int screenWidth, int screenHeight){
            super();

            this.setBackground(new Color(0,0,0,100));
            this.setLayout(null);
            //this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
            //this.setBorder(new EmptyBorder(new Insets(screenWidth/6,0,0,0)));

            int currentY=screenHeight/5;

            //menu name
            JLabel title=new JLabel("Paused",SwingConstants.CENTER);
            title.setFont(new Font("Monocraft", Font.PLAIN, 120));
            title.setBackground(new Color(0,0,0,0));
            title.setForeground(new Color(0,255,255));

            title.setBounds(screenWidth/2-300,currentY,600,200);
            currentY+=300;
            this.add(title);

            //spacing
            //this.add(Box.createRigidArea(new Dimension(0,100)));

            //continue button
            var butt=new JButton();
            butt.setText("Resume");
            butt.setFont(new Font("Monocraft", Font.PLAIN, 50));
            butt.setForeground(Color.white);
            butt.setBackground(new Color(0,0,0,255));


            butt.setBounds(screenWidth/2-200,currentY,400,80);
            currentY+=130;

            butt.addActionListener(e->{
                if(butt.isEnabled()){
                    GameScreen.unpause();
                }
            });
            this.add(butt);

            //spacing
            //this.add(Box.createRigidArea(new Dimension(0,50)));

            //main menu button
            var butt2=new JButton();
            butt2.setText("Main menu");
            butt2.setFont(new Font("Monocraft", Font.PLAIN, 50));
            butt2.setForeground(Color.white);
            butt2.setBackground(new Color(0,0,0,255));


            butt2.setBounds(screenWidth/2-200,currentY,400,80);

            butt2.addActionListener(e->{
                if(butt2.isEnabled()){
                    ((MainFrame)MainFrame.currentFrame).setCurrentStage(MainFrame.GAME_STAGES.TITLE_SCREEN);
                }
            });
            this.add(butt2);

            this.grabFocus();
        }
    }

    private static class DeathScreen extends JPanel{
        public DeathScreen(int screenWidth, int screenHeight){
            super();

            this.setBackground(new Color(0,0,0,100));
            this.setLayout(null);
            //this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
            //this.setBorder(new EmptyBorder(new Insets(screenWidth/6,0,0,0)));

            int currentY=screenHeight/5;

            //menu name
            JLabel title=new JLabel("You are dieded",SwingConstants.CENTER);
            title.setFont(new Font("Monocraft", Font.PLAIN, 100));
            title.setBackground(new Color(0,0,0,0));
            title.setForeground(new Color(255,0,0));

            title.setBounds(screenWidth/2-500,currentY,1000,200);
            currentY+=300;
            this.add(title);

            //spacing
            //this.add(Box.createRigidArea(new Dimension(0,100)));

            //continue button
            var butt=new JButton();
            butt.setText("Restart");
            butt.setFont(new Font("Monocraft", Font.PLAIN, 50));
            butt.setForeground(Color.white);
            butt.setBackground(new Color(0,0,0,255));


            butt.setBounds(screenWidth/2-200,currentY,400,80);
            currentY+=130;

            butt.addActionListener(e->{
                if(butt.isEnabled()){
                    GameScreen.undie();
                }
            });
            this.add(butt);

            //spacing
            //this.add(Box.createRigidArea(new Dimension(0,50)));

            //main menu button
            var butt2=new JButton();
            butt2.setText("Main menu");
            butt2.setFont(new Font("Monocraft", Font.PLAIN, 50));
            butt2.setForeground(Color.white);
            butt2.setBackground(new Color(0,0,0,255));


            butt2.setBounds(screenWidth/2-200,currentY,400,80);

            butt2.addActionListener(e->{
                if(butt2.isEnabled()){
                    ((MainFrame)MainFrame.currentFrame).setCurrentStage(MainFrame.GAME_STAGES.TITLE_SCREEN);
                }
            });
            this.add(butt2);

            this.grabFocus();
        }
    }
}
