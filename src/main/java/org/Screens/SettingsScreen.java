package main.java.org.Screens;

import main.java.org.AudioManagement.AudioManager;
import main.java.org.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;
import main.java.org.Settings.Settings;

/**
 * Eine Kindklasse von JPanel.
 * Beinhaltet den Stufenauswahlbildschirminhalt.
 */
public class SettingsScreen extends JPanel {
    /**
     * @hidden
     */
    private int screenWidth,screenHeight;

    /**
     * @hidden
     */
    private static final int[] fovValues={40,50,60,80};
    private static final String[] fovNames={"Tiny","Little","Normal","Gigachad"};

    /**
     * Erzeugt eine neue LevelSelectionScreen-Instanz
     * @param width Die Breite des Fensters
     * @param height Die Höhe des Fensters
     */
    public SettingsScreen(int width, int height) {
        screenWidth=width;
        screenHeight=height;

        this.setLayout(null);

        SettingsScreenBackground tb=new SettingsScreenBackground();
        tb.setBounds(0,0,screenWidth,screenHeight);

        SettingsScreenForeground tf=new SettingsScreenForeground();
        tf.setBounds(0,0,screenWidth,screenHeight);

        this.add(tf);
        this.add(tb);
    }



    /**
     * Der Vordergrund des Stufenauswahlbildschirminhaltes, er vererbt von JPanel.
     * Er beinhaltet die Stufenauswahltasten und noch eine Taste zum Zurückgang.
     */
    private class SettingsScreenForeground extends JPanel{

