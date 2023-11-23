package main.java.org.Obstacles;

import main.java.org.LinearAlgebruh.Vector3;
import main.java.org.Physics.AABB;
import main.java.org.Physics.CollisionDetection;
import main.java.org.Render.Camera.Camera;
import main.java.org.Render.Drawables.Cube;
import main.java.org.Render.Drawables.Drawable;

import java.awt.*;

public class CubeStatic extends Obstacle{

    /**
     * Erzeugt ein quaderformiges Hindernis, das unbewegbar ist
     * @param name der Name des Hindernisses
     * @param pos die Position des Hindernisses
     * @param scale die Größe des Hindernisses
     * @param color die Basisfarbe des Hindernisses
     */
    public CubeStatic(String name, Vector3 pos, Vector3 scale, Color color){
        super();

        drawable=new Cube(color);
        drawable.setPosition(pos);
        drawable.setScale(scale);
        drawable.setName(name);

        aabb=new AABB(pos,scale,true,name);
    }

    /**
     * Überschreibt die update-Funktion des Updateable-Interfaces, aber macht nichts
     * @param deltaTime die Zeit, die nach dem letzten UpdateableManager-Anruf verging
     */
    @Override
    public void update(double deltaTime) {
        //
    }
}
