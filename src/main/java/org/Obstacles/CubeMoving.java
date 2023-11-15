package main.java.org.Obstacles;

import main.java.org.LinearAlgebruh.Vector3;
import main.java.org.Physics.AABB;
import main.java.org.Render.Drawables.Cube;

import java.awt.*;

public class CubeMoving extends Obstacle{

    private double timeGone;

    private final Vector3 basedPosition;
    private final Vector3 amplitude;
    private final float speed;

    public CubeMoving(String name, Vector3 pos, Vector3 scale, Color color,Vector3 amplitude, float speed){
        super();

        drawable=new Cube(color);
        drawable.setPosition(pos);
        drawable.setScale(scale);
        drawable.setName(name);

        aabb=new AABB(pos,scale,true,name);

        timeGone=0.0f;

        this.basedPosition=pos.copy();
        this.amplitude=amplitude.copy();
        this.speed=speed;
    }

    @Override
    public void update(double deltaTime) {
        timeGone+=speed*deltaTime;

        Vector3 pos=Vector3.sum(basedPosition,Vector3.multiplyWithScalar((float)Math.sin(timeGone),amplitude));

        drawable.setPosition(pos);
        aabb.setPosition(pos);
    }

    public void reset(){
        timeGone=0.0f;

        drawable.setPosition(basedPosition);
        aabb.setPosition(basedPosition);
    }
}