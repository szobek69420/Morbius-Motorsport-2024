package main.java.org.Screens;

import main.java.org.AudioManagement.AudioManager;
import main.java.org.InputManagement.InputManager;
import main.java.org.LinearAlgebruh.Vector3;
import main.java.org.Main;
import main.java.org.Obstacles.CubeMoving;
import main.java.org.Obstacles.CubeStatic;
import main.java.org.Obstacles.CubeWeak;
import main.java.org.Obstacles.Obstacle;
import main.java.org.Physics.AABB;
import main.java.org.Render.Drawables.Cube;
import main.java.org.Resizable.Resizable;
import main.java.org.Updateable.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Eine Kindklasse von JFrame, es verwaltet den Bildschirm, in dem das Spiel passieren wird
 */
public class MainFrame extends JFrame implements Resizable {

    /**
     * Ein Enum für die verschiedenen Stufen des Spieles:
     * -TITLE_SCREEN: der Startbildschirm, es zeigt den Titel und ein wunderschönes Hintergrundbild
     * -LEVEL_SELECTOR: der Stufenauswahlbildschirm, es zeigt Tasten, mit denen die Stufen geladen werden können
     */
    public static enum GAME_STAGES{
        TITLE_SCREEN,
        LEVEL_SELECTOR,
        SETTINGS,
        GAME,
    }

    /**
     * Ein Enum für die verschiedenen Stufen. Es ist entstanden, um die Kode lesbarer zu machen
     */
    public static enum LEVELS{
        LEVEL_1,
        LEVEL_2,
        LEVEL_3,
        LEVEL_4,
        LEVEL_5
    }

    /**
     * LEVELS->int Konverter
     * @param level das zu konvertierende Stufenenum
     * @return der dem level entsprechenden Integerwert
     */
    public static int getLevelNumber(LEVELS level){
        switch (level){
            case LEVEL_1 -> {
                return 0;
            }
            case LEVEL_2 -> {
                return 1;
            }
            case LEVEL_3 -> {
                return 2;
            }
            case LEVEL_4 -> {
                return 3;
            }
            case LEVEL_5 -> {
                return 4;
            }
        }

        return -1;
    }

    /**
     * Das aktuell gezeigtes Bildschirm
     */
    private GAME_STAGES currentStage;
    private double highscore;

    /**
     * Aktive MainFrame-Instanz
     */
    public static JFrame currentFrame=null;

    /**
     * Aktiver Bildschirm
     */
    private JPanel currentScreen=null;

    /**
     * Die aktuell gewählte Stufe
     */
    private LEVELS levelSelected;

