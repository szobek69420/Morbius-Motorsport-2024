package main.java.org.Screens;

import main.java.org.AudioManagement.AudioManager;
import main.java.org.InputManagement.InputManager;
import main.java.org.Main;
import main.java.org.Obstacles.Obstacle;
import main.java.org.Physics.CollisionDetection;
import main.java.org.Render.Camera.Camera;
import main.java.org.Render.Drawables.Cube;
import main.java.org.Resizable.Resizable;
import main.java.org.Updateable.Player;
import main.java.org.Updateable.Updateable;
import main.java.org.Updateable.UpdateableManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Die Kindklasse von JPanel.
 * Beinhaltet den Spielinhalt.
 */
public class GameScreen extends JPanel implements Resizable {
    /**
     * Der aktive Renderer
     */
    public static Camera mainCamera;

    /**
     * Das aktive Physiksystem
     */
    public static CollisionDetection physics;

    /**
     * Der aktive Updateable-Verwalter
     */
    public static UpdateableManager um;

    /**
     * @hidden
     */
    private static boolean paused=false;
    /**
     * @hidden
     */
    private static boolean justPaused=false;
    /**
     * @hidden
     */
    private static boolean justUnpaused=false;
    /**
     * @hidden
     */
    private static boolean focusLost=false;

    /**
     * @hidden
     */
    private static boolean justDied=false;
    /**
     * @hidden
     */
    private static boolean justUndied=false;

    /**
     * @hidden
     */
    private static boolean justFinished=false;
    /**
     * @hidden
     */
    private static boolean justUnfinished=false;

    /**
     * Die aktive Player-Instanz
     */
    private Player player=null;

    /**
     * Die Hindernisse des geladenen Spielfeldes
     */
    private ArrayList<Obstacle> obstacles;

    /**
     * @hidden
     */
    private PauseMenu pauseMenu=null;
    /**
     * @hidden
     */
    private DeathScreen deathScreen=null;
    /**
     * @hidden
     */
    private FinishScreen finishScreen=null;

    /**
     * @hidden
     */
    private Font timerFont=null;

    /**
     * @hidden
     */
    private int screenWidth,screenHeight;

    /**
     * @hidden
     */
    private double time;
    /**
     * @hidden
     */
    private double highscore;
    /**
     * @hidden
     */
    private String highscoreString;

    /**
     * @hidden
     */
    private String devsBestString=null;


    /**
     * Erzeugt eine neue GameScreen-Instanz.
     * Gibt neuen Werte für den folgenden statischen Feldern:
     * -mainCamera
     * -physics
     * -um
     * @param width Die Breite des Fensters
     * @param height Die Höhe des Fensters
     * @param highscore Die Bestzeit in der ausgewählten Stufe
     */
    public GameScreen(int width, int height, double highscore){
        mainCamera=new Camera(width,height);
        physics=new CollisionDetection();

        um=new UpdateableManager();

        paused=false;

        screenWidth=width;
        screenHeight=height;

        this.time=0;
        this.highscore=highscore;
        this.highscoreString=timeString(highscore);

        player=new Player();
        this.addUpdateable(player);
        player.addToPhysics(GameScreen.physics);
        player.addToCamera(GameScreen.mainCamera);
        player.respawn();

        obstacles=new ArrayList<>();

        timerFont=new Font("Monocraft",Font.PLAIN,50);


        this.setLayout(new GridLayout(1,1));
        MainFrame.currentFrame.setVisible(true);
    }

