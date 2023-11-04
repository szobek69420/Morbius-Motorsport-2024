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
    public void Update(float deltaTime){
        Move(deltaTime);
    }

    private void Move(float deltaTime){
        float forward=0.0f;
        float left=0.0f;

        if(InputManager.W)
            forward++;
        if(InputManager.S)
            forward--;

        if(InputManager.A)
            left++;
        if(InputManager.D)
            left--;

        //System.out.println(forward+" "+left);

        forward*=10.0f*deltaTime;
        left*=10.0f*deltaTime;

        pos=Vector3.sum(
                pos,
                Vector3.sum(
                        Vector3.multiplyWithScalar(forward, RenderThread.mainCamera.getForward()),
                        Vector3.multiplyWithScalar(left, RenderThread.mainCamera.getLeft())
                )
        );

        RenderThread.mainCamera.setPosition(pos.copy());
        System.out.println(pos);
    }
}
