package main.java.org;

import main.java.org.InputManagement.InputManager;
import main.java.org.LinearAlgebruh.Matrix3;

import main.java.org.LinearAlgebruh.Vector3;
import main.java.org.Render.Camera.Camera;
import main.java.org.Render.Drawables.*;
import main.java.org.Render.RenderThread;
import main.java.org.Updateable.Player;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        /*var hehe=new JFrame();
        hehe.setSize(1000,500);
        hehe.add(new Macska());
        hehe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        hehe.setVisible(true);*/

        var hehe=new JFrame("Morbius Motorsport 2024 - GOTY Edition");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        hehe.setSize((int)screenSize.getWidth(),(int)screenSize.getHeight());
        hehe.setBackground(new Color(83,255,246));
        hehe.addKeyListener(new InputManager.KeyInput());

        var hehe2=new RenderThread((int)screenSize.getWidth(),(int)screenSize.getHeight());
        RenderThread.mainCamera.addDrawable(new Cube());
        var kubatemp=new Cube();
        kubatemp.setPosition(new Vector3(-5,2,7));
        kubatemp.setScale(new Vector3(1.0f,0.5f,2.0f));
        RenderThread.mainCamera.addDrawable(kubatemp);
        hehe2.addUpdateable(new Player());

        hehe.add(hehe2);
        hehe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        hehe.setVisible(true);

        InputManager.hideCursor(hehe);
        //InputManager.showCursor(hehe);

        Matrix3 macska=new Matrix3(new float[][]{{2,5,1},{-2,3,8},{8,-4,1}});
        System.out.println(macska);
    }
}