    /**
     * Ein Frame der inneren Spielschleife.
     * Aufruft die Render-, Physik- und Updateable-Systeme.
     * Falls das Bild ist gehalten oder beendet, werden andere Bildschirme als Inhalt angezeigt.
     * @param deltaTime die Zeit, die seit dem letzten Frame verging.
     */
    public void frame(double deltaTime){
        if(!paused){
            time+=deltaTime;

            InputManager.fetchMousePosition();

            repaint();

            physics.CalculatePhysics(deltaTime);

            um.update(deltaTime);
        }
        else if(justPaused){
            repaint();//azert kell, hogy az ido ne legyen kiirva a hatterben ( a paint fuggvenyen belul csak akkor jeleniti meg, ha nem paused)
            justPaused=false;
            InputManager.showCursor(MainFrame.currentFrame);

            pauseMenu=new PauseMenu(screenWidth,screenHeight);
            this.add(pauseMenu);

            focusLost=false;//kell, hogy a PauseMenu ne kérjen egyből focust, ha a játékos elkattintott a képernyőről
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
            repaint();
            justDied=false;
            InputManager.showCursor(MainFrame.currentFrame);

            deathScreen=new DeathScreen(screenWidth,screenHeight);
            this.add(deathScreen);

            MainFrame.currentFrame.setVisible(true);

            AudioManager.playSound(AudioManager.SOUNDS.DEATH);
        }
        else if(justUndied){
            if(deathScreen!=null){
                this.remove(deathScreen);
                deathScreen=null;

                justUndied=false;

                this.time=0;

                for(Obstacle obbi:obstacles)
                    obbi.reset();

                player.respawn();

                MainFrame.currentFrame.requestFocus();
                InputManager.hideCursor(MainFrame.currentFrame);
            }
            paused=false;
        }
        else if(justFinished){
            repaint();
            justFinished=false;
            InputManager.showCursor(MainFrame.currentFrame);

            if(highscore<0||time<highscore) {
                highscore=time;
                highscoreString=timeString(highscore);
                LevelSelectionScreen.saveHighscore(((MainFrame)MainFrame.currentFrame).getCurrentLevel(),time);
                finishScreen = new FinishScreen(screenWidth, screenHeight, timeString(time),highscoreString);
            }
            else
                finishScreen=new FinishScreen(screenWidth,screenHeight, timeString(time),highscoreString);

            this.add(finishScreen);

            MainFrame.currentFrame.setVisible(true);
        }
        else if(justUnfinished){
            if(finishScreen!=null){
                this.remove(finishScreen);
                finishScreen=null;

                justUnfinished=false;

                this.time=0;

                for(Obstacle obbi:obstacles)
                    obbi.reset();

                player.respawn();

                MainFrame.currentFrame.requestFocus();
                InputManager.hideCursor(MainFrame.currentFrame);
            }
            paused=false;
        }
    }

