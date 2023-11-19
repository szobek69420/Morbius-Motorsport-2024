package main.java.org.Render.Drawables;

import main.java.org.LinearAlgebruh.Vector3;

import java.awt.*;

/**
 * Schatten für den Spieler.
 * Vererbt von Drawable.
 */
public class Shadow extends Drawable{
    /**
     * Erzeugt eine neue Schatteninstanz
     * @param color Die Basisfarbe des Schattens
     */
    public Shadow(Vector3 pos,Vector3 scale, Color color){
        vertices=new Vector3[4];
        vertices[0]=new Vector3(1,0,0);
        vertices[1]=new Vector3(0, 0, -1);
        vertices[2]=new Vector3(-1, 0,0);
        vertices[3]=new Vector3(0, 0, 1);

        indices=new int[]{
                0,1,2,
                0,2,3
        };

        faceColors=new Color[]{
                color,
                color
        };

        this.scale=scale;
        this.pos=pos;

        calculateModelMatrix();

        this.setName("amogus");
    }
}
