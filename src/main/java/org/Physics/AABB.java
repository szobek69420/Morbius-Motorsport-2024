package main.java.org.Physics;

import main.java.org.LinearAlgebruh.Vector3;

import java.util.Vector;

public class AABB {

    public static enum CollisionType{
        NONE,TOP,BOTTOM,SIDE
    }

    private Vector3 position;
    private Vector3 scale;

    private Vector3 velocity;

    private CollisionType lastCollisionType;
    private long lastCollision;

    public final boolean isKinematic;

    public AABB(Vector3 position, Vector3 scale, boolean isKinematic){
        this.position=position;

        this.scale=scale;

        this.velocity=new Vector3(0,0,0);

        this.lastCollision=0;
        this.lastCollisionType=CollisionType.NONE;

        this.isKinematic=isKinematic;
    }

    public void move(double deltaTime){
        position=Vector3.sum(position,Vector3.multiplyWithScalar((float)deltaTime,velocity));
    }


    public static boolean resolveCollision(AABB nonKinematic, AABB kinematic){
        Vector3 deltaPos=Vector3.difference(nonKinematic.position,kinematic.position);
        Vector3 minDistance=new Vector3(nonKinematic.scale.get(0)+kinematic.scale.get(0),nonKinematic.scale.get(1)+kinematic.scale.get(1),nonKinematic.scale.get(2)+kinematic.scale.get(2));

        float[] delta=new float[3];
        boolean[] isIntersecting=new boolean[]{false,false, false};

        for(int i=0;i<3;i++){
            if(deltaPos.get(i)>0){
                delta[i]=minDistance.get(i)-deltaPos.get(i);
                if(delta[i]>0)
                    isIntersecting[i]=true;
            }
            else{
                delta[i]=-minDistance.get(i)-deltaPos.get(i);
                if(delta[i]<0)
                    isIntersecting[i]=true;
            }
        }

        if(isIntersecting[0]&&isIntersecting[1]&&isIntersecting[2]){

            float min=Math.abs(delta[0]);
            int index=0;
            if(min>Math.abs(delta[1])){
                min=Math.abs(delta[1]);
                index=1;
            }
            if(min>Math.abs(delta[2])){
                min=Math.abs(delta[2]);
                index=2;
            }

            nonKinematic.lastCollision=System.nanoTime();
            switch (index){
                case 1:
                    if(nonKinematic.getVelocityByReference().get(1)>0)
                        nonKinematic.lastCollisionType=CollisionType.TOP;
                    else
                        nonKinematic.lastCollisionType=CollisionType.BOTTOM;
                    break;

                default:
                    nonKinematic.lastCollisionType=CollisionType.SIDE;
                    break;
            }

            nonKinematic.position.set(index,nonKinematic.position.get(index)+delta[index]);
            if(delta[index]>0&&nonKinematic.velocity.get(index)<0)
                nonKinematic.velocity.set(index,0);
            else if(delta[index]<0&&nonKinematic.velocity.get(index)>0)
                nonKinematic.velocity.set(index,0);

            //System.out.println("collision");
            return true;
        }
        else
            return false;
    }

    public Vector3 getPositionByReference(){
        return this.position;
    }
    public void setPosition(Vector3 pos){
        this.position=pos;
    }

    public void setScale(Vector3 scale){
        this.scale=scale;
    }


    public Vector3 getVelocityByReference(){
        return velocity;
    }
    public void setVelocity(Vector3 velocity){
        this.velocity=velocity;
    }

    public CollisionType getLastCollisionType(){
        return lastCollisionType;
    }

    public long getLastCollision(){
        return lastCollision;
    }
}
