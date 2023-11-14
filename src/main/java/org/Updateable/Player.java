package main.java.org.Updateable;

import main.java.org.InputManagement.InputManager;
import main.java.org.LinearAlgebruh.Vector3;
import main.java.org.Physics.AABB;
import main.java.org.Physics.CollisionDetection;
import main.java.org.Screens.GameScreen;

public class Player implements Updateable{
    private AABB aabb;

    private static final float MAX_VELOCITY=4;
    private static final float MAX_VELOCITY_SQUARED=16;

    private boolean canJump=false;
    private boolean isSprinting=false;

    public Player(){
        aabb=new AABB(new Vector3(0,0,0),new Vector3(0.25f,0.9f, 0.25f), false,"Player");
    }

    public void addToPhysics(CollisionDetection cd){
        cd.addAABB(aabb);
    }


    @Override
    public void update(double deltaTime){
        if((System.nanoTime()-aabb.getLastCollision())*0.000000001<deltaTime&&aabb.getLastCollisionType()== AABB.CollisionType.BOTTOM)
            canJump=true;
        if(aabb.getVelocityByReference().get(1)<-40.0f*deltaTime)
            canJump=false;

        if(aabb.getLastCollisionName().equals("Sus"))
            GameScreen.die();
        if(aabb.getLastCollisionType()== AABB.CollisionType.BOTTOM&&aabb.getLastCollisionName().equals("Finish"))
            GameScreen.finish();

        if(InputManager.CONTROL&&InputManager.W&&canJump)
            isSprinting=true;
        if(!InputManager.W)
            isSprinting=false;

        RotateCamera(deltaTime);
        Move(deltaTime);
        GameScreen.mainCamera.setPosition(Vector3.sum(aabb.getPositionByReference(),new Vector3(0,0.8f,0)));

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

        Vector3 forwardVec=GameScreen.mainCamera.getForward().copy();
        forwardVec.set(1,0);
        Vector3.normalize(forwardVec);

        Vector3 acceleration=Vector3.multiplyWithScalar(forward, forwardVec);
        acceleration=Vector3.sum(acceleration,Vector3.multiplyWithScalar(left, GameScreen.mainCamera.getLeft()));
        acceleration=Vector3.difference(acceleration,aabb.getVelocityByReference());
        acceleration.set(1,0);

        float accelMag=Vector3.magnitude(acceleration);

        if(accelMag>20.0f*deltaTime){
            Vector3.normalize(acceleration);
            acceleration=Vector3.multiplyWithScalar(20*(float)deltaTime,acceleration);
        }


        Vector3 velocity=Vector3.sum(aabb.getVelocityByReference(),acceleration);

        float maxVel=MAX_VELOCITY;
        float maxVelSqr=MAX_VELOCITY_SQUARED;
        if(isSprinting){
            maxVelSqr*=9;
            maxVel*=3;
        }

        float magnitude=velocity.get(0)*velocity.get(0)+velocity.get(2)*velocity.get(2);
        if(magnitude>maxVelSqr){

            magnitude=maxVel-(float)Math.sqrt(magnitude);
            if(magnitude<-30.0f*(float)deltaTime)
                magnitude=-30.0f*(float)deltaTime;

            velocity=Vector3.sum(velocity,Vector3.multiplyWithScalar(magnitude,new Vector3(velocity.get(0),0,velocity.get(2))));
        }

        if(up>1&&canJump){
            canJump=false;
            velocity.set(1,10);
        }
        else
            velocity.set(1,velocity.get(1)-20.0f*(float)deltaTime);

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

        up+=GameScreen.mainCamera.getPitch();
        left+=GameScreen.mainCamera.getYaw();

        if(up<-89.9f)
            up=-89.9f;
        else if(up>89.9f)
            up=89.9f;

        if(left<-360)
            left+=360;
        if(left>360)
            left-=360;

        GameScreen.mainCamera.setPitch(up);
        GameScreen.mainCamera.setYaw(left);
    }

    public void respawn(){
        aabb.setVelocity(new Vector3(0,0,0));
        aabb.setPosition(new Vector3(0,0,0));

        GameScreen.mainCamera.setPitch(0);
        GameScreen.mainCamera.setYaw(0);
    }

    public static float lerp(float a, float b, float i){
        return a+(b-a)*i;
    }
}
