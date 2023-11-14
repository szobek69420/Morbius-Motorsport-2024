package main.java.org.Obstacles;

import main.java.org.LinearAlgebruh.Vector3;
import main.java.org.Physics.AABB;
import main.java.org.Physics.CollisionDetection;
import main.java.org.Render.Camera.Camera;
import main.java.org.Render.Drawables.Cube;
import main.java.org.Render.Drawables.Drawable;

import java.awt.*;

public class CubeStatic extends Obstacle{

    public CubeStatic(String name, Vector3 pos, Vector3 scale, Color color){
        super();

        drawable=new Cube(color);
        drawable.setPosition(pos);
        drawable.setScale(scale);
        drawable.setName(name);

        aabb=new AABB(pos,scale,true,name);
    }

    @Override
    public void update(double deltaTime) {
        //
    }
}
