package main.java.org;

import main.java.org.InputManagement.InputManager;
import main.java.org.LinearAlgebruh.Matrix3;

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
        hehe.addKeyListener(new InputManager.KeyInput());

        var hehe2=new RenderThread((int)screenSize.getWidth(),(int)screenSize.getHeight());
        RenderThread.mainCamera.addDrawable(new Cube());
        hehe2.addUpdateable(new Player());

        hehe.add(hehe2);
        hehe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        hehe.setVisible(true);

        Matrix3 macska=new Matrix3(new float[][]{{2,5,1},{-2,3,8},{8,-4,1}});
        System.out.println(macska);

    }
}