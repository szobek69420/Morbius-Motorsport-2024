package main.java.org.Obstacles;

import main.java.org.LinearAlgebruh.Vector3;
import main.java.org.Physics.AABB;
import main.java.org.Render.Drawables.Cube;

import java.awt.*;

public class CubeWeak extends Obstacle{

    private boolean touched;
    private double timeLeft;
    private boolean dead;

    private final Vector3 basedPosition;

    public CubeWeak(String name, Vector3 pos, Vector3 scale, Color color){
        super();

        drawable=new Cube(color);
        drawable.setPosition(pos);
        drawable.setScale(scale);
        drawable.setName(name);

        aabb=new AABB(pos,scale,true,name);

        touched=false;
        dead=false;
        timeLeft=1.0f;

        basedPosition=pos.copy();
    }

    @Override
    public void update(double deltaTime) {
        if(dead)
            return;
        if(!touched&&aabb.getLastCollisionName().equals("Player")){
            touched=true;
        }

        if(touched){
            timeLeft-=deltaTime;

            Vector3 newPos=Vector3.sum(basedPosition,new Vector3(0.05f*(float)Math.sin(timeLeft*30.0f),0,0.05f*(float)Math.sin(-timeLeft*50.0f+1.2f)));
            drawable.setPosition(newPos);

            if(timeLeft<0)
            {
                drawable.setPosition(new Vector3(0,10000,0));
                aabb.setPosition(new Vector3(0,10000,0));
                dead=true;
            }
        }
    }

    public void reset(){
        timeLeft=1.0f;
        touched=false;
        dead=false;

        drawable.setPosition(basedPosition);
        aabb.setPosition(basedPosition);

        aabb.clearHistory();
    }
}
