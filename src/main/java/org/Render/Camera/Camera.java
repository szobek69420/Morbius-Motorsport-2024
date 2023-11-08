package main.java.org.Render.Camera;

import main.java.org.LinearAlgebruh.Matrix3;
import main.java.org.LinearAlgebruh.Vector3;
import main.java.org.Render.Drawables.Drawable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Camera {

    private final int GAME_WIDTH,GAME_HEIGHT;
    private final float aspectYX;
    private final float aspectXY;


    private float nearPlane;
    private float nearPlaneSquared;
    private final float nearPlaneWidth;
    private final float nearPlaneHeight;
    private final float onePerNearPlaneWidth;
    private final float onePerNearPlaneHeight;


    private ArrayList<Drawable> drawables;
    private Vector3 pos;

    private float pitch,yaw;
    private Vector3 left,up,forward;


    private Matrix3 viewMatrix;

    public Camera(int width, int height){
        drawables=new ArrayList<>();

        pitch=-5;
        yaw=20;

        pos= new Vector3(-3,3,-10);

        GAME_WIDTH=width;
        GAME_HEIGHT=height;

        aspectXY=((float)GAME_WIDTH/GAME_HEIGHT);
        aspectYX=((float)GAME_HEIGHT/GAME_WIDTH);


        nearPlane=0.05f;
        nearPlaneSquared=nearPlane*nearPlane;
        nearPlaneHeight=(float)Math.tan(0.0174532925*60.0)*nearPlane;
        nearPlaneWidth=nearPlaneHeight*aspectXY;

        onePerNearPlaneWidth=1/nearPlaneWidth;
        onePerNearPlaneHeight=1/nearPlaneHeight;

        calculateOrientation();
    }


    public void render(Graphics g){
        calculateOrientation();

        g.setColor(Color.red);

        int[] indices=new int[drawables.size()];
        float[] sqrDistances=new float[drawables.size()];

        //calculating distance
        for(int i=0;i<drawables.size();i++){
            indices[i]=i;
            Vector3 temp=Vector3.difference(drawables.get(i).getPositionByReference(),this.pos);

            sqrDistances[i]=Vector3.sqrMagnitude(temp);
        }

        //sorting
        for(int i=0;i<drawables.size();i++){
            for(int j=0;j<drawables.size()-1;j++){
                if(sqrDistances[j]<sqrDistances[j+1]){

                    float temp=sqrDistances[j];
                    sqrDistances[j]=sqrDistances[j+1];
                    sqrDistances[j+1]=temp;

                    int temp2=indices[j];
                    indices[j]=indices[j+1];
                    indices[j+1]=temp2;
                }
            }
        }

        //System.out.println(left+" "+up+" "+forward);
        for(int i=0;i<drawables.size();i++)
        {
            if(sqrDistances[i]>10000){
                //System.out.println(i+" "+sqrDistances[indices[i]]);
                continue;
            }

            //System.out.println("rendered: "+drawables.get(indices[i]).getName());
            drawables.get(indices[i]).render(g,this);
        }
    }

    private void calculateOrientation(){
        forward=new Vector3(
                (float)(Math.cos(0.0174532925*pitch)*Math.sin(0.0174532925*yaw)),
                (float)Math.sin(0.0174532925*pitch),
                (float)(Math.cos(0.0174532925*pitch)*Math.cos(0.0174532925*yaw))
        );

        left=Vector3.crossProduct(Vector3.up,forward);
        Vector3.normalize(left);
        up=Vector3.crossProduct(forward,left);
        Vector3.normalize(up);

        viewMatrix=new Matrix3(left,up,forward);
        Matrix3.transpose(viewMatrix);

    }


    public void addDrawable(Drawable d){
        drawables.add(d);
    }


    //getter setters
    public Vector3 getPosition(){
        return pos.copy();
    }
    public void setPosition(Vector3 pos){
        this.pos=pos;
    }

    public Vector3 getForward(){
        return forward.copy();
    }

    public Vector3 getUp() {
        return up.copy();
    }

    public Vector3 getLeft() {
        return left.copy();
    }

    public float getPitch(){
        return pitch;
    }

    public void setPitch(float pitch){
        this.pitch=pitch;
    }

    public float getYaw(){
        return yaw;
    }

    public void setYaw(float yaw){
        this.yaw=yaw;
    }

    public float getNearPlane(){
        return nearPlane;
    }

    public float getNearPlaneWidth(){
        return nearPlaneWidth;
    }

    public float getNearPlaneHeight(){
        return nearPlaneHeight;
    }

    public int getScreenWidth(){
        return GAME_WIDTH;
    }

    public int getScreenHeight(){
        return GAME_HEIGHT;
    }
}
