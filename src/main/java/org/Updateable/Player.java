package main.java.org.Updateable;

import main.java.org.AudioManagement.AudioManager;
import main.java.org.InputManagement.InputManager;
import main.java.org.LinearAlgebruh.Vector3;
import main.java.org.Physics.AABB;
import main.java.org.Physics.CollisionDetection;
import main.java.org.Render.Camera.Camera;
import main.java.org.Render.Drawables.Shadow;
import main.java.org.Screens.GameScreen;
import main.java.org.Settings.Settings;

import java.awt.*;

/**
 * Es ist die Entität, die von dem Spieler steuert werden wird
 */
public class Player implements Updateable{

    /**
     * Der Collider des Spielers
     */
    private AABB aabb;

    /**
     * Der Schatten des Spielers
     */
    private Shadow shadow;

    /**
     * Die maximale horizontale Geschwindigkeit, die von dem Spieler durch Spazieren erreicht werden kann
     */
    private static final float MAX_VELOCITY=4;
    /**
     * @hidden
     */
    private static final float MAX_VELOCITY_SQUARED=16;
    /**
     * @hidden
     */
    private boolean canJump=false;
    /**
     * @hidden
     */
    private boolean isSprinting=false;

    /**
     * @hidden
     */
    private final float ZOOM_SPEED=300.0f;
    /**
     * @hidden
     */
    private final float ZOOMED_FOV;
    /**
     * @hidden
     */
    private final float BASED_FOV;
    /**
     * Das aktuelle Sichtfeld der Kamera
     */
    private float currentFov;

    /**
     * Erzeugt eine neue Player-Instanz.
     * Die Collider und Schatten des Spielers werden auch hier erzeugt.
     */
    public Player(){
        aabb=new AABB(new Vector3(0,0,0),new Vector3(0.25f,0.9f, 0.25f), false,"Player");
        shadow=new Shadow(new Vector3(0,-0.88f,0),new Vector3(0.25f,1, 0.25f),new Color(0,0,0, Settings.shadowShown()?100:0));

        BASED_FOV=Settings.getFov();
        ZOOMED_FOV=Settings.getFov()/4;

        currentFov=BASED_FOV;
    }

    /**
     * Registriert den Collider des Spielerinstanzes zu einem Physiksystem
     * @param cd Das Physiksystem, zu dem der Collider hinzufügt wird
     */
    public void addToPhysics(CollisionDetection cd){
        cd.addAABB(aabb);
    }

    /**
     * Registriert den Schatten in einer Kamera.
     * @param cam Die Kamera, in der der Schatten registriert wird
     */
    public void addToCamera(Camera cam){
        cam.addDrawable(shadow);
    }


    /**
     * Überschreibt die update Funktion des Updateable Interfaces
     * Überprüft, ob der Spieler mit einem anderen Collider gestoßen ist. Falls ja:
     * -falls der andere Collider der Name "Sus" hat, dann wird der Spieler sterben
     * -falls der andere Collider der Name "Finish" hat und er ist unter dem Spieler, dann wird das Spiel enden
     * Sucht für Benutzereingaben
     * @param deltaTime die Zeit, die nach dem letzten Frame verging
     */
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

        zoomControl((float) deltaTime);

        RotateCamera(deltaTime);
        Move(deltaTime);
        shadow.setPosition(Vector3.sum(aabb.getPositionByReference(),new Vector3(0,-0.88f,0)));
        GameScreen.mainCamera.setPosition(Vector3.sum(aabb.getPositionByReference(),new Vector3(0,0.8f,0)));
    }

    /**
     * Überprüft, ob es neue Benutzereingaben gibt und stellt die Geschwindigkeit des Spielers den entsprechend
     * @param deltaTime die Zeit, die nach dem letzten Frame verging
     */
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
            AudioManager.playSound(AudioManager.SOUNDS.JUMP);
        }
        else
            velocity.set(1,velocity.get(1)-20.0f*(float)deltaTime);

        aabb.setVelocity(velocity);
        //System.out.println(pos);
    }

    /**
     * Überprüft, ob eine Mausbewegung passierte und stellt die Kameraeinrichtung dementsprechend
     * @param deltaTime die Zeit, die nach dem letzten Frame verging
     */
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

    /**
     * Verändert das Sichtfeld des Spielers entsprechend den Benutzereingaben
     * @param deltaTime die Zeit, die nach dem letzten Frame verging
     */
    private void zoomControl(float deltaTime){
        if(InputManager.C){
            currentFov-=deltaTime*ZOOM_SPEED;
            if(currentFov<ZOOMED_FOV)
                currentFov=ZOOMED_FOV;
        }
        else{
            currentFov+=deltaTime*ZOOM_SPEED;
            if(currentFov>BASED_FOV)
                currentFov=BASED_FOV;
        }

        GameScreen.mainCamera.setFOV(currentFov);
    }

    /**
     * Falls das Spiel zum Start zurückgestellt werden soll, wird das von dieser Funktion erledigt
     * respawn wird vor dem Start und nach Tasten des Respawnbuttones des Endbildschirmes gerufen
     */
    public void respawn(){
        aabb.setVelocity(new Vector3(0,0,0));
        aabb.setPosition(new Vector3(0,0,0));

        GameScreen.mainCamera.setPitch(0);
        GameScreen.mainCamera.setYaw(0);

        AudioManager.playSound(AudioManager.SOUNDS.SPAWN);
    }
}
