package main.java.org.Settings;

import main.java.org.Main;
import main.java.org.Screens.MainFrame;

import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Settings {
    private static int fov=60;
    private static boolean shadow=true;
    private static boolean sfx=true;
    private static boolean music=true;

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

    public static int getFov(){
        return fov;
    }

    public static void setFov(int fov){
        Settings.fov=fov;
    }

    public static boolean shadowShown(){
        return shadow;
    }

    public static void setShadow(boolean shadowShown){
        Settings.shadow=shadowShown;
    }

    public static boolean sfxOn(){
        return sfx;
    }

    public static void setSfx(boolean sfx){
        Settings.sfx=sfx;
    }

    public static boolean musicOn(){
        return music;
    }

    public static void setMusic(boolean music){
        Settings.sfx=music;
    }
}
