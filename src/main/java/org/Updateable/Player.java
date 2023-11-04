package main.java.org.Updateable;

import main.java.org.InputManagement.InputManager;
import main.java.org.LinearAlgebruh.Vector3;
import main.java.org.Render.RenderThread;

public class Player implements Updateable{
    private Vector3 pos;

    public Player(){
        pos=new Vector3(0,0,-5);
    }

    @Override
    public void Update(double deltaTime){
        Move(deltaTime);
        RotateCamera(deltaTime);
    }

    private void Move(double deltaTime){
        float forward=0.0f;
        float left=0.0f;
        float up=0.0f;

        if(InputManager.W)
            forward++;
        if(InputManager.S)
            forward--;

        if(InputManager.A)
            left++;
        if(InputManager.D)
            left--;

        if(InputManager.SPACE)
            up++;
        if(InputManager.L_SHIT)
            up--;

        //System.out.println(forward+" "+left);

        forward*=10.0f*deltaTime;
        left*=10.0f*deltaTime;
        up*=10.0f*deltaTime;

        Vector3 forwardVec=RenderThread.mainCamera.getForward();
        forwardVec=new Vector3(forwardVec.get(0),0.0f, forwardVec.get(2));
        pos=Vector3.sum(pos,Vector3.multiplyWithScalar(forward, forwardVec));
        pos=Vector3.sum(pos,Vector3.multiplyWithScalar(left, RenderThread.mainCamera.getLeft()));
        pos=Vector3.sum(pos, Vector3.multiplyWithScalar(up,Vector3.up));

        RenderThread.mainCamera.setPosition(pos.copy());
        //System.out.println(pos);
    }

    private void RotateCamera(double deltaTime){
        //float left=RenderThread.mainCamera.getYaw();
        //float up=RenderThread.mainCamera.getPitch();
        float up=-InputManager.deltaMouseY;
        float left=-InputManager.deltaMouseX;

        up*=0.05f;
        left*=0.05f;

        //System.out.println(deltaTime+" "+up+" "+left);

        up+=RenderThread.mainCamera.getPitch();
        left+=RenderThread.mainCamera.getYaw();

        if(up<-88)
            up=-88;
        else if(up>88)
            up=88;

        if(left<-360)
            left+=360;
        if(left>360)
            left-=360;

        RenderThread.mainCamera.setPitch(up);
        RenderThread.mainCamera.setYaw(left);
    }
}
