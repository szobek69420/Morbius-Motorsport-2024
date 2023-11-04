package main.java.org.Render.Drawables;

import main.java.org.LinearAlgebruh.*;

import java.awt.*;

public abstract class Drawable {
    protected Vector3[] vertices;
    protected int[] indices;
    protected Matrix3 modelMatrix;

    protected Vector3 pos;

    protected Vector3 scale;


    protected void calculateModelMatrix(){
        modelMatrix=new Matrix3(new float[][]{{scale.get(0),0,0},{0,scale.get(1),0},{0,0,scale.get(2)}});
    }



    public final Vector3[] getVerticesByReference(){
        return vertices;
    }

    public final int[] getIndicesByReference(){
        return indices;
    }

    public final Matrix3 getModelMatrixByReference(){
        return modelMatrix;
    }

    public final Vector3 getPositionByReference(){
        return pos;
    }
}
