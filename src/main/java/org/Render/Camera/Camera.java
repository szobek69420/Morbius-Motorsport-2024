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

    /**
     * Die Bildschirmgröße
     */
    private final int GAME_WIDTH,GAME_HEIGHT;

    /**
     * Das Verhältnis zwischen den Bildschirmhöhen und -breiten
     */
    private final float aspectYX;
    /**
     * Das Verhältnis zwischen den Bildschirmbreiten und -höhen
     */
    private final float aspectXY;

    /**
     * Das vertikale Sichtfeld in Graden gegeben
     */
    private float fov;

    /**
     * Mindestens wie weit sollen die gezeichnete Objekte von der Kamera sein
     */
    private float nearPlane;
    private float nearPlaneSquared;

    /**
     * Die Breite der Projektionebene mit der Hilfe von fov berechnet
     */
    private float nearPlaneWidth;
    /**
     * Die Höhe der Projektionebene mit der Hilfe von fov berechnet
     */
    private float nearPlaneHeight;
    private float onePerNearPlaneWidth;
    private float onePerNearPlaneHeight;

    /**
     * Die Liste solchen Drawable-Instanzen, die von dieser Kamera gezeichnet werden sollen
     */
    private ArrayList<Drawable> drawables;
    /**
     * Die Position der Kamera
     */
    private Vector3 pos;

    /**
     * Die vertikale und horizontale Umdrehung der Kamera
     */
    private float pitch,yaw;
    /**
     * Die Basisvektoren des Sichtraums (View space)
     */
    private Vector3 left,up,forward;

    /**
     * Eine Matrix, mit denen die Vektoren in einem solchen Weltraum, dessen Mitte die Position der Kamera ist, in der Basis der Kamera konvertiert werden können
     * (also keine echte view matrix)
     */
    private Matrix3 viewMatrix;

    /**
     * Erzeugt eine neue Kamerainstanz
     * @param width Die Breite des Fensters
     * @param height Die Höhe des Fensters
     */
    public Camera(int width, int height){
        drawables=new ArrayList<>();

        pitch=-5;
        yaw=20;

        pos= new Vector3(-3,3,-10);

        GAME_WIDTH=width;
        GAME_HEIGHT=height;

        aspectXY=((float)GAME_WIDTH/GAME_HEIGHT);
        aspectYX=((float)GAME_HEIGHT/GAME_WIDTH);


        fov=60.0f;
        nearPlane=0.05f;
        calculateScreenDimensions();

        calculateOrientation();
    }

    /**
     * Zeichnet alle Drawable-Objekte, die zu dieser Kamera geordnet sein
     * @param g Die Graphics-Kontext, in der die Sachen gezeichnet werden sollen
     */
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

    /**
     * Nach den Werten der Umdrehung werden die Einheitsvektoren der Kamerabasis und die Viewmatrix kalkuliert
     */
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

    /**
     * Registrier ein Drawable-Objekt zu der Kamera
     * @param d
     */
    public void addDrawable(Drawable d){
        drawables.add(d);
    }

    /**
     * Lösch ein Drawable-Objekt von der Liste der Kamera
     * @param d Das zu löschene Objekt
     */
    public void removeDrawable(Drawable d){
        drawables.remove(d);
    }

    /**
     * Berechne die Projektionebene anhand dem fov
     */
    private void calculateScreenDimensions(){
        nearPlaneSquared=nearPlane*nearPlane;
        nearPlaneHeight=(float)Math.tan(0.0174532925*fov)*nearPlane;
        nearPlaneWidth=nearPlaneHeight*aspectXY;

        onePerNearPlaneWidth=1/nearPlaneWidth;
        onePerNearPlaneHeight=1/nearPlaneHeight;
    }

    //getter setters

    /**
     * Zurückgibt den kopierten Wert der Kameraposition
     * @return der Wert der Kameraposition
     */
    public Vector3 getPosition(){
        return pos.copy();
    }

    /**
     * Stellt die Position der Kamera ein
     * @param pos die neue Position
     */
    public void setPosition(Vector3 pos){
        this.pos=pos;
    }

    /**
     * Zurückgibt den kopierten Wert des dritten Basisvektors des Kameraraumes
     * @return der Wert des dritten Basisvektors des Kameraraumes
     */
    public Vector3 getForward(){
        return forward.copy();
    }

    /**
     * Zurückgibt den kopierten Wert des zweiten Basisvektors des Kameraraumes
     * @return der Wert des zweiten Basisvektors des Kameraraumes
     */
    public Vector3 getUp() {
        return up.copy();
    }

    /**
     * Zurückgibt den kopierten Wert des ersten Basisvektors des Kameraraumes
     * @return der Wert des ersten Basisvektors des Kameraraumes
     */
    public Vector3 getLeft() {
        return left.copy();
    }

    /**
     * Zurückgibt die vertikale Umdrehung der Kamera
     * @return die vertikale Umdrehung der Kamera
     */
    public float getPitch(){
        return pitch;
    }

    /**
     * Stellt die vertikale Umdrehung der Kamera ein
     * @param pitch die neue vertikale Umdrehung
     */
    public void setPitch(float pitch){
        this.pitch=pitch;
    }

    /**
     * Zurückgibt die horizontale Umdrehung der Kamera
     * @return die horizontale Umdrehung der Kamera
     */
    public float getYaw(){
        return yaw;
    }

    /**
     * Stellt die horizontale Umdrehung der Kamera ein
     * @param yaw die neue horizontale Umdrehung
     */
    public void setYaw(float yaw){
        this.yaw=yaw;
    }

    /**
     * Zurückgibt die Klipdistanz der Kamera
     * @return die Klipdistanz der Kamera
     */
    public float getNearPlane(){
        return nearPlane;
    }

    /**
     * Zurückgibt die Breite der Projektionebene
     * @return die Breite der Projektionebene
     */
    public float getNearPlaneWidth(){
        return nearPlaneWidth;
    }
    /**
     * Zurückgibt die Höhe der Projektionebene
     * @return die Höhe der Projektionebene
     */
    public float getNearPlaneHeight(){
        return nearPlaneHeight;
    }

    /**
     * Zurückgibt die Breite des Fensters
     * @return die Breite des Fensters
     */
    public int getScreenWidth(){
        return GAME_WIDTH;
    }

    /**
     * Zurückgibt die Höhe des Fensters
     * @return die Höhe des Fensters
     */
    public int getScreenHeight(){
        return GAME_HEIGHT;
    }

    /**
     * Zurückgibt das vertikale Sichtfeld
     * @return das vertikale Sichtfeld
     */
    public float getFOV(){
        return fov;
    }

    /**
     * Stellt den Wert des vertikalen Sichtfeldes ein
     * @param fov der neue Wert von fov
     */
    public void setFOV(float fov){
        this.fov=fov;
        calculateScreenDimensions();
    }
}
