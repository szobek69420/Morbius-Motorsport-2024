package main.java.org.Obstacles;

import main.java.org.Physics.AABB;
import main.java.org.Physics.CollisionDetection;
import main.java.org.Render.Camera.Camera;
import main.java.org.Render.Drawables.Drawable;
import main.java.org.Updateable.Updateable;
import main.java.org.Updateable.UpdateableManager;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Elternklasse für die Hindernisse.
 * Implementiert das Updateable-Interface
 */
public abstract class Obstacle implements Updateable {
    /**
     * Collider des Hindernisses
     */
    protected AABB aabb;
    /**
     * Gezeichnetes Mesh des Hindernisses
     */
    protected Drawable drawable;
    /**
     * Physiksysteme, in denen das Hinderniss registriert ist
     */
    private ArrayList<CollisionDetection> cds;
    /**
     * Kameras, in denen das Hinderniss registriert ist
     */
    private ArrayList<Camera> cameras;

    /**
     * Erzeugt eine neue Obstacle-Instanz
     */
    protected Obstacle(){
        cds=new ArrayList<>();
        cameras=new ArrayList<>();
    }

    /**
     * Registriert das Hinderniss in eine Kamera
     * @param cam die Kamera, in die das Hindernis registriert wird
     */
    public final void addToCamera(Camera cam){
        cam.addDrawable(drawable);
        cameras.add(cam);
    }

    /**
     * Registriert das Hinderniss in eines Physiksystem
     * @param cd das Physiksystem, in das das Hindernis registriert wird
     */
    public final void addToPhysics(CollisionDetection cd){
        cd.addAABB(aabb);
        cds.add(cd);
    }

    /**
     * Löscht das Hinderniss von allen Kameras und Physiksystemen
     */
    protected final void removeFromThings(){
        for(var cd : cds)
            cd.removeAABB(aabb);

        for(var cam:cameras){
            cam.removeDrawable(drawable);
        }
    }

    /**
     * Was passiert, wenn die Stufe neu gestartet wird.
     * Optional überschreibbar
     */
    public void reset(){

    }
}
