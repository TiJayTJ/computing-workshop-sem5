package exercise.one.additional;

import lombok.Data;

@Data
public class MyVector {

    private double[] vectorData;
    private int size;

    public MyVector (){
        this.vectorData = new double[0];
        this.size = 0;
    }
    public MyVector (double[] array){
        this.vectorData = array.clone();
        this.size = array.length;
    }
    /**
     * Создаёт нулевой вектор длины length.
     *
     * @param length длина возвращаемого вектора
     * @return нулевой вектор длины length
     */
    public static MyVector getVectorWithUnit(int length) {
        double[] zeroVector = new double[length];
        for (int i = 0; i < length; i++) {
            zeroVector[i] = 0;
        }
        return new MyVector(zeroVector);
    }

    public double get(int i){
        return vectorData[i];
    }

    public void set(int i, double element){
        vectorData[i] = element;
    }

    public MyVector copy(){
        return new MyVector(this.getVectorData());
    }

    public void printVector(){
        for (int i = 0; i < size; i++) {
            System.out.print(vectorData[i] + " ");
        }
        System.out.println("\n");
    }
}
