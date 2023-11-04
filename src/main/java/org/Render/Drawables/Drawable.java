package main.java.org.Render.Drawables;

import main.java.org.LinearAlgebruh.*;
import main.java.org.Physics.AABB;

import java.awt.*;

public abstract class Drawable {
    protected Vector3[] vertices;
    protected int[] indices;
    protected Color[] faceColors;
    protected Matrix3 modelMatrix;

    protected Vector3 pos;

    protected Vector3 scale;

    protected AABB aabb;


    protected void calculateModelMatrix(){
        modelMatrix=new Matrix3(new float[][]{{scale.get(0),0,0},{0,scale.get(1),0},{0,0,scale.get(2)}});
    }



    public final Vector3[] getVerticesByReference(){
        return vertices;
    }

    public final int[] getIndicesByReference(){
        return indices;
    }

    public final Color[] getFaceColorsByReference(){
        return faceColors;
    }

    public final Matrix3 getModelMatrixByReference(){
        return modelMatrix;
    }

    public final Vector3 getPositionByReference(){
        return pos;
    }
    public final void setPosition(Vector3 pos){
        this.pos=pos;
        aabb.setPosition(pos.copy());
    }
    public final void setScale(Vector3 scale){
        this.scale=scale;
        aabb.setScale(scale.copy());
        calculateModelMatrix();
    }

}
