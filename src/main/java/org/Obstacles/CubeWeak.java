package main.java.org.Obstacles;

import main.java.org.AudioManagement.AudioManager;
import main.java.org.LinearAlgebruh.Vector3;
import main.java.org.Physics.AABB;
import main.java.org.Render.Drawables.Cube;

import java.awt.*;

public class CubeWeak extends Obstacle{

    /**
     * Ist das Hindernis vom Spieler berüht?
     */
    private boolean touched;
    /**
     * Wie lang soll das Hinderniss nach Berühren halten, bevor es zerstört wird?
     */
    private double timeLeft;
    /**
     * Ist das Hindernis zerstört?
     */
    private boolean dead;

    /**
     * Die Anfangsposition des Hindernisses
     */
    private final Vector3 basedPosition;

    /**
     * Erzeugt ein quaderformiges Hindernis, das unbewegbar ist
     * @param name der Name des Hindernisses
     * @param pos die Position des Hindernisses
     * @param scale die Größe des Hindernisses
     * @param color die Basisfarbe des Hindernisses
     */
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

    /**
     * Überschreibt die update-Funktion des Updateable-Interfaces.
     * Sucht nach Kollisionen mit dem Spieler.
     * Detektiert, ob es fallen soll.
     * @param deltaTime die Zeit, die nach dem letzten UpdateableManager-Anruf verging
     */
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
                AudioManager.playSound(AudioManager.SOUNDS.BLOCK_BREAKS);
            }
        }
    }

    /**
     * Überschreibt die reset-Funktion der Obstacle-Klasse.
     * Wiederherstellt den Anfangslage des Hindernisses.
     */
    public void reset(){
        timeLeft=1.0f;
        touched=false;
        dead=false;

        drawable.setPosition(basedPosition);
        aabb.setPosition(basedPosition);

        aabb.clearHistory();
    }
}
