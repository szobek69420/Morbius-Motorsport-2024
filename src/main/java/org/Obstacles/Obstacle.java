package main.java.org.Obstacles;

import main.java.org.Physics.AABB;
import main.java.org.Physics.CollisionDetection;
import main.java.org.Render.Camera.Camera;
import main.java.org.Render.Drawables.Drawable;
import main.java.org.Updateable.Updateable;
import main.java.org.Updateable.UpdateableManager;

import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class Obstacle implements Updateable {
    protected AABB aabb;
    protected Drawable drawable;
    private ArrayList<CollisionDetection> cds;
    private ArrayList<Camera> cameras;

    protected Obstacle(){
        cds=new ArrayList<>();
        cameras=new ArrayList<>();
    }

    public final void addToCamera(Camera cam){
        cam.addDrawable(drawable);
        cameras.add(cam);
    }

    public final void addToPhysics(CollisionDetection cd){
        cd.addAABB(aabb);
        cds.add(cd);
    }

    protected void removeFromThings(){
        for(var cd : cds)
            cd.removeAABB(aabb);

        for(var cam:cameras){
            cam.removeDrawable(drawable);
        }
    }

    public void reset(){

    }
}
