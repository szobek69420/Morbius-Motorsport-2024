package main.java.org.InputManagement;

import main.java.org.Screens.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class InputManager  {

    private static final Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    private static int basedMouseX=Toolkit.getDefaultToolkit().getScreenSize().width/2;
    private static int basedMouseY=Toolkit.getDefaultToolkit().getScreenSize().height/2;
    public static int deltaMouseX=0;
    public static int deltaMouseY=0;

    public static void fetchMousePosition(){
        Point p=MouseInfo.getPointerInfo().getLocation();
        deltaMouseX=p.x-basedMouseX;
        deltaMouseY=p.y-basedMouseY;

        robot.mouseMove(basedMouseX,basedMouseY);
    }

    public static void hideCursor(JFrame frame){
        frame.setCursor(frame.getToolkit().createCustomCursor(
                new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
                "null"));
    }

    public static void showCursor(JFrame frame){
        frame.setCursor(Cursor.getDefaultCursor());
    }


    public static boolean W=false;
    public static boolean A=false;
    public static boolean S=false;
    public static boolean D=false;

    public static boolean SPACE=false;
    public static boolean L_SHIT=false;

    public static boolean UP=false;
    public static boolean DOWN=false;
    public static boolean LEFT=false;
    public static boolean RIGHT=false;

    public static class KeyInput extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W -> W = true;
                case KeyEvent.VK_A -> A = true;
                case KeyEvent.VK_S -> S = true;
                case KeyEvent.VK_D -> D = true;
                case KeyEvent.VK_SPACE->SPACE=true;
                case KeyEvent.VK_SHIFT->L_SHIT=true;
                case KeyEvent.VK_UP->UP=true;
                case KeyEvent.VK_DOWN->DOWN=true;
                case KeyEvent.VK_LEFT->LEFT=true;
                case KeyEvent.VK_RIGHT->RIGHT=true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W -> W = false;
                case KeyEvent.VK_A -> A = false;
                case KeyEvent.VK_S -> S = false;
                case KeyEvent.VK_D -> D = false;
                case KeyEvent.VK_SPACE->SPACE=false;
                case KeyEvent.VK_SHIFT->L_SHIT=false;
                case KeyEvent.VK_UP->UP=false;
                case KeyEvent.VK_DOWN->DOWN=false;
                case KeyEvent.VK_LEFT->LEFT=false;
                case KeyEvent.VK_RIGHT->RIGHT=false;

                case KeyEvent.VK_ESCAPE -> {
                    if(!GameScreen.isPaused()) GameScreen.pause();
                }
            }
        }
    }
}
