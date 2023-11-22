package main.java.org.Screens;

import main.java.org.Main;
import main.java.org.Resizable.Resizable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Eine Kindklasse von JPanel.
 * Beinhaltet den Titelbildschirminhalt.
 */
public class TitleScreen extends JPanel implements Resizable {

    /**
     * Die Größe des Bildschirmes
     */
    private int screenWidth,screenHeight;

    /**
     * @hidden
     */
    private TitleScreenForeground tf;
    /**
     * @hidden
     */
    private TitleScreenBackground tb;

    /**
     * Erzeugt eine neue TitleScreen-Instanz.
     * Sein Inhalt besteht aus zwei Teilen: Vordergrund (TitleScreenForeground) und Hintergrund (TitleScreenBackground)
     * @param width die Breite des Bildschirmes in Pixels
     * @param height die Höhe des Bildschrimes in Pixels
     */
    public TitleScreen(int width, int height) {
        screenWidth=width;
        screenHeight=height;

        this.setLayout(null);

        tb=new TitleScreenBackground();
        tb.setBounds(0,0,screenWidth,screenHeight);

        tf=new TitleScreenForeground();
        tf.setBounds(0,0,screenWidth,screenHeight);

        this.add(tf);
        this.add(tb);
    }

    /**
     * Überschreibt die resize-Funktion des Resizable-Interfaces
     * @param width die neue Breite des Fensters
     * @param height die neue Höhe des Fensters
     */
    public void onResize(int width, int height){
        tb.setBounds(0,0,width,height);
        tf.setBounds(0,0,width,height);
        tf.onResize(width,height);
    }

    /**
     * Der Vordergrund des Inhaltes, er vererbt von JPanel.
     * Er beinhaltet die Starttaste, die Fliehtaste und den Title.
     */
    private class TitleScreenForeground extends JPanel implements Resizable{
        /**
         * @hidden
         */
        private JLabel title;
        /**
         * @hidden
         */
        private JLabel title2;
        /**
         * @hidden
         */
        private JLabel title3;
        /**
         * @hidden
         */
        private JLabel title4;

        /**
         * @hidden
         */
        private JButton butt;
        /**
         * @hidden
         */
        private JButton butt2;
        /**
         * @hidden
         */
        private JButton butt3;


        public TitleScreenForeground(){
            super();

            this.setLayout(null);
            this.setBackground(new Color(0,0,0,0));

            //title
            title=new JLabel("MORBIUS",SwingConstants.LEFT);
            title.setFont(new Font("Monocraft", Font.PLAIN, 100));
            title.setBackground(new Color(0,0,0,0));
            title.setForeground(new Color(255,209,0));

            this.add(title);

            title2=new JLabel("MOTORSPORT",SwingConstants.LEFT);
            title2.setFont(new Font("Monocraft", Font.PLAIN, 100));
            title2.setBackground(new Color(0,0,0,0));
            title2.setForeground(new Color(255,209,0));

            this.add(title2);

            title3=new JLabel("2024",SwingConstants.LEFT);
            title3.setFont(new Font("Monocraft", Font.PLAIN, 250));
            title3.setBackground(new Color(0,0,0,0));
            title3.setForeground(new Color(0,255,255));

            this.add(title3);

            title4=new JLabel("GOTY Edition",SwingConstants.LEFT);
            title4.setFont(new Font("Monocraft", Font.PLAIN, 50));
            title4.setBackground(new Color(0,0,0,0));
            title4.setForeground(new Color(255,255,255));

            this.add(title4);

            //start button
            butt=new JButton();
            butt.setText("It's morbin' time");
            butt.setFont(new Font("Monocraft", Font.PLAIN, 40));
            butt.setForeground(new Color(0,255,255));
            butt.setBackground(new Color(0,0,0,255));
            butt.setBorder(BorderFactory.createLineBorder(new Color(0,255,255),5));


            butt.addActionListener(e->{
                if(butt.isEnabled()){
                    ((MainFrame)MainFrame.currentFrame).setCurrentStage(MainFrame.GAME_STAGES.LEVEL_SELECTOR);
                }
            });
            this.add(butt);

            //settings button
            butt3=new JButton();
            butt3.setText("Morb the game");
            butt3.setFont(new Font("Monocraft", Font.PLAIN, 40));
            butt3.setForeground(new Color(0,255,255));
            butt3.setBackground(new Color(0,0,0,255));
            butt3.setBorder(BorderFactory.createLineBorder(new Color(0,255,255),5));


            butt3.addActionListener(e->{
                if(butt3.isEnabled()){
                    ((MainFrame)MainFrame.currentFrame).setCurrentStage(MainFrame.GAME_STAGES.SETTINGS);
                }
            });
            this.add(butt3);

            //quit button
            butt2=new JButton();
            butt2.setText("I'm gay (quit)");
            butt2.setFont(new Font("Monocraft", Font.PLAIN, 40));
            butt2.setForeground(new Color(0,255,255));
            butt2.setBackground(new Color(0,0,0,255));
            butt2.setBorder(BorderFactory.createLineBorder(new Color(0,255,255),5));


            butt2.addActionListener(e->{
                if(butt2.isEnabled()){
                    MainFrame.currentFrame.dispatchEvent(new WindowEvent(MainFrame.currentFrame, WindowEvent.WINDOW_CLOSING));
                }
            });
            this.add(butt2);

            this.onResize(screenWidth,screenHeight);
        }

        /**
         * Überschreibt die resize-Funktion des Resizable-Interfaces
         * @param width die neue Breite des Fensters
         * @param height die neue Höhe des Fensters
         */
        public void onResize(int width, int height){
            title.setBounds(200,100,700,100);
            title2.setBounds(200,200,700,100);
            title3.setBounds(900,100,700,200);
            title4.setBounds(200,300,700,50);

            butt.setBounds(200,height-540,600,80);
            butt3.setBounds(200,height-420,600,80);
            butt2.setBounds(200,height-300,600,80);
        }
    }

    /**
     * Der Hintergrund des Inhaltes, er vererbt von JPanel.
     * Er beinhaltet das Hintergrundbild.
     */
    private class TitleScreenBackground extends JPanel{
        /**
         * Das eingeladene Bild
         */
        private BufferedImage background;

        /**
         * Erzeugt eine neue LevelSelectionScreenBackground-Instanz und ladet das Hintergrundbild ein
         */
        public TitleScreenBackground(){
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

        /**
         * Überschreibt die paint-Funktion von JComponent, um den Inhalt besser angepasst werden zu können.
         * @param g  the <code>Graphics</code> context in which to paint
         */
        @Override
        public void paint(Graphics g){
            super.paint(g);

            if(background!=null){
                g.drawImage(background, 0, 0, null);
            }
        }
    }
}
