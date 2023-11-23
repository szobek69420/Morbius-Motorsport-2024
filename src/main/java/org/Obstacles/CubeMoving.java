package main.java.org.Obstacles;

import main.java.org.LinearAlgebruh.Vector3;
import main.java.org.Physics.AABB;
import main.java.org.Render.Drawables.Cube;

import java.awt.*;

public class CubeMoving extends Obstacle{

    /**
     * Die vergangene Zeit seit der Erzeugung
     */
    private double timeGone;
    /**
     * Die Anfangsposition des Hindernisses
     */
    private final Vector3 basedPosition;
    /**
     * Der maximale Abstand von Anfangsposition
     */
    private final Vector3 amplitude;
    /**
     * Die Bewegungsgeschwindigkeit
     */
    private final float speed;

    /**
     * Erzeugt ein quaderformiges Hindernis, das unbewegbar ist
     * @param name der Name des Hindernisses
     * @param pos die Position des Hindernisses
     * @param scale die Größe des Hindernisses
     * @param color die Basisfarbe des Hindernisses
     * @param amplitude Was ist die maximale Abweichung der Position von der Anfangsposition
     * @param speed Die Geschwindigkeit des Hindernisses
     */
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

    /**
     * Überschreibt die update-Funktion des Updateable-Interfaces.
     * Bewegt das Hindernis.
     * @param deltaTime die Zeit, die nach dem letzten UpdateableManager-Anruf verging
     */
    @Override
    public void update(double deltaTime) {
        timeGone+=speed*deltaTime;

        Vector3 pos=Vector3.sum(basedPosition,Vector3.multiplyWithScalar((float)Math.sin(timeGone),amplitude));

        drawable.setPosition(pos);
        aabb.setPosition(pos);
    }

    /**
     * Überschreibt die reset-Funktion der Obstacle-Klasse.
     * Wiederherstellt die Position des Hindernisses.
     */
    public void reset(){
        timeGone=0.0f;

        drawable.setPosition(basedPosition);
        aabb.setPosition(basedPosition);
    }
}