    /**
     * Überschreibt die paint-Funktion von JComponent, um den Inhalt besser angepasst werden zu können.
     * @param g  the <code>Graphics</code> context in which to paint
     */
    @Override
    public void paint(Graphics g){
        super.paint(g);

        BufferedImage image = new BufferedImage(screenWidth,screenHeight,BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();

        mainCamera.render(graphics);
        graphics.setFont(timerFont);

        if(!paused){
            //time
            graphics.setColor(new Color(0,255,255));
            graphics.drawString("time: "+timeString(time),screenWidth-500,50);
            graphics.drawString("best time: "+highscoreString,30,50);
            graphics.setColor(Color.white);
            graphics.drawString("dev's pb: "+devsBestString,30,100);

            //cursor
            for(int i=-2;i<2;i++){
                for(int j=-2;j<2;j++){
                    int color=image.getRGB(screenWidth/2+i,screenHeight/2+j);//argb
                    int inverse = color ^ 0xffffffff;

                    image.setRGB(screenWidth/2+i,screenHeight/2+j,inverse);
                }
            }
            //graphics.fillRect(screenWidth/2-2,screenHeight/2-2,4,4);
        }

        g.drawImage(image,0,0,this);
    }

    /**
     * Überschreibt die resize-Funktion des Resizable-Interfaces
     * @param width die neue Breite des Fensters
     * @param height die neue Höhe des Fensters
     */
    public void onResize(int width, int height){
        screenWidth=width;
        screenHeight=height;

        mainCamera.setScreenSize(width,height);

        if(pauseMenu!=null){
            unpause();
            frame(0.0);
            pause();
            frame(0.0);
        }
        if(deathScreen!=null){
            this.remove(deathScreen);

            deathScreen=new DeathScreen(screenWidth,screenHeight);
            this.add(deathScreen);

            MainFrame.currentFrame.setVisible(true);
            MainFrame.currentFrame.requestFocus();
        }
        if(finishScreen!=null){
            this.remove(finishScreen);

            finishScreen=new FinishScreen(screenWidth,screenHeight,timeString(time),highscoreString);
            this.add(finishScreen);

            MainFrame.currentFrame.setVisible(true);
            MainFrame.currentFrame.requestFocus();
        }
    }

    /**
     * Stellt die PAR-Zeit ein
     * @param devsBestInSeconds PAR-Zeit in Sekunden
     */
    public void setDevsBest(double devsBestInSeconds){
        devsBestString=timeString(devsBestInSeconds);
    }

    /**
     * Registriert ein Updateable-Objekt
     * @param u zu registrierendes Updateable-Objekt
     */
    public void addUpdateable(Updateable u){
        um.addUpdateable(u);
    }

    /**
     * Registriert ein Hindernis
     * @param obbi zu registrierendes Hindernis
     */
    public void addObstacle(Obstacle obbi){
        obstacles.add(obbi);
    }

    //static

    /**
     * Konvertiert eine, in Sekunden gegebene Zeitintervall zum String mit der Form mm:ss.hh
     * @param time die zu umformende Zeit
     * @return das erzeugte String
     */
    public static String timeString(double time){
        if(time<0)
            return "unknown";

        int hundreths=(int)(100*(time+0.001-(int)time));
        int seconds=((int)time)%60;
        int minutes=((int)time/60)%60;

        char[] chars=new char[8];
        chars[0]=(char)(minutes/10+'0');
        chars[1]=(char)(minutes%10+'0');
        chars[2]=':';
        chars[3]=(char)(seconds/10+'0');
        chars[4]=(char)(seconds%10+'0');
        chars[5]=':';
        chars[6]=(char)(hundreths/10+'0');
        chars[7]=(char)(hundreths%10+'0');

        return String.copyValueOf(chars);
    }

    /**
     * Ist das Spiel gehalten?
     * @return TRUE falls das Spiel gehalten ist, FALSE andererweise
     */
    public static boolean isPaused(){
        return paused;
    }

    /**
     * Pausiert das Spiel (die Wirkung dieser Funktion wird in dem nächsten Frame gesehen)
     */
    public static void pause(){
        GameScreen.paused=true;
        GameScreen.justPaused=true;
    }

    /**
     * Fortfahren mit dem Spiel (die Wirkung dieser Funktion wird in dem nächsten Frame gesehen)
     */
    public static void unpause(){
        GameScreen.justUnpaused=true;
    }

    /**
     * Sagt dem GameScreen, dass der Spieler gestorben ist (die Wirkung dieser Funktion wird in dem nächsten Frame gesehen)
     */
    public static void die(){
        GameScreen.paused=true;
        GameScreen.justDied=true;
    }

    /**
     * Startet ein neues Spiel (die Wirkung dieser Funktion wird in dem nächsten Frame gesehen)
     */
    public static void undie(){
        GameScreen.justUndied=true;
    }

    /**
     * Sagt dem GameScreen, dass der Spieler das Ziel erreicht hat (die Wirkung dieser Funktion wird in dem nächsten Frame gesehen)
     */
    public static void finish(){
        GameScreen.paused=true;
        GameScreen.justFinished=true;
    }

    /**
     * Startet ein neues Spiel (die Wirkung dieser Funktion wird in dem nächsten Frame gesehen)
     */
    public static void unfinish(){
        GameScreen.justUnfinished=true;
    }

    /**
     * Der PauseMenu-Inhalt. Er wird dann gezeigt, falls das Spiel gehalten ist.
     * Er vererbt von JPanel.
     */
    private static class PauseMenu extends JPanel implements Resizable{

        /**@hidden*/
        private JLabel title;
        /**@hidden*/
        private JButton resumeButton;
        /**@hidden*/
        private JButton quitButton;

        /**
         * Erzeugt eine neue PauseMenu-Instanz und falls das MainFrame Fokus hat, wird er auch nach Fokus fragen.
         * @param screenWidth Die Breite des Fensters.
         * @param screenHeight Die Höhe des Fensters.
         */
        public PauseMenu(int screenWidth, int screenHeight){
            super();

            this.setBackground(new Color(0,0,0,100));
            this.setLayout(null);
            //this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
            //this.setBorder(new EmptyBorder(new Insets(screenWidth/6,0,0,0)));


            //menu name
            title=new JLabel("Paused",SwingConstants.CENTER);
            title.setFont(new Font("Monocraft", Font.PLAIN, 120));
            title.setBackground(new Color(0,0,0,0));
            title.setForeground(Color.white);

            this.add(title);

            //spacing
            //this.add(Box.createRigidArea(new Dimension(0,100)));

            //continue button
            resumeButton=new JButton();
            resumeButton.setText("Resume");
            resumeButton.setFont(new Font("Monocraft", Font.PLAIN, 50));
            resumeButton.setForeground(new Color(0,255,255));
            resumeButton.setBackground(new Color(0,0,0,255));
            resumeButton.setBorder(BorderFactory.createLineBorder(new Color(0,255,255),5));



            resumeButton.addActionListener(e->{
                if(resumeButton.isEnabled()){
                    GameScreen.unpause();
                }
            });
            this.add(resumeButton);

            //spacing
            //this.add(Box.createRigidArea(new Dimension(0,50)));

            //main menu button
            quitButton=new JButton();
            quitButton.setText("Main menu");
            quitButton.setFont(new Font("Monocraft", Font.PLAIN, 50));
            quitButton.setForeground(new Color(0,255,255));
            quitButton.setBackground(new Color(0,0,0,255));
            quitButton.setBorder(BorderFactory.createLineBorder(new Color(0,255,255),5));


            quitButton.addActionListener(e->{
                if(quitButton.isEnabled()){
                    ((MainFrame)MainFrame.currentFrame).setCurrentStage(MainFrame.GAME_STAGES.TITLE_SCREEN);
                }
            });
            this.add(quitButton);

            this.onResize(screenWidth,screenHeight);

            if(!focusLost){
                this.requestFocus();
            }
        }

        /**
         * Überschreibt die resize-Funktion des Resizable-Interfaces
         * @param width die neue Breite des Fensters
         * @param height die neue Höhe des Fensters
         */
        public void onResize(int width, int height) {
            int currentY=height/2-324;

            title.setBounds(width/2-300,currentY,600,200);
            currentY+=300;

            resumeButton.setBounds(width/2-200,currentY,400,80);
            currentY+=130;

            quitButton.setBounds(width/2-200,currentY,400,80);
        }
    }

    /**
     * Das Sterbbildschrim. Es wird dann gezeigt, falls das Spiel gehalten ist.
     * Es vererbt von JPanel.
     */
    private static class DeathScreen extends JPanel implements Resizable{

        /**@hidden*/
        private JLabel title;
        /**@hidden*/
        private JButton restartButton;
        /**@hidden*/
        private JButton menuButton;

        /**
         * Erzeugt eine neue DeathScreen-Instanz und wird er auch nach Fokus fragen.
         * @param screenWidth Die Breite des Fensters.
         * @param screenHeight Die Höhe des Fensters.
         */
        public DeathScreen(int screenWidth, int screenHeight){
            super();

            this.setBackground(new Color(0,0,0,100));
            this.setLayout(null);
            //this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
            //this.setBorder(new EmptyBorder(new Insets(screenWidth/6,0,0,0)));


            //menu name
            title=new JLabel("You are dieded",SwingConstants.CENTER);
            title.setFont(new Font("Monocraft", Font.PLAIN, 100));
            title.setBackground(new Color(0,0,0,0));
            title.setForeground(new Color(255,0,0));

            this.add(title);

            //spacing
            //this.add(Box.createRigidArea(new Dimension(0,100)));

            //continue button
            restartButton=new JButton();
            restartButton.setText("Restart");
            restartButton.setFont(new Font("Monocraft", Font.PLAIN, 50));
            restartButton.setForeground(new Color(0,255,255));
            restartButton.setBackground(new Color(0,0,0,255));
            restartButton.setBorder(BorderFactory.createLineBorder(new Color(0,255,255),5));


            restartButton.addActionListener(e->{
                if(restartButton.isEnabled()){
                    GameScreen.undie();
                }
            });
            this.add(restartButton);

            //spacing
            //this.add(Box.createRigidArea(new Dimension(0,50)));

            //main menu button
            menuButton=new JButton();
            menuButton.setText("Main menu");
            menuButton.setFont(new Font("Monocraft", Font.PLAIN, 50));
            menuButton.setForeground(new Color(0,255,255));
            menuButton.setBackground(new Color(0,0,0,255));
            menuButton.setBorder(BorderFactory.createLineBorder(new Color(0,255,255),5));



            menuButton.addActionListener(e->{
                if(menuButton.isEnabled()){
                    ((MainFrame)MainFrame.currentFrame).setCurrentStage(MainFrame.GAME_STAGES.TITLE_SCREEN);
                }
            });
            this.add(menuButton);

            this.onResize(screenWidth,screenHeight);

            this.grabFocus();
        }

        /**
         * Überschreibt die resize-Funktion des Resizable-Interfaces
         * @param width die neue Breite des Fensters
         * @param height die neue Höhe des Fensters
         */
        public void onResize(int width, int height){
            int currentY=height/2-324;


            title.setBounds(width/2-500,currentY,1000,200);
            currentY+=300;

            restartButton.setBounds(width/2-200,currentY,400,80);
            currentY+=130;

            menuButton.setBounds(width/2-200,currentY,400,80);
        }
    }

    /**
     * Das Erfolgsbildschrim. Es wird dann gezeigt, falls das Spiel gehalten ist.
     * Es vererbt von JPanel.
     */
    private static class FinishScreen extends JPanel implements Resizable{

        /**@hidden*/
        private JLabel title;
        /**@hidden */
        private JLabel timeLabel;
        /**@hidden */
        private JLabel highscoreLabel;
        /**@hidden */
        private JButton restartButton;
        /**@hidden */
        private JButton menuButton;

        /**
         * Erzeugt eine neue FinishScreen-Instanz und wird er auch nach Fokus fragen.
         * @param screenWidth Die Breite des Fensters.
         * @param screenHeight Die Höhe des Fensters.
         * @param time Die Zeit der Spieler zum Absolvieren gebraucht.
         * @param highscore Die Bestzeit des Spielers.
         */
        public FinishScreen(int screenWidth, int screenHeight, String time, String highscore){
            super();

            this.setBackground(new Color(0,0,0,100));
            this.setLayout(null);
            //this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
            //this.setBorder(new EmptyBorder(new Insets(screenWidth/6,0,0,0)));


            //menu name
            title=new JLabel("meh",SwingConstants.CENTER);
            title.setFont(new Font("Monocraft", Font.PLAIN, 100));
            title.setBackground(new Color(0,0,0,0));
            title.setForeground(new Color(255,209,0));

            this.add(title);

            //time label
            timeLabel=new JLabel("Time: "+time,SwingConstants.CENTER);
            timeLabel.setFont(new Font("Monocraft", Font.PLAIN, 50));
            timeLabel.setBackground(new Color(0,0,0,0));
            timeLabel.setForeground(Color.white);

            this.add(timeLabel);

            //highscorelabel
            highscoreLabel=new JLabel("Best: "+highscore,SwingConstants.CENTER);
            highscoreLabel.setFont(new Font("Monocraft", Font.PLAIN, 50));
            highscoreLabel.setBackground(new Color(0,0,0,0));
            highscoreLabel.setForeground(Color.white);

            this.add(highscoreLabel);

            //spacing
            //this.add(Box.createRigidArea(new Dimension(0,100)));

            //continue button
            restartButton=new JButton();
            restartButton.setText("Restart");
            restartButton.setFont(new Font("Monocraft", Font.PLAIN, 50));
            restartButton.setForeground(new Color(0,255,255));
            restartButton.setBackground(new Color(0,0,0,255));
            restartButton.setBorder(BorderFactory.createLineBorder(new Color(0,255,255),5));



            restartButton.addActionListener(e->{
                if(restartButton.isEnabled()){
                    GameScreen.unfinish();
                }
            });
            this.add(restartButton);

            //spacing
            //this.add(Box.createRigidArea(new Dimension(0,50)));

            //main menu button
            menuButton=new JButton();
            menuButton.setText("Main menu");
            menuButton.setFont(new Font("Monocraft", Font.PLAIN, 50));
            menuButton.setForeground(new Color(0,255,255));
            menuButton.setBackground(new Color(0,0,0,255));
            menuButton.setBorder(BorderFactory.createLineBorder(new Color(0,255,255),5));


            menuButton.addActionListener(e->{
                if(menuButton.isEnabled()){
                    ((MainFrame)MainFrame.currentFrame).setCurrentStage(MainFrame.GAME_STAGES.TITLE_SCREEN);
                }
            });
            this.add(menuButton);

            this.onResize(screenWidth,screenHeight);

            this.grabFocus();
        }

        /**
         * Überschreibt die resize-Funktion des Resizable-Interfaces
         * @param width die neue Breite des Fensters
         * @param height die neue Höhe des Fensters
         */
        public void onResize(int width, int height) {
            int currentY=height/2-384;

            title.setBounds(width/2-500,currentY,1000,200);
            currentY+=250;

            timeLabel.setBounds(width/2-500,currentY,1000,50);
            currentY+=70;

            highscoreLabel.setBounds(width/2-500,currentY,1000,50);
            currentY+=150;

            restartButton.setBounds(width/2-200,currentY,400,80);
            currentY+=120;

            menuButton.setBounds(width/2-200,currentY,400,80);
        }
    }
}
