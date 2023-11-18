package main.java.org.Render.Drawables;

import main.java.org.LinearAlgebruh.*;
import main.java.org.Physics.AABB;
import main.java.org.Render.Camera.Camera;

import java.awt.*;

/**
 * Eine Basisklasse für alle Objekte, die von einer Kamera gezeichnet werden sollen.
 */
public abstract class Drawable {

    /**
     * Der Name des Drawables
     */
    private String name="amogus";

    /**
     * Die Knoten des Drawables
     */
    protected Vector3[] vertices;

    /**
     * Die Indizen des Drawables
     */
    protected int[] indices;

    /**
     * Die Seitenfarben des Drawables
     */
    protected Color[] faceColors;

    /**
     * Eine Matrix, mit deren die lokalen Knotenpositionen mit berücksichtigung der Größe des Objektes berechnet werden können.
     * (die ist nicht eine echte Modelmatrix, weil es die Knotenpositionen nicht zu Weltraumpositionen konvertiert)
     */
    protected Matrix3 modelMatrix;

    /**
     * Die Position des Objektes
     */
    protected Vector3 pos;
    /**
     * Die Größe des Objektes
     */
    protected Vector3 scale;


    /**
     * Berechnet die Modelmatrix anhand der Größe
     */
    protected void calculateModelMatrix(){
        modelMatrix=new Matrix3(new float[][]{{scale.get(0),0,0},{0,scale.get(1),0},{0,0,scale.get(2)}});
    }

    /**
     * Eine abstrakte Funktion, die dann wird aufgerufen, falls das Objekt gezeichnet werden soll.
     * @param g Die Graphics-Kontext, in der das Objekt gezeichnet werden soll.
     * @param cam Die Kamera, nach deren Orientation die Bildschirmpositionen berechnet werden sollen.
     */
    public abstract void render(Graphics g, Camera cam);


    /**
     * Gibt die Position des Objektes zurück. Der Wert der Position wird nicht in eine andere Vector3-Instanz kopiert.
     * @return die Position des Objektes
     */
    public final Vector3 getPositionByReference(){
        return pos;
    }

    /**
     * Stellt die Position des Objektes
     * @param pos die neue Position
     */
    public final void setPosition(Vector3 pos){
        this.pos=pos;
    }
    /**
     * Stellt die Größe des Objektes
     * @param scale die neue Größe
     */
    public final void setScale(Vector3 scale){
        this.scale=scale;
        calculateModelMatrix();
    }
    /**
     * Gibt die Größe des Objektes zurück. Der Wert der Größe wird nicht in eine andere Vector3-Instanz kopiert.
     * @return die Position des Objektes
     */
    public final Vector3 getScaleByReference(){
        return this.scale;
    }

    /**
     * Stellt den Name des Objektes ein
     * @param name der neue Name
     */
    public final void setName(String name){
        this.name=name;
    }

    /**
     * Gibt der Name des Objektes zurück
     * @return der Name des Objektes
     */
    public final String getName(){
        return this.name;
    }
}
