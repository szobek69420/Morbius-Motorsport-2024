package main.java.org.Screens;

import main.java.org.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TitleScreen extends JPanel {

    private int screenWidth,screenHeight;
    public TitleScreen(int width, int height) {
        screenWidth=width;
        screenHeight=height;

        this.setLayout(null);

        TitleScreenBackground tb=new TitleScreenBackground();
        tb.setBounds(0,0,screenWidth,screenHeight);

        TitleScreenForeground tf=new TitleScreenForeground();
        tf.setBounds(0,0,screenWidth,screenHeight);

        this.add(tf);
        this.add(tb);
    }


    private class TitleScreenForeground extends JPanel{
        public TitleScreenForeground(){
            super();

            this.setLayout(null);
            this.setBackground(new Color(0,0,0,0));

            //title
            JLabel title=new JLabel("MORBIUS",SwingConstants.LEFT);
            title.setFont(new Font("Monocraft", Font.PLAIN, 100));
            title.setBackground(new Color(0,0,0,0));
            title.setForeground(new Color(255,209,0));

            title.setBounds(200,100,700,100);
            this.add(title);

            JLabel title2=new JLabel("MOTORSPORT",SwingConstants.LEFT);
            title2.setFont(new Font("Monocraft", Font.PLAIN, 100));
            title2.setBackground(new Color(0,0,0,0));
            title2.setForeground(new Color(255,209,0));

            title2.setBounds(200,200,700,100);
            this.add(title2);

            JLabel title3=new JLabel("2024",SwingConstants.LEFT);
            title3.setFont(new Font("Monocraft", Font.PLAIN, 250));
            title3.setBackground(new Color(0,0,0,0));
            title3.setForeground(new Color(0,255,255));

            title3.setBounds(900,100,700,200);
            this.add(title3);

            JLabel title4=new JLabel("GOTY Edition",SwingConstants.LEFT);
            title4.setFont(new Font("Monocraft", Font.PLAIN, 50));
            title4.setBackground(new Color(0,0,0,0));
            title4.setForeground(new Color(255,255,255));

            title4.setBounds(200,300,700,50);
            this.add(title4);

            //start button
            var butt=new JButton();
            butt.setText("It's morbin' time");
            butt.setFont(new Font("Monocraft", Font.PLAIN, 40));
            butt.setForeground(Color.white);
            butt.setBackground(new Color(0,0,0,255));
            butt.setBorder(BorderFactory.createLineBorder(Color.white,5));

            butt.setBounds(200,screenHeight/2+100,600,80);

            butt.addActionListener(e->{
                if(butt.isEnabled()){
                    ((MainFrame)MainFrame.currentFrame).setCurrentStage(MainFrame.GAME_STAGES.GAME);
                }
            });
            this.add(butt);

            //quit button
            var butt2=new JButton();
            butt2.setText("I'm gay (quit)");
            butt2.setFont(new Font("Monocraft", Font.PLAIN, 40));
            butt2.setForeground(Color.white);
            butt2.setBackground(new Color(0,0,0,255));
            butt2.setBorder(BorderFactory.createLineBorder(Color.white,5));

            butt2.setBounds(200,screenHeight/2+220,600,80);

            butt2.addActionListener(e->{
                if(butt2.isEnabled()){
                    MainFrame.currentFrame.dispatchEvent(new WindowEvent(MainFrame.currentFrame, WindowEvent.WINDOW_CLOSING));
                }
            });
            this.add(butt2);
        }
    }

    private class TitleScreenBackground extends JPanel{

        private BufferedImage background;
        public TitleScreenBackground(){
            super();

            this.setLayout(null);
            this.setBackground(new Color(0,0,0,255));

            File bgFile=new File(new File(Main.assetsDirectory,"sprites"),"bg_3.png");
            System.out.println(bgFile.getAbsolutePath());

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
}
