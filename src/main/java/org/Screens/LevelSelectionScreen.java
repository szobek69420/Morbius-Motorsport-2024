package main.java.org.Screens;

import main.java.org.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;

public class LevelSelectionScreen extends JPanel {

    public static final int LEVEL_COUNT=5;

    private int screenWidth,screenHeight;
    public LevelSelectionScreen(int width, int height) {
        screenWidth=width;
        screenHeight=height;

        LevelData[] levelsDone=fetchLevelData();

        this.setLayout(null);

        LevelSelectionScreenBackground tb=new LevelSelectionScreenBackground();
        tb.setBounds(0,0,screenWidth,screenHeight);

        LevelSelectionScreenForeground tf=new LevelSelectionScreenForeground(levelsDone);
        tf.setBounds(0,0,screenWidth,screenHeight);

        this.add(tf);
        this.add(tb);
    }

    private LevelData[] fetchLevelData(){
        LevelData[] leveldata=new LevelData[LEVEL_COUNT];


        File levelData=new File(Main.dataDirectory,"1D956EA5DD9E1C32BBA10314C01BDAA63A18ED59D7B.bingchilling");
        if(levelData.exists()){
            try(Scanner sc=new Scanner(levelData)){
                for(int i=0;i<LEVEL_COUNT;i++){
                    boolean done=sc.nextBoolean();
                    double highscore=0.01*sc.nextInt();

                    leveldata[i]=new LevelData(done,highscore);
                }
            }
            catch (IOException ex){
                System.err.println("bro what");
            }
        }
        else{
            try(PrintWriter pw=new PrintWriter(new FileWriter(levelData))){
                for(int i=0;i<LEVEL_COUNT;i++){
                    pw.println(false);
                    pw.println(-1);
                    leveldata[i]=new LevelData(false,-1);
                }
            }
            catch(IOException ex){
                System.err.println("bro what 2");
            }
        }

        return leveldata;
    }

    private static class LevelData{
        public double highscore;
        public boolean done;

        public LevelData(boolean done, double highscore){
            this.done=done;
            this.highscore=highscore;
        }
    }


    private class LevelSelectionScreenForeground extends JPanel{
        public LevelSelectionScreenForeground(LevelData[] levelData){
            super();

            this.setLayout(null);
            this.setBackground(new Color(0,0,0,100));

            int contentWidth=150*LEVEL_COUNT+50*(LEVEL_COUNT-1);
            int currentX=(screenWidth/2)-(contentWidth/2);
            int currentY=(screenHeight/2)-75;

            //title
            JLabel title=new JLabel("Select level",SwingConstants.CENTER);
            title.setFont(new Font("Monocraft", Font.PLAIN, 100));
            title.setBackground(new Color(0,0,0,0));
            title.setForeground(new Color(255,255,255));

            title.setBounds(currentX,currentY-250,contentWidth,100);
            this.add(title);

            //level start buttons
            MainFrame.LEVELS[] levels=new MainFrame.LEVELS[]{MainFrame.LEVELS.LEVEL_1, MainFrame.LEVELS.LEVEL_2, MainFrame.LEVELS.LEVEL_3, MainFrame.LEVELS.LEVEL_4, MainFrame.LEVELS.LEVEL_5};
            for(int i=1;i<=LEVEL_COUNT;i++){
                var butt=new JButton();
                butt.setText(((Integer)i).toString());
                butt.setHorizontalAlignment(SwingConstants.CENTER);

                butt.setFont(new Font("Monocraft", Font.PLAIN, 80));
                butt.setBackground(new Color(0,0,0,255));

                if(levelData[i-1].done){
                    butt.setForeground(new Color(0,255,255));
                    butt.setBorder(BorderFactory.createLineBorder(Color.green,5));
                }
                else{
                    butt.setForeground(new Color(0,255,255));
                    butt.setBorder(BorderFactory.createLineBorder(Color.white,5));
                }

                butt.setBounds(currentX,currentY,150,150);
                currentX+=200;

                MainFrame.LEVELS level=levels[i-1];
                double highscore=levelData[i-1].highscore;
                butt.addActionListener(e->{
                    if(butt.isEnabled()){
                        ((MainFrame)MainFrame.currentFrame).setCurrentLevel(level);
                        ((MainFrame)MainFrame.currentFrame).setHighscore(highscore);
                        ((MainFrame)MainFrame.currentFrame).setCurrentStage(MainFrame.GAME_STAGES.GAME);
                    }
                });
                this.add(butt);
            }

            //back
            JButton backButton=new JButton("Go back");
            backButton.setHorizontalAlignment(SwingConstants.CENTER);
            backButton.setFont(new Font("Monocraft", Font.PLAIN, 50));
            backButton.setBackground(new Color(0,0,0,255));
            backButton.setForeground(new Color(0,255,255));
            backButton.setBorder(BorderFactory.createLineBorder(new Color(0,255,255),5));

            backButton.setBounds(screenWidth/2-150,currentY+250,300,80);

            backButton.addActionListener(e->{
                if(backButton.isEnabled()){
                    ((MainFrame)MainFrame.currentFrame).setCurrentStage(MainFrame.GAME_STAGES.TITLE_SCREEN);
                }
            });

            this.add(backButton);
        }
    }

    private class LevelSelectionScreenBackground extends JPanel{

        private BufferedImage background;
        public LevelSelectionScreenBackground(){
            super();

            this.setLayout(null);
            this.setBackground(new Color(0,0,0,255));

            File bgFile=new File(new File(Main.assetsDirectory,"sprites"),"bg_3.png");
            //System.out.println(bgFile.getAbsolutePath());

            background=null;
            try{
                background= ImageIO.read(bgFile);
            }
            catch(IOException e){
                System.err.println("Title background could not be loaded");
            }
        }

        @Override
        public void paint(Graphics g){
            super.paintComponents(g);

            if(background!=null){
                g.drawImage(background, 0, 0, null);
            }
        }
    }

    public static void saveHighscore(MainFrame.LEVELS level, double highscoreInSeconds){
        int levelNumber=MainFrame.getLevelNumber(level);

        File levelDataFile=new File(Main.dataDirectory,"1D956EA5DD9E1C32BBA10314C01BDAA63A18ED59D7B.bingchilling");

        LevelData[] levelData=new LevelData[LEVEL_COUNT];

        try(Scanner sc=new Scanner(levelDataFile)){
            for(int i=0;i<LEVEL_COUNT;i++){
                boolean done=sc.nextBoolean();
                double highscore=0.01*sc.nextInt();

                levelData[i]=new LevelData(done,highscore);
            }
        }
        catch (IOException ex){
            System.err.println("bro what 3");
        }

        levelData[levelNumber].highscore=highscoreInSeconds;
        levelData[levelNumber].done=true;
        try(PrintWriter pw=new PrintWriter(new FileWriter(levelDataFile))){
            for(int i=0;i<LEVEL_COUNT;i++){
                pw.println(levelData[i].done);
                pw.println((int)Math.round(levelData[i].highscore*100));
            }
        }
        catch(IOException ex){
                System.err.println("bro what 4");
        }
    }
}
