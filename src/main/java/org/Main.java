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
        RenderThread.mainCamera.addDrawable(new Cube(Color.red));
        var kubatemp=new Cube(Color.green);
        kubatemp.setPosition(new Vector3(-5,2,7));
        kubatemp.setScale(new Vector3(1.0f,0.5f,2.0f));
        kubatemp.setName("amogus2");
        RenderThread.mainCamera.addDrawable(kubatemp);

        var kubatemp2=new Cube(Color.magenta);
        kubatemp2.setPosition(new Vector3(0,-10,0));
        kubatemp2.setScale(new Vector3(6.0f,6f,6.0f));
        kubatemp2.setName("amogus3");
        RenderThread.mainCamera.addDrawable(kubatemp2);
        Vector3[] pens=new Vector3[]{new Vector3(50,20,0),new Vector3(1,20,1),new Vector3(50,-1,-2),new Vector3(1,1,1),new Vector3(50,-1,2),new Vector3(1,1,1)};
        for(int i=0;i<pens.length/2;i++){
            var kubatemp3=new Cube(Color.yellow);
            kubatemp3.setPosition(pens[i*2]);
            kubatemp3.setScale(pens[i*2+1]);
            RenderThread.mainCamera.addDrawable(kubatemp3);
        }
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