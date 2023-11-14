package main.java.org.Obstacles;

import main.java.org.Physics.AABB;
import main.java.org.Physics.CollisionDetection;
import main.java.org.Render.Camera.Camera;
import main.java.org.Render.Drawables.Drawable;

public abstract class Obstacle {
    protected AABB aabb;
    protected Drawable drawable;

    public final void addToCamera(Camera cam){
        cam.addDrawable(drawable);
    }

    public final void addToPhysics(CollisionDetection cd){
        cd.addAABB(aabb);
    }
}
