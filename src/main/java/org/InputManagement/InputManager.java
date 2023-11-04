package main.java.org.InputManagement;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputManager  {
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
            }
        }
    }
}
