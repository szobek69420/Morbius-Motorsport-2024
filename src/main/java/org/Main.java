package main.java.org;

import main.java.org.InputManagement.InputManager;
import main.java.org.LinearAlgebruh.Matrix3;

import main.java.org.LinearAlgebruh.Vector3;
import main.java.org.Render.Camera.Camera;
import main.java.org.Render.Drawables.*;
import main.java.org.Screens.GameScreen;
import main.java.org.Screens.MainFrame;
import main.java.org.Settings.Settings;
import main.java.org.Updateable.Player;
import main.java.org.Updateable.Updateable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Main Klasse
 */
public class Main {
    /**
     * Die Lokation der Spielresourcen
     */
    public static final String assetsDirectory=(new File(new File("").getAbsolutePath(),"src/main/assets")).getAbsolutePath();
    /**
     * Die Lokation der Spieldaten
     */
    public static final String dataDirectory=(new File(new File("").getAbsolutePath(),"src/main/data")).getAbsolutePath();
    /**
     * Wurde das Spiel schon geladen?
     */
    private static boolean notLoaded=true;

    /**
     * das Herz und die Seele des Spieles
     * @param args Kommandozeilenargumente. Sie werden ignoriert.
     */
    public static void main(String[] args) {

        if(notLoaded){
            notLoaded=false;

            try{
                loadFont();
            }
            catch (Exception e){
                System.err.println(e.getMessage());
                return;
            }

            Settings.fetchSettings();

            var frame=new MainFrame("Morbius Motorsport 2024 - GOTY Edition");
            //frame.setResizable(false);

            ((MainFrame)MainFrame.currentFrame).start();
        }

        //InputManager.showCursor(hehe);
    }

    /**
     * Ladet die Schriftdatei
     * @throws Exception falls etwas beim Laden schiefgegangen ist
     */
    private static void loadFont() throws Exception{
        try {
            File based=new File(assetsDirectory,"fonts");
            based=new File(based,"Monocraft.ttf");

            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, based));
        } catch (IOException e) {
            throw new Exception("No minecraft font :(");
        }
    }
}