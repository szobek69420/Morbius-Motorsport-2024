package main.java.org.Updateable;

import main.java.org.InputManagement.InputManager;
import main.java.org.LinearAlgebruh.Vector3;
import main.java.org.Physics.AABB;
import main.java.org.Render.RenderThread;

public class Player implements Updateable{
    private AABB aabb;

    public Player(){
        aabb=new AABB(new Vector3(0,0,-5),new Vector3(0.25f,0.9f, 0.25f), false);
        RenderThread.physics.addAABB(aabb);
    }

    @Override
    public void Update(double deltaTime){
        RotateCamera(deltaTime);
        Move(deltaTime);
        RenderThread.mainCamera.setPosition(Vector3.sum(aabb.getPositionByReference(),new Vector3(0,0.8f,0)));
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

        //forward*=10.0f*deltaTime;
        //left*=10.0f*deltaTime;
        //up*=10.0f*deltaTime;
        forward*=10;
        left*=10;
        up*=10;

        Vector3 forwardVec=RenderThread.mainCamera.getForward().copy();
        forwardVec.set(1,0);
        Vector3.normalize(forwardVec);

        Vector3 velocity=new Vector3(0,0,0);
        velocity=Vector3.sum(velocity,Vector3.multiplyWithScalar(forward, forwardVec));
        velocity=Vector3.sum(velocity,Vector3.multiplyWithScalar(left, RenderThread.mainCamera.getLeft()));
        //velocity=Vector3.sum(velocity,Vector3.multiplyWithScalar(up, Vector3.up));
        if(up>1)
            velocity=Vector3.sum(velocity, new Vector3(0,up,0));
        else
            velocity=Vector3.sum(velocity, new Vector3(0,(float)(aabb.getVelocityByReference().get(1)-10.0f*deltaTime),0));

        aabb.setVelocity(velocity);
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
