package main.java.org.Render;

import main.java.org.InputManagement.InputManager;
import main.java.org.Render.Camera.Camera;
import main.java.org.Render.Drawables.Cube;
import main.java.org.Updateable.Updateable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RenderThread extends JPanel implements Runnable{
    public static Camera mainCamera;
    public ArrayList<Updateable> updateables;

    private Thread thread;

    public RenderThread(int width, int height){
        mainCamera=new Camera(width,height);

        updateables=new ArrayList<>();

        thread=new Thread(this);
        thread.start();
    }

    @Override
    public void run(){
        //game loop
        long lastTime = System.nanoTime();
        double deltaTime;
        while(true) {
            long now = System.nanoTime();
            deltaTime = (now -lastTime)*0.000000001;

            if(deltaTime>0.01666){
                lastTime = now;

                InputManager.fetchMousePosition();

                repaint();

                for(Updateable u : updateables)
                    u.Update(deltaTime);
            }
        }
    }

    @Override
    public void paint(Graphics g){
        Image image = createImage(getWidth(),getHeight());
        Graphics graphics = image.getGraphics();

        mainCamera.render(graphics);

        g.drawImage(image,0,0,this);
    }

    public void addUpdateable(Updateable u){
        updateables.add(u);
    }
}
