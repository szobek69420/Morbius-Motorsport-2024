package main.java.org.Render.Drawables;

import main.java.org.LinearAlgebruh.Matrix3;
import main.java.org.LinearAlgebruh.Vector3;

import java.awt.*;

public class Cube extends Drawable{

    public Cube(){
        vertices=new Vector3[8];
        vertices[0]=new Vector3(1,-1,-1);
        vertices[1]=new Vector3(-1, -1, -1);
        vertices[2]=new Vector3(-1, -1 ,1);
        vertices[3]=new Vector3(1, -1, 1);
        vertices[4]=new Vector3(1,1,-1);
        vertices[5]=new Vector3(-1, 1, -1);
        vertices[6]=new Vector3(-1, 1 ,1);
        vertices[7]=new Vector3(1, 1, 1);

        indices=new int[]{
                    0,1,5,
                    0,5,4,
                    1,2,6,
                    1,6,5,
                    2,3,7,
                    2,7,6,
                    3,0,4,
                    3,4,7,
                    0,2,1,
                    0,3,2,
                    4,5,6,
                    4,6,7
        };

        scale=new Vector3(1,1,1);
        pos=new Vector3(0,0,0);
        calculateModelMatrix();
    }

}
