package main.java.org.Render;

import main.java.org.LinearAlgebruh.Matrix3;
import main.java.org.LinearAlgebruh.Vector3;
import main.java.org.Render.Drawables.Drawable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Camera extends JPanel {

    private final int GAME_WIDTH,GAME_HEIGHT;
    private final float aspectYX;
    private final float aspectXY;
    private float nearPlane;
    private final float nearPlaneWidth;
    private final float nearPlaneHeight;
    private final float onePerNearPlaneWidth;
    private final float getOnePerNearPlaneHeight;


    private ArrayList<Drawable> drawables;
    private Vector3 pos;

    private float pitch,yaw;
    private Vector3 left,up,forward;


    private Matrix3 viewMatrix;

    public Camera(int width, int height){
        drawables=new ArrayList<>();

        pitch=-5;
        yaw=20;

        pos= new Vector3(-2,3,-5);

        GAME_WIDTH=width;
        GAME_HEIGHT=height;

        aspectXY=((float)GAME_WIDTH/GAME_HEIGHT);
        aspectYX=((float)GAME_HEIGHT/GAME_WIDTH);


        nearPlane=0.1f;
        nearPlaneWidth=0.1f*aspectXY;
        nearPlaneHeight=0.1f;

        onePerNearPlaneWidth=1/nearPlaneWidth;
        getOnePerNearPlaneHeight=1/nearPlaneHeight;
    }

    protected void paintComponent(Graphics g){
        calculateOrientation();
        g.setColor(Color.red);

        for(int i=0;i<drawables.size();i++)
            draw(g,drawables.get(i));
    }

    private void calculateOrientation(){
        forward=new Vector3(
                (float)(Math.cos(0.0174532925*pitch)*Math.sin(0.0174532925*yaw)),
                (float)Math.sin(0.0174532925*pitch),
                (float)(Math.cos(0.0174532925*pitch)*Math.cos(0.0174532925*yaw))
        );

        left=Vector3.crossProduct(Vector3.up,forward);
        up=Vector3.crossProduct(forward,left);

        viewMatrix=new Matrix3(left,up,forward);
        Matrix3.transpose(viewMatrix);
    }

    private void draw(Graphics g, Drawable d){

        g.setColor(Color.red);

        Vector3 objectPosition=d.getPositionByReference();

        Vector3[] vertices=d.getVerticesByReference();
        int vertexCount=vertices.length;

        int[] indices=d.getIndicesByReference();
        int indexCount=indices.length;

        Matrix3 modelMatrix=d.getModelMatrixByReference();

        Vector3[] transformedVertices=new Vector3[vertexCount];

        //view space
        for(int i=0;i< vertexCount;i++){
            /*transformedVertices[i]=Vector3.multiplyWithMatrix(
                    viewMatrix,
                    Vector3.difference(
                            Vector3.sum(
                                    Vector3.multiplyWithMatrix(
                                            modelMatrix,
                                            vertices[i]),
                                    objectPosition)
                            ,pos));*/
            Vector3 temp= Vector3.difference(
                                        Vector3.sum(
                                                Vector3.multiplyWithMatrix(
                                                        modelMatrix,
                                                        vertices[i]),
                                                objectPosition)
                                        ,pos);

            transformedVertices[i]=new Vector3(
                    Vector3.dotProduct(left,temp),
                    Vector3.dotProduct(up,temp),
                    Vector3.dotProduct(forward,temp)
            );

            System.out.println(transformedVertices[i]);
        }

        //backface cull
        int faceCount=indexCount/3;
        boolean[] shouldRender=new boolean[faceCount];
        for(int i=0;i<faceCount;i++){
            if(Vector3.dotProduct(
                    this.forward,
                    Vector3.crossProduct(
                            Vector3.difference(transformedVertices[indices[i*3+2]],transformedVertices[indices[i*3+1]]),
                            Vector3.difference(transformedVertices[indices[i*3]],transformedVertices[indices[i*3+1]])
                    )) <0
            ){
                    shouldRender[i]=true;
            }
            else {
                shouldRender[i]=false;
            }
        }

        //screen positions
        int[] x=new int[vertexCount];
        int[] y=new int[vertexCount];

        float onePerNearPlaneHeight=1/nearPlaneHeight;
        for(int i=0;i< vertexCount;i++){
            float distanceRatio=nearPlane/transformedVertices[i].get(2);

            x[i]=(int)((0.5f*GAME_WIDTH-(transformedVertices[i].get(0)*distanceRatio*onePerNearPlaneHeight)*0.5f*GAME_WIDTH)*aspectYX);
            y[i]=(int)(0.5f*GAME_HEIGHT-(transformedVertices[i].get(1)*distanceRatio*onePerNearPlaneHeight)*0.5f*GAME_HEIGHT);
            System.out.println(x[i]+" "+y[i]);
        }

        //draw
        for(int i=0;i<faceCount;i++){
            if(!shouldRender[i])
                continue;

            g.drawPolygon(new int[]{x[indices[3*i]],x[indices[3*i+1]],x[indices[3*i+2]]},new int[]{y[indices[3*i]],y[indices[3*i+1]],y[indices[3*i+2]]},3);
        }
    }

    public void addDrawable(Drawable d){
        drawables.add(d);
    }
}
