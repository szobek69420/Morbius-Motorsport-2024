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
        getOnePerNearPlaneHeight=1/nearPlaneHeight;

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
            draw(g,drawables.get(indices[i]));
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

    private void draw(Graphics g, Drawable d){

        g.setColor(Color.red);

        Vector3 objectPosition=d.getPositionByReference();

        Vector3[] vertices=d.getVerticesByReference();
        int vertexCount=vertices.length;

        int[] indices=d.getIndicesByReference();
        int indexCount=indices.length;

        Matrix3 modelMatrix=d.getModelMatrixByReference();
        Vector3 scale=d.getScaleByReference();

        Vector3[] transformedVertices=new Vector3[vertexCount];

        //view space
        for(int i=0;i< vertexCount;i++){
            Vector3 temp= Vector3.difference(
                                        Vector3.sum(
                                                new Vector3(scale.get(0)*vertices[i].get(0),scale.get(1)*vertices[i].get(1),scale.get(2)*vertices[i].get(2)),
                                                objectPosition)
                                        ,pos);

            transformedVertices[i]=new Vector3(
                    Vector3.dotProduct(this.left,temp),
                    Vector3.dotProduct(this.up,temp),
                    Vector3.dotProduct(this.forward,temp)
            );
        }

        //backface cull
        int faceCount=indexCount/3;
        boolean isEverythingBehind=true;
        boolean[] isBehindView=new boolean[faceCount*3];
        int[] clipCount=new int[faceCount];
        for(int i=0;i<faceCount;i++){
            Vector3[] face=new Vector3[]{transformedVertices[indices[i*3]],transformedVertices[indices[i*3+1]],transformedVertices[indices[i*3+2]]};

            if(Vector3.dotProduct(
                    Vector3.avg(face,3),
                    Vector3.crossProduct(
                            Vector3.difference(face[2],face[1]),
                            Vector3.difference(face[0],face[1])
                    )) <0
            ){
                isBehindView[i*3]=false;
                isBehindView[i*3+1]=false;
                isBehindView[i*3+2]=false;
                clipCount[i]=0;


                if(face[0].get(2)<0){
                    isBehindView[i*3]=true;
                    clipCount[i]++;
                }
                if(face[1].get(2)<0){
                    isBehindView[i*3+1]=true;
                    clipCount[i]++;
                }
                if(face[2].get(2)<0){
                    isBehindView[i*3+2]=true;
                    clipCount[i]++;
                }

                //check if anything is visible
                if(clipCount[i]<3)
                    isEverythingBehind=false;
            }
            else {
                isBehindView[i*3]=true;
                isBehindView[i*3+1]=true;
                isBehindView[i*3+2]=true;
                clipCount[i]=3;
            }
        }

        if(isEverythingBehind)//object not visible -> yeet
            return;

        //screen positions
        int[] x=new int[vertexCount];
        int[] y=new int[vertexCount];

        float onePerNearPlaneHeight=1/nearPlaneHeight;
        for(int i=0;i< vertexCount;i++){
            float distanceRatio=(float)(nearPlane/Math.abs(transformedVertices[i].get(2)));

            x[i]=(int)((0.5f*GAME_WIDTH-(transformedVertices[i].get(0)*distanceRatio*onePerNearPlaneWidth)*0.5f*GAME_WIDTH));
            y[i]=(int)(0.5f*GAME_HEIGHT-(transformedVertices[i].get(1)*distanceRatio*onePerNearPlaneHeight)*0.5f*GAME_HEIGHT);
        }

        //draw
        Color[] colours=d.getFaceColorsByReference();
        Color orgColor=g.getColor();
        for(int i=0;i<faceCount;i++){

            switch (clipCount[i]){
                case 0:
                    g.setColor(colours[i]);
                    //System.out.println("guter: "+x[indices[3*i]]+" "+y[indices[3*i]]+" "+x[indices[3*i+1]]+" "+y[indices[3*i+1]]+" "+x[indices[3*i+2]]+" "+y[indices[3*i+2]]);
                    g.fillPolygon(new int[]{x[indices[3*i]],x[indices[3*i+1]],x[indices[3*i+2]]},new int[]{y[indices[3*i]],y[indices[3*i+1]],y[indices[3*i+2]]},3);
                    break;

                case 1:
                    int[] modX2=new int[4];
                    int[] modY2=new int[4];
                    Vector3[] jok2=new Vector3[2];
                    int index3=0;
                    Vector3 rossz=null;
                    for(int j=0;j<3;j++){
                        if(!isBehindView[i*3+j])
                        {
                            //System.out.println(x[indices[i*3+j]]+" "+y[indices[i*3+j]]);
                            modX2[index3]=x[indices[i*3+j]];
                            modY2[index3]=y[indices[i*3+j]];
                            jok2[index3]=transformedVertices[indices[i*3+j]];
                            index3++;
                        }
                        else
                            rossz=transformedVertices[indices[i*3+j]];
                    }

                    for(int j=0;j<2;j++){
                        //float arany=(nearPlane-transformedVertices[rossz].get(2))/(joZ2[j]-transformedVertices[rossz].get(2));
                        //modX2[2+j]=x[rossz]+(int)(arany*(modX2[j]-x[rossz]));
                        //modY2[2+j]=y[rossz]+(int)(arany*(modY2[j]-y[rossz]));
                        float arany=(nearPlane-rossz.get(2))/(jok2[j].get(2)-rossz.get(2));

                        modX2[2+j]=(int)((0.5f*GAME_WIDTH-((rossz.get(0)+(jok2[j].get(0)-rossz.get(0))*arany)*onePerNearPlaneWidth)*0.5f*GAME_WIDTH));
                        modY2[2+j]=(int)(0.5f*GAME_HEIGHT-((rossz.get(1)+(jok2[j].get(1)-rossz.get(1))*arany)*onePerNearPlaneHeight)*0.5f*GAME_HEIGHT);
                    }

                    g.setColor(colours[i]);
                    //g.fillPolygon(modX2,modY2,4);
                    g.fillPolygon(new int[]{modX2[0],modX2[1],modX2[2]},new int[]{modY2[0],modY2[1],modY2[2]},3);
                    g.fillPolygon(new int[]{modX2[1],modX2[2],modX2[3]},new int[]{modY2[1],modY2[2],modY2[3]},3);
                    break;

                case 2:
                    int[] modX=new int[3];
                    int[] modY=new int[3];
                    Vector3 jo=null;
                    for(int j=0;j<3;j++){
                        if(!isBehindView[i*3+j])
                        {
                            //System.out.println(x[indices[i*3+j]]+" "+y[indices[i*3+j]]);
                            modX[0]=x[indices[i*3+j]];
                            modY[0]=y[indices[i*3+j]];
                            jo=transformedVertices[indices[i*3+j]];
                            break;
                        }
                    }

                    int index2=1;
                    //System.out.println(joZ+" "+modX[0]+" "+modY[0]);
                    for(int j=0;j<3;j++){
                        if(!isBehindView[i*3+j])
                            continue;

                        //float arany=(nearPlane-transformedVertices[indices[i*3+j]].get(2))/(joZ-transformedVertices[indices[i*3+j]].get(2));
                        //modX[index2]=x[indices[i*3+j]]+(int)(arany*(modX[0]-x[indices[i*3+j]]));
                        //modY[index2]=y[indices[i*3+j]]+(int)(arany*(modY[0]-y[indices[i*3+j]]));
                        Vector3 tempRossz=transformedVertices[indices[i*3+j]];

                        float arany=(nearPlane-tempRossz.get(2))/(jo.get(2)-tempRossz.get(2));

                        modX[index2]=(int)((0.5f*GAME_WIDTH-((tempRossz.get(0)+(jo.get(0)-tempRossz.get(0))*arany)*onePerNearPlaneWidth)*0.5f*GAME_WIDTH));
                        modY[index2]=(int)(0.5f*GAME_HEIGHT-((tempRossz.get(1)+(jo.get(1)-tempRossz.get(1))*arany)*onePerNearPlaneHeight)*0.5f*GAME_HEIGHT);

                        index2++;
                    }

                    //System.out.println("skipped: "+x[indices[3*i]]+" "+y[indices[3*i]]+" "+x[indices[3*i+1]]+" "+y[indices[3*i+1]]+" "+x[indices[3*i+2]]+" "+y[indices[3*i+2]]);
                    g.setColor(colours[i]);
                    g.fillPolygon(modX,modY,3);
                    break;
            }
        }
        g.setColor(orgColor);
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
}
