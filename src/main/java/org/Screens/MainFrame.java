package main.java.org.Screens;

import main.java.org.InputManagement.InputManager;
import main.java.org.LinearAlgebruh.Vector3;
import main.java.org.Main;
import main.java.org.Render.Drawables.Cube;
import main.java.org.Updateable.Player;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static enum GAME_STAGES{
        TITLE_SCREEN,
        LEVEL_SELECTOR,
        GAME,
        END_SCREEN,
        QUIT
    }

    private GAME_STAGES currentStage;
    public static JFrame currentFrame=null;

    public MainFrame(String name){
        super(name);
        currentFrame=this;
        currentStage=GAME_STAGES.TITLE_SCREEN;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int)screenSize.getWidth(),(int)screenSize.getHeight());
        //this.setBackground(new Color(83,255,246));
        this.setBackground(new Color(0,0,0));
        //hehe.setIconImage(new ImageIcon(hehe.getClass().getResource("/assets/sprites/logo_1.png")).getImage());

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void run(){
        while(true){
            switch (currentStage){
                case TITLE_SCREEN -> titleScreen();
                case LEVEL_SELECTOR -> levelSelectionScreen();
                case GAME -> game();
            }
        }
    }

    public void setCurrentStage(GAME_STAGES currentStage){
        this.currentStage=currentStage;
    }

    private void titleScreen()  {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        TitleScreen titleScreen=new TitleScreen((int)screenSize.getWidth(),(int)screenSize.getHeight());
        this.add(titleScreen);
        this.setVisible(true);
        this.repaint();

        while(currentStage==GAME_STAGES.TITLE_SCREEN){
            try{
                Thread.sleep(50);
            }
            catch (InterruptedException ie){

            }
        }

        this.remove(titleScreen);
    }

    private void levelSelectionScreen()  {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        LevelSelectionScreen levelScreen=new LevelSelectionScreen((int)screenSize.getWidth(),(int)screenSize.getHeight());
        this.add(levelScreen);
        this.setVisible(true);
        this.repaint();

        while(currentStage==GAME_STAGES.LEVEL_SELECTOR){
            try{
                Thread.sleep(50);
            }
            catch (InterruptedException ie){

            }
        }

        this.remove(levelScreen);
    }

    private void game(){


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        GameScreen gameScreen=new GameScreen((int)screenSize.getWidth(),(int)screenSize.getHeight());
        this.add(gameScreen);

        fillGameScreen(gameScreen);

        this.addKeyListener(new InputManager.KeyInput());
        InputManager.hideCursor(this);

        this.requestFocus();

        this.setVisible(true);

        //game loop
        long lastTime = System.nanoTime();
        double deltaTime;
        while(currentStage==GAME_STAGES.GAME) {
            long now = System.nanoTime();
            deltaTime = (now -lastTime)*0.000000001;

            if(deltaTime>0.01666){
                //System.out.println(deltaTime);
                lastTime = now;

                gameScreen.frame(deltaTime);
            }
        }

        this.remove(gameScreen);
        gameScreen=null;

        for(var kl : this.getKeyListeners())
            this.removeKeyListener(kl);

        InputManager.showCursor(this);
    }

    public void fillGameScreen(GameScreen gameScreen){//temporary

        GameScreen.mainCamera.addDrawable(new Cube(Color.red));
        //var kubatemp=new Cube(Color.green);
        var kubatemp=new Cube(Color.red);
        kubatemp.setPosition(new Vector3(-5,2,7));
        kubatemp.setScale(new Vector3(1.0f,0.5f,2.0f));
        kubatemp.setName("amogus2");
        GameScreen.mainCamera.addDrawable(kubatemp);

        //var kubatemp2=new Cube(Color.magenta);
        var kubatemp2=new Cube(Color.red);
        kubatemp2.setPosition(new Vector3(5,-10,0));
        kubatemp2.setScale(new Vector3(6.0f,6f,6.0f));
        kubatemp2.setName("amogus3");
        GameScreen.mainCamera.addDrawable(kubatemp2);



        gameScreen.addUpdateable(new Player());
    }
}
