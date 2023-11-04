package main.java.org.InputManagement;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputManager  {
    public static boolean W=false;
    public static boolean A=false;
    public static boolean S=false;
    public static boolean D=false;

    public static class KeyInput extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W -> W = true;
                case KeyEvent.VK_A -> A = true;
                case KeyEvent.VK_S -> S = true;
                case KeyEvent.VK_D -> D = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W -> W = false;
                case KeyEvent.VK_A -> A = false;
                case KeyEvent.VK_S -> S = false;
                case KeyEvent.VK_D -> D = false;
            }
        }
    }
}
