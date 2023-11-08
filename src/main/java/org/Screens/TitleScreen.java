package main.java.org.Screens;

import main.java.org.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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


            //start button
            var butt=new JButton();
            butt.setText("Start");
            butt.setFont(new Font("Monocraft", Font.PLAIN, 50));
            butt.setForeground(Color.white);
            butt.setBackground(new Color(0,0,0,255));


            butt.setBounds(200,screenHeight/2,400,80);

            butt.addActionListener(e->{
                if(butt.isEnabled()){
                    ((MainFrame)MainFrame.currentFrame).setCurrentStage(MainFrame.GAME_STAGES.GAME);
                }
            });
            this.add(butt);
        }
    }

    private class TitleScreenBackground extends JPanel{

        private BufferedImage background;
        public TitleScreenBackground(){
            super();

            this.setLayout(null);
            this.setBackground(new Color(0,0,0,255));

            File bgFile=new File(new File(Main.assetsDirectory,"sprites"),"bg_1.png");
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
