package main.java.org.Settings;

import main.java.org.Main;
import main.java.org.Screens.MainFrame;

import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Eine statische Klasse für die Sammlung der Einstellungen
 */
public class Settings {
    /**
     * @hidden
     */
    private static int fov=60;
    /**
     * @hidden
     */
    private static boolean shadow=true;
    /**
     * @hidden
     */
    private static boolean sfx=true;
    /**
     * @hidden
     */
    private static boolean music=true;

    /**
     * Einladet die gespeicherten Einstellungen
     */
    public static synchronized void fetchSettings(){
        File settingsFile=new File(Main.dataDirectory,"B5774F81D2BCCCF2D5305EB080D2AD206A3DFF317B4B.bingchilling");

        try(Scanner sc=new Scanner(settingsFile)){
            fov=sc.nextInt();
            shadow=sc.nextBoolean();
            sfx=sc.nextBoolean();
            music=sc.nextBoolean();
        }
        catch (Exception ex){
            System.err.println("Settings file could not be opened");
            MainFrame.currentFrame.dispatchEvent(new WindowEvent(MainFrame.currentFrame, WindowEvent.WINDOW_CLOSING));
        }
    }

    /**
     * Speichert die aktualisierten Einstellungen
     */
    public static synchronized void updateSettings(){
        File settingsFile=new File(Main.dataDirectory,"B5774F81D2BCCCF2D5305EB080D2AD206A3DFF317B4B.bingchilling");

        try(PrintWriter pw=new PrintWriter(new FileWriter(settingsFile)))
        {
            pw.println(fov);
            pw.println(shadow);
            pw.println(sfx);
            pw.println(music);
        }
        catch(Exception ex){
            System.err.println("Settings file could not be written");
            MainFrame.currentFrame.dispatchEvent(new WindowEvent(MainFrame.currentFrame, WindowEvent.WINDOW_CLOSING));
        }
    }

    /**
     * Gibt die Sichtfeldgröße zurück
     * @return die Sichtfeldgröße
     */
    public static int getFov(){
        return fov;
    }

    /**
     * Stellt die Sichtfeldgröße ein
     * @param fov die neue Sichtfeldgröße
     */
    public static void setFov(int fov){
        Settings.fov=fov;
    }

    /**
     * Wird der Schatten des Spielers gezeichnet?
     * @return TRUE, falls der Schatten gezeichnet wird
     */
    public static boolean shadowShown(){
        return shadow;
    }

    /**
     * Einstellt, dass der Schatten des Spielers gezeichnet wird oder nicht
     * @param shadowShown TRUE, falls der Schatten gezeichnet wird
     */
    public static void setShadow(boolean shadowShown){
        Settings.shadow=shadowShown;
    }

    /**
     * Zurückgibt, ob die Toneffekte gespielt werden
     * @return TRUE, falls die Toneffekte gespielt werden
     */
    public static boolean sfxOn(){
        return sfx;
    }

    /**
     * Einstellt, dass die Toneffekte gespielt werden oder nicht
     * @param sfx TRUE, falls die Toneffekte gespielt werden
     */
    public static void setSfx(boolean sfx){
        Settings.sfx=sfx;
    }

    /**
     * Zurückgibt, ob die Musik gespielt wird
     * @return TRUE, falls die Musik gespielt wird
     */
    public static boolean musicOn(){
        return music;
    }

    /**
     * Einstellt, dass die Musik gespielt wird oder nicht
     * @param music TRUE, falls die Musik gespielt wird
     */
    public static void setMusic(boolean music){
        Settings.music=music;
    }
}