    /**
     * Erzeugt eine neue MainFrame-Instanz.
     * Es stellt currentFrame zu der erzeugten Instanz.
     * @param name Der Name des erzeugten Fensters
     */
    public MainFrame(String name){
        super(name);
        currentFrame=this;
        currentStage=GAME_STAGES.TITLE_SCREEN;
        levelSelected=LEVELS.LEVEL_1;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int)screenSize.getWidth(),(int)screenSize.getHeight());
        //this.setBackground(new Color(83,255,246));
        this.setBackground(new Color(0,0,0));
        //hehe.setIconImage(new ImageIcon(hehe.getClass().getResource("/assets/sprites/logo_1.png")).getImage());

        this.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if(currentStage==GAME_STAGES.GAME&&!GameScreen.isPaused())
                    GameScreen.pause();
            }
            @Override
            public void focusGained(FocusEvent e) {}
        });

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                AudioManager.closeAll();
            }
        });
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                onResize(componentEvent.getComponent().getWidth(),componentEvent.getComponent().getHeight());
            }
        });
        AudioManager.playSound(AudioManager.SOUNDS.MUSIC);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Die Spielschleife
     */
    public void start(){
        while(true){
            switch (currentStage){
                case TITLE_SCREEN -> titleScreen();
                case LEVEL_SELECTOR -> levelSelectionScreen();
                case SETTINGS-> settingsScreen();
                case GAME -> game();
            }
        }
    }

    /**
     * Überschreibt die resize-Funktion des Resizable-Interfaces
     * @param width die neue Breite des Fensters
     * @param height die neue Höhe des Fensters
     */
    public void onResize(int width, int height){
        if(currentScreen==null)
            return;

        switch (currentStage){
            case TITLE_SCREEN -> ((TitleScreen)currentScreen).onResize(width, height);
            case LEVEL_SELECTOR -> ((LevelSelectionScreen)currentScreen).onResize(width, height);
            case SETTINGS-> ((SettingsScreen)currentScreen).onResize(width, height);
            case GAME -> ((GameScreen)currentScreen).onResize(width, height);
        }
    }

    /**
     * Stellt die aktuelle Spielstufe ein
     * @param currentStage GAME_STAGES Variable für die aktuelle Spielstufe
     */
    public void setCurrentStage(GAME_STAGES currentStage){
        this.currentStage=currentStage;
    }

    /**
     * Stellt die aktuelle Stufe ein
     * @param levelSelected LEVELS Variable für die aktuelle Stufe
     */
    public void setCurrentLevel(LEVELS levelSelected){
        this.levelSelected=levelSelected;
    }

    /**
     * Gibt die aktuelle Stufe zurück
     * @return die aktuelle Stufe
     */
    public LEVELS getCurrentLevel(){return this.levelSelected;}

    /**
     * Stellt die Bestzeit des Spielers in der gewählten Stufe ein
     * @param highscore die Bestzeit des Spielers in der gewählten Stufe
     */
    public void setHighscore(double highscore){
        this.highscore=highscore;
    }

    /**
     * Erzeugt eine TitleScreen-Instanz und wartet für Benutzereingabe.
     * Das Spiel kann von hier zum Stufenauswahlbildschirm gehen oder geschlossen werden.
     */
    private void titleScreen()  {
        TitleScreen titleScreen=new TitleScreen(this.getWidth(),this.getHeight());
        this.add(titleScreen);
        this.setVisible(true);
        this.repaint();

        currentScreen=titleScreen;

        while(currentStage==GAME_STAGES.TITLE_SCREEN){
            try{
                Thread.sleep(50);
            }
            catch (InterruptedException ie){

            }
        }

        currentScreen=null;
        this.remove(titleScreen);
    }

    /**
     * Erzeugt eine LevelSelectionScreen-Instanz und wartet für Benutzereingabe.
     * Das Spiel kann von hier zum Titelbildschirm und Spielbildschirm (GameScreen) gehen oder geschlossen werden.
     */
    private void levelSelectionScreen()  {
        LevelSelectionScreen levelScreen=new LevelSelectionScreen(this.getWidth(),this.getHeight());
        this.add(levelScreen);
        this.setVisible(true);
        this.repaint();

        currentScreen=levelScreen;

        while(currentStage==GAME_STAGES.LEVEL_SELECTOR){
            try{
                Thread.sleep(50);
            }
            catch (InterruptedException ie){

            }
        }

        currentScreen=null;
        this.remove(levelScreen);
    }

    /**
     * Erzeugt eine SettingsScreen-Instanz und wartet für Benutzereingabe.
     * Das Spiel kann von hier zum Titelbildschirm und Spielbildschirm (GameScreen) gehen oder geschlossen werden.
     */
    private void settingsScreen()  {
        SettingsScreen settingsScreen=new SettingsScreen(this.getWidth(),this.getHeight());
        this.add(settingsScreen);
        this.setVisible(true);
        this.repaint();

        currentScreen=settingsScreen;

        while(currentStage==GAME_STAGES.SETTINGS){
            try{
                Thread.sleep(50);
            }
            catch (InterruptedException ie){

            }
        }

        currentScreen=null;
        this.remove(settingsScreen);
    }

    /**
     * Erzeugt eine GameScreen-Instanz.
     * Beinhaltet eine andere Spielschleife, die zyklisch die frame-Funktion der GameScreen-Instanz aufruft. Ein Laufen dieser Schleife ist "Frame" genannt.
     * Das Spiel kann am höchstens 60 FPS laufen
     */
    private void game(){

        GameScreen gameScreen=new GameScreen(this.getWidth(),this.getHeight(), highscore);
        this.add(gameScreen);

        currentScreen=gameScreen;

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

        currentScreen=null;
        this.remove(gameScreen);
        gameScreen=null;

        for(var kl : this.getKeyListeners())
            this.removeKeyListener(kl);

        InputManager.showCursor(this);
    }

    /**
     * Am Spielanfang (GameScreen-Erzeugung) wird die zur Stufe gehörige Datei eingelesen, und danach das Spielfeld gebaut.
     * @param gameScreen Die aktive GameScreen-Instanz
     */
    public void fillGameScreen(GameScreen gameScreen){//temporary

        File levelFile=new File(Main.dataDirectory);
        switch (levelSelected){
            case LEVEL_1:
                levelFile=new File(levelFile,"190467C1EA52B2B69EA0F7700C3507C9199F4A50ADDFB522B7DA2.bingchilling");
                break;

            case LEVEL_2:
                levelFile=new File(levelFile,"CCE3C0C58CD08DE49BB31A6172D14757EB889.bingchilling");
                break;

            case LEVEL_3:
                levelFile=new File(levelFile,"D32D91E8B858B53DB758AA105D1C852A3144B74A5.bingchilling");
                break;

            case LEVEL_4:
                levelFile=new File(levelFile,"FBD2F960EE7823D5EA8CF11B4094D4B40B81C2F2708A0C28D6C0EC8FEE.bingchilling");
                break;

            case LEVEL_5:
                levelFile=new File(levelFile,"2D3B1F0D647C4D15D0794BE1CF6A53012A4DC03AA6BF33F3F9C1B40C3.bingchilling");
                break;
        }

        if(!levelFile.exists()){
            System.err.println("Bro the level files are missing");
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

        int lowestPoint=0;
        int blockCount=0;
        Vector3[] blockPosition=null;
        Vector3[] blockScale=null;
        Color[] blockColour=null;
        int[] blockType=null;
        int[][] blockAdditionalInfo=null;

        try(Scanner sc=new Scanner(levelFile)){
            gameScreen.setDevsBest(0.001*sc.nextInt());//highscore

            lowestPoint=sc.nextInt();

            blockCount=sc.nextInt();

            blockPosition=new Vector3[blockCount];
            blockScale=new Vector3[blockCount];
            blockColour=new Color[blockCount];
            blockType=new int[blockCount];
            blockAdditionalInfo=new int[blockCount][4];


            for(int i=0;i<blockCount;i++){

                int[] temp=new int[9];

                for(int j=0;j<9;j++)
                    temp[j]=sc.nextInt();

                blockPosition[i]=new Vector3(temp[0]*0.01f,temp[1]*0.01f,temp[2]*0.01f);
                blockScale[i]=new Vector3(temp[3]*0.01f,temp[4]*0.01f,temp[5]*0.01f);
                blockColour[i]=new Color(temp[6],temp[7],temp[8]);

                blockType[i]=sc.nextInt();

                if(blockType[i]==3){
                    for(int j=0;j<4;j++)
                        blockAdditionalInfo[i][j]=sc.nextInt();
                }
            }
        }
        catch (IOException ex){
            System.err.println("bro the fucking level file is kaputt");
        }

        //creating level
        GameScreen.physics.addAABB(new AABB(new Vector3(0,0.01f*lowestPoint,0),new Vector3(1000,2,1000),true,"Sus"));//bottom of the playfield

        for(int i=0;i<blockCount;i++){
            String name=null;
            Obstacle obstacle=null;

            switch (blockType[i]){
                case 0,1:
                    name=i==blockCount-1?"Finish":blockType[i]==1?"Sus":"amogus";
                    obstacle=new CubeStatic(name,blockPosition[i],blockScale[i],blockColour[i]);
                    obstacle.addToCamera(GameScreen.mainCamera);
                    obstacle.addToPhysics(GameScreen.physics);
                    break;

                case 2:
                    obstacle=new CubeWeak("a mogus",blockPosition[i],blockScale[i],blockColour[i]);
                    obstacle.addToCamera(GameScreen.mainCamera);
                    obstacle.addToPhysics(GameScreen.physics);
                    break;

                case 3:
                    Vector3 amplitude=new Vector3(
                            0.01f*blockAdditionalInfo[i][0],
                            0.01f*blockAdditionalInfo[i][1],
                            0.01f*blockAdditionalInfo[i][2]);
                    float speed=0.01f*blockAdditionalInfo[i][3];
                    obstacle=new CubeMoving("am ogus",blockPosition[i],blockScale[i],blockColour[i], amplitude, speed);
                    obstacle.addToCamera(GameScreen.mainCamera);
                    obstacle.addToPhysics(GameScreen.physics);
                    break;

            }

            if(obstacle!=null) {
                GameScreen.um.addUpdateable(obstacle);
                gameScreen.addObstacle(obstacle);
            }
        }
    }
}
