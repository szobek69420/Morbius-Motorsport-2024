package main.java.org.LinearAlgebruh;

public class Matrix3 implements Cloneable{
    private float[][] value;

    public Matrix3(){
        value=new float[3][3];
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(i==j)
                    continue;
                value[i][j]=0;
            }
            value[i][i]=1;
        }
    }

    public Matrix3(float[][] value){
        this.value=value;
    }

    public Matrix3(Vector3 vec0, Vector3 vec1, Vector3 vec2){
        value=new float[3][3];
        for (int i=0;i<3;i++){
            value[i][0]=vec0.get(i);
            value[i][1]=vec1.get(i);
            value[i][2]=vec2.get(i);
        }
    }

    @Override
    public Object clone(){
        return new Matrix3(this.getValues());
    }

    public float[][] getValues(){
        float[][] copy=new float[3][3];
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                copy[i][j]=value[i][j];
            }
        }

        return copy;
    }

    public float[][] getValuesByReference(){
        return value;
    }

    @Override
    public String toString(){
        return value[0][0]+" "+value[0][1]+" "+value[0][2]+"\n"+value[1][0]+" "+value[1][1]+" "+value[1][2]+"\n"+value[2][0]+" "+value[2][1]+" "+value[2][2];
    }

    //static
    public static float determinant(Matrix3 neo){
        float[][] values=neo.getValuesByReference();

        return (values[0][0]*(values[1][1]*values[2][2]-values[1][2]*values[2][1])
                - values[0][1]*(values[1][0]*values[2][2]-values[1][2]*values[2][0])
                + values[0][2]*(values[1][0]*values[2][1]-values[1][1]*values[2][0]));
    }

    public static void transpose(Matrix3 neo){
        float[][] values=neo.getValuesByReference();
        float temp;
        for(int i=0;i<3;i++){
            for(int j=0;j<i;j++) {
                temp=values[i][j];
                values[i][j]=values[j][i];
                values[j][i]=temp;
            }
        }
    }

}
