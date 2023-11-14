package main.java.org.Render.Drawables;

import main.java.org.LinearAlgebruh.Matrix3;
import main.java.org.LinearAlgebruh.Vector3;
import main.java.org.Physics.AABB;
import main.java.org.Render.Camera.Camera;
import main.java.org.Screens.GameScreen;

import java.awt.*;

public class Cube extends Drawable{

    public Cube(Color color){
        vertices=new Vector3[8];
        vertices[0]=new Vector3(1,-1,-1);
        vertices[1]=new Vector3(-1, -1, -1);
        vertices[2]=new Vector3(-1, -1,1);
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

        int r=color.getRed();
        int g=color.getGreen();
        int b=color.getBlue();

        faceColors=new Color[]{
                new Color(7*r/8,7*g/8,7*b/8),
                new Color(7*r/8,7*g/8,7*b/8),
                new Color(6*r/8,6*g/8,6*b/8),
                new Color(6*r/8,6*g/8,6*b/8),
                new Color(4*r/8,4*g/8,4*b/8),
                new Color(4*r/8,4*g/8,4*b/8),
                new Color(5*r/8,5*g/8,5*b/8),
                new Color(5*r/8,5*g/8,5*b/8),
                new Color(3*r/8,3*g/8,3*b/8),
                new Color(3*r/8,3*g/8,3*b/8),
                new Color(r,g,b),
                new Color(r,g,b)
        };

        scale=new Vector3(1,1,1);
        pos=new Vector3(0,0,0);

        calculateModelMatrix();

        this.setName("amogus");
    }

    @Override
    public void render(Graphics g, Camera cam){
        g.setColor(Color.red);

        Vector3 cameraPos=cam.getPosition();
        Vector3 forward=cam.getForward();
        Vector3 left=cam.getLeft();
        Vector3 up=cam.getUp();
        float nearPlane=cam.getNearPlane();
        float onePerNearPlaneHeight=1/cam.getNearPlaneHeight();
        float onePerNearPlaneWidth=1/cam.getNearPlaneWidth();
        int GAME_WIDTH=cam.getScreenWidth();
        int GAME_HEIGHT=cam.getScreenHeight();

        int vertexCount=vertices.length;
        int indexCount=indices.length;

        Vector3[] transformedVertices=new Vector3[vertexCount];

        //view space
        for(int i=0;i< vertexCount;i++){
            Vector3 temp= Vector3.difference(
                    Vector3.sum(
                            new Vector3(scale.get(0)*vertices[i].get(0),scale.get(1)*vertices[i].get(1),scale.get(2)*vertices[i].get(2)),
                            this.pos)
                    ,cameraPos);

            transformedVertices[i]=new Vector3(
                    Vector3.dotProduct(left,temp),
                    Vector3.dotProduct(up,temp),
                    Vector3.dotProduct(forward,temp)
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

        for(int i=0;i< vertexCount;i++){
            float distanceRatio=(float)(nearPlane/Math.abs(transformedVertices[i].get(2)));

            x[i]=(int)((0.5f*GAME_WIDTH-(transformedVertices[i].get(0)*distanceRatio*onePerNearPlaneWidth)*0.5f*GAME_WIDTH));
            y[i]=(int)(0.5f*GAME_HEIGHT-(transformedVertices[i].get(1)*distanceRatio*onePerNearPlaneHeight)*0.5f*GAME_HEIGHT);
        }

        //draw
        Color orgColor=g.getColor();
        for(int i=0;i<faceCount;i++){

            switch (clipCount[i]){
                case 0:
                    g.setColor(faceColors[i]);
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

                    g.setColor(faceColors[i]);
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
                    g.setColor(faceColors[i]);
                    g.fillPolygon(modX,modY,3);
                    break;
            }
        }
        g.setColor(orgColor);
    }

}
