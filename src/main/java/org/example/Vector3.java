package main.java.org.example;

public class Vector3 {


    private float[] value;

    public Vector3(){
        value=new float[3];
        value[0]=0;
        value[1]=0;
        value[2]=0;
    }

    public Vector3(float x, float y, float z){
        value=new float[3];
        value[0]=x;
        value[1]=y;
        value[2]=z;
    }

    public float get(int index){
        assert index<3&&index>=0;
        return value[index];
    }

    public void set(int index, float value){
        assert index<3&&index>=0;
        this.value[index]=value;
    }

    //static
    public static final Vector3 zero=new Vector3(0,0,0);
    public static final Vector3 right =new Vector3(-1, 0,0);
    public static final Vector3 up=new Vector3(0,1,0);
    public static final Vector3 forward=new Vector3(0,0,1);


    public float sqrMagnitude(Vector3 vec){
        return (vec.value[0]*vec.value[0]+vec.value[1]*vec.value[1]+vec.value[2]*value[2]);
    }

    public float magnitude(Vector3 vec){
        return (float)Math.sqrt((double)(vec.value[0]*vec.value[0]+vec.value[1]*vec.value[1]+vec.value[2]*value[2]));
    }
    public void normalize(Vector3 vec){
        float temp=magnitude(vec);
        vec.value[0]/=temp;
        vec.value[1]/=temp;
        vec.value[2]/=temp;
    }

    public float dotProduct(Vector3 a, Vector3 b){
        return (a.value[0]*b.value[0]+a.value[1]*b.value[1]+a.value[2]*b.value[2]);
    }

    public Vector3 crossProduct(Vector3 a, Vector3 b){
        return new Vector3(
                a.value[1]*b.value[2]-a.value[2]*b.value[1],
                a.value[2]*b.value[0]-a.value[0]*b.value[2],
                a.value[0]*b.value[1]-a.value[1]*b.value[0]);
    }
}