        public SettingsScreenForeground(){
            super();

            this.setLayout(null);
            this.setBackground(new Color(0,0,0,100));

            int contentWidth=600;
            int currentX=(screenWidth/2)-(contentWidth/2);
            int maxX=(screenWidth/2)+(contentWidth/2);
            int currentY=(screenHeight/2)-400;

            //title
            JLabel title=new JLabel("Settings",SwingConstants.CENTER);
            title.setFont(new Font("Monocraft", Font.PLAIN, 100));
            title.setBackground(new Color(0,0,0,0));
            title.setForeground(new Color(255,255,255));
            title.setBounds(currentX,currentY,contentWidth,100);
            this.add(title);

            //fov label
            JLabel fovLabel=new JLabel("FOV:",SwingConstants.LEFT);
            fovLabel.setFont(new Font("Monocraft", Font.PLAIN, 60));
            fovLabel.setBackground(new Color(0,0,0,0));
            fovLabel.setForeground(new Color(255,209,0));
            fovLabel.setBounds(currentX,currentY+200,200,80);
            this.add(fovLabel);

            //fov button
            int index2=0;
            for(int i=0;i<fovValues.length;i++) if(fovValues[i]==Settings.getFov()) index2=i;

            JButton fovButton=new JButton(fovNames[index2]);
            fovButton.setHorizontalAlignment(SwingConstants.CENTER);
            fovButton.setFont(new Font("Monocraft", Font.PLAIN, 50));
            fovButton.setBackground(new Color(0,0,0,255));
            fovButton.setForeground(new Color(0,255,255));
            fovButton.setBorder(BorderFactory.createLineBorder(new Color(0,255,255),5));
            fovButton.setBounds(maxX-300,currentY+200,300,80);

            fovButton.addActionListener(e->{
                int index=0;
                for(int i=0;i<fovValues.length;i++) if(fovValues[i]==Settings.getFov()) index=i;
                index=index==fovValues.length-1?0:index+1;
                Settings.setFov(fovValues[index]);
                fovButton.setText(fovNames[index]);
            });
            this.add(fovButton);

            //shadow label
            JLabel shadowLabel=new JLabel("Shadow:",SwingConstants.LEFT);
            shadowLabel.setFont(new Font("Monocraft", Font.PLAIN, 60));
            shadowLabel.setBackground(new Color(0,0,0,0));
            shadowLabel.setForeground(new Color(255,209,0));
            shadowLabel.setBounds(currentX,currentY+320,300,80);
            this.add(shadowLabel);

            //shadow button
            JButton shadowButton=new JButton(Settings.shadowShown()?"on":"off");
            shadowButton.setHorizontalAlignment(SwingConstants.CENTER);
            shadowButton.setFont(new Font("Monocraft", Font.PLAIN, 50));
            shadowButton.setBackground(new Color(0,0,0,255));
            shadowButton.setForeground(new Color(0,255,255));
            shadowButton.setBorder(BorderFactory.createLineBorder(new Color(0,255,255),5));
            shadowButton.setBounds(maxX-120,currentY+320,120,80);
            shadowButton.addActionListener(e->{
                boolean newVal=!Settings.shadowShown();
                shadowButton.setText(newVal?"on":"off");
                Settings.setShadow(newVal);
            });
            this.add(shadowButton);


            //music label
            JLabel musicLabel=new JLabel("Music:",SwingConstants.LEFT);
            musicLabel.setFont(new Font("Monocraft", Font.PLAIN, 60));
            musicLabel.setBackground(new Color(0,0,0,0));
            musicLabel.setForeground(new Color(255,209,0));
            musicLabel.setBounds(currentX,currentY+440,300,80);
            this.add(musicLabel);

            //music button
            JButton musicButton=new JButton(Settings.musicOn()?"on":"off");
            musicButton.setHorizontalAlignment(SwingConstants.CENTER);
            musicButton.setFont(new Font("Monocraft", Font.PLAIN, 50));
            musicButton.setBackground(new Color(0,0,0,255));
            musicButton.setForeground(new Color(0,255,255));
            musicButton.setBorder(BorderFactory.createLineBorder(new Color(0,255,255),5));
            musicButton.setBounds(maxX-120,currentY+440,120,80);
            musicButton.addActionListener(e->{
                boolean newVal=!Settings.musicOn();
                musicButton.setText(newVal?"on":"off");
                Settings.setMusic(newVal);
            });
            this.add(musicButton);

            //sfx label
            JLabel sfxLabel=new JLabel("SFX:",SwingConstants.LEFT);
            sfxLabel.setFont(new Font("Monocraft", Font.PLAIN, 60));
            sfxLabel.setBackground(new Color(0,0,0,0));
            sfxLabel.setForeground(new Color(255,209,0));
            sfxLabel.setBounds(currentX,currentY+560,300,80);
            this.add(sfxLabel);

            //sfx button
            JButton sfxButton=new JButton(Settings.sfxOn()?"on":"off");
            sfxButton.setHorizontalAlignment(SwingConstants.CENTER);
            sfxButton.setFont(new Font("Monocraft", Font.PLAIN, 50));
            sfxButton.setBackground(new Color(0,0,0,255));
            sfxButton.setForeground(new Color(0,255,255));
            sfxButton.setBorder(BorderFactory.createLineBorder(new Color(0,255,255),5));
            sfxButton.setBounds(maxX-120,currentY+560,120,80);
            sfxButton.addActionListener(e->{
                boolean newVal=!Settings.sfxOn();
                sfxButton.setText(newVal?"on":"off");
                Settings.setSfx(newVal);
            });
            this.add(sfxButton);

            //back
            JButton backButton=new JButton("Save changes");
            backButton.setHorizontalAlignment(SwingConstants.CENTER);
            backButton.setFont(new Font("Monocraft", Font.PLAIN, 50));
            backButton.setBackground(new Color(0,0,0,255));
            backButton.setForeground(new Color(0,255,255));
            backButton.setBorder(BorderFactory.createLineBorder(new Color(0,255,255),5));

            backButton.setBounds(screenWidth/2-250,currentY+700,500,80);

            final boolean previousMusicState=Settings.musicOn();
            backButton.addActionListener(e->{
                if(backButton.isEnabled()){
                    Settings.updateSettings();
                    if(Settings.musicOn()&& !previousMusicState)
                        AudioManager.playSound(AudioManager.SOUNDS.MUSIC);
                    else if(!Settings.musicOn())
                        AudioManager.closeAll();
                    ((MainFrame)MainFrame.currentFrame).setCurrentStage(MainFrame.GAME_STAGES.TITLE_SCREEN);
                }
            });

            this.add(backButton);
        }
    }

    /**
     * Der Hintergrund des Stufenauswahlbildschirminhaltes, er vererbt von JPanel.
     * Er beinhaltet das Hintergrundbild.
     */
    private class SettingsScreenBackground extends JPanel{
        /**
         * Das eingeladene Bild
         */
        private BufferedImage background;

        /**
         * Erzeugt eine neue LevelSelectionScreenBackground-Instanz und ladet das Hintergrundbild ein
         */
        public SettingsScreenBackground(){
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
