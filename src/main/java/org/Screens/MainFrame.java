package main.java.org.Screens;

import main.java.org.InputManagement.InputManager;
import main.java.org.LinearAlgebruh.Vector3;
import main.java.org.Main;
import main.java.org.Render.Drawables.Cube;
import main.java.org.Updateable.Player;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static JFrame currentFrame=null;

    private GameScreen gameScreen;
    public MainFrame(String name){
        super(name);
        currentFrame=this;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int)screenSize.getWidth(),(int)screenSize.getHeight());
        //hehe.setBackground(new Color(83,255,246));
        this.setBackground(new Color(0,0,0));
        //hehe.setIconImage(new ImageIcon(hehe.getClass().getResource("/assets/sprites/logo_1.png")).getImage());

        this.addKeyListener(new InputManager.KeyInput());

        gameScreen=new GameScreen((int)screenSize.getWidth(),(int)screenSize.getHeight());

        GameScreen.mainCamera.addDrawable(new Cube(Color.red));
        var kubatemp=new Cube(Color.green);
        kubatemp.setPosition(new Vector3(-5,2,7));
        kubatemp.setScale(new Vector3(1.0f,0.5f,2.0f));
        kubatemp.setName("amogus2");
        GameScreen.mainCamera.addDrawable(kubatemp);

        var kubatemp2=new Cube(Color.magenta);
        kubatemp2.setPosition(new Vector3(0,-10,0));
        kubatemp2.setScale(new Vector3(6.0f,6f,6.0f));
        kubatemp2.setName("amogus3");
        GameScreen.mainCamera.addDrawable(kubatemp2);
        Vector3[] pens=new Vector3[]{new Vector3(50,20,0),new Vector3(1,20,1),new Vector3(50,-1,-2),new Vector3(1,1,1),new Vector3(50,-1,2),new Vector3(1,1,1)};
        for(int i=0;i<pens.length/2;i++){
            var kubatemp3=new Cube(Color.yellow);
            kubatemp3.setPosition(pens[i*2]);
            kubatemp3.setScale(pens[i*2+1]);
            GameScreen.mainCamera.addDrawable(kubatemp3);
        }
        gameScreen.addUpdateable(new Player());


        this.add(gameScreen);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void start(){
        //game loop
        long lastTime = System.nanoTime();
        double deltaTime;
        while(true) {
            long now = System.nanoTime();
            deltaTime = (now -lastTime)*0.000000001;

            if(deltaTime>0.01666){
                //System.out.println(deltaTime);
                lastTime = now;

                gameScreen.frame(deltaTime);
            }
        }
    }
}
