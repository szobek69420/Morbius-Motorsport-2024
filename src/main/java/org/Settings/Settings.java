package main.java.org.Settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.org.Main;
import main.java.org.Screens.MainFrame;
import main.java.org.Screens.SettingsScreen;

import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Scanner;

/**
 * Eine statische Klasse für die Sammlung der Einstellungen
 */
public class Settings {

    private static class SettingsContainer{
        public int fov;
        public boolean shadow;
        public boolean sfx;
        public boolean music;

        public SettingsContainer(int fov, boolean shadow, boolean sfx, boolean music){
            this.fov=fov;
            this.shadow=shadow;
            this.sfx=sfx;
            this.music=music;
        }
    }

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

        try(BufferedReader br=new BufferedReader(new FileReader(settingsFile))){
            StringBuilder settingsJasonBuilder=new StringBuilder();

            while(true){
                String line=br.readLine();
                if(line==null)
                    break;
                settingsJasonBuilder.append(line);
            }

            String settingsJason=settingsJasonBuilder.toString();
            SettingsContainer sc=new GsonBuilder().setPrettyPrinting().create().fromJson(settingsJason,SettingsContainer.class);

            fov=sc.fov;
            shadow=sc.shadow;
            sfx=sc.sfx;
            music=sc.music;
        }
        catch (Exception ex){
            System.err.println("Settings file could not be opened");
            System.exit(69);
        }
    }

    /**
     * Speichert die aktualisierten Einstellungen
     */
    public static synchronized void updateSettings(){
        File settingsFile=new File(Main.dataDirectory,"B5774F81D2BCCCF2D5305EB080D2AD206A3DFF317B4B.bingchilling");

        try(PrintWriter pw=new PrintWriter(new FileWriter(settingsFile)))
        {
            SettingsContainer sc=new SettingsContainer(fov, shadow,sfx,music);
            String settingsJason = new GsonBuilder().setPrettyPrinting().create().toJson(sc);
            pw.println(settingsJason);
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
