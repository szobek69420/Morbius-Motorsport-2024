package main.java.org;

import main.java.org.InputManagement.InputManager;
import main.java.org.LinearAlgebruh.Matrix3;

import main.java.org.LinearAlgebruh.Vector3;
import main.java.org.Render.Camera.Camera;
import main.java.org.Render.Drawables.*;
import main.java.org.Screens.GameScreen;
import main.java.org.Screens.MainFrame;
import main.java.org.Updateable.Player;
import main.java.org.Updateable.Updateable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static final String mainDirectory=(new File(new File(new File(new File(new File("").getAbsolutePath(),"src"),"main"),"java"),"org")).getAbsolutePath();
    public static final String assetsDirectory=(new File(new File(new File(new File("").getAbsolutePath(),"src"),"main"),"assets")).getAbsolutePath();

    public static void main(String[] args) {

        try{
            loadFont();
        }
        catch (Exception e){
            System.err.println(e.getMessage());
            return;
        }

        new MainFrame("Morbius Motorsport 2024 - GOTY Edition");

        ((MainFrame)MainFrame.currentFrame).run();

        //InputManager.showCursor(hehe);
    }

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