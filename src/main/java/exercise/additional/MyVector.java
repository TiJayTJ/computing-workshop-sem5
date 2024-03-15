package exercise.additional;

import lombok.Data;

@Data
public class MyVector {

    private double[] vectorData;
    private int size;

    public MyVector (){
        this.vectorData = new double[0];
        this.size = 0;
    }

    public MyVector (int size){
        this.vectorData = new double[size];
        this.size = size;
    }

    public MyVector (double[] array){
        this.vectorData = new double[array.length];
        this.size = array.length;
        System.arraycopy(array, 0, this.vectorData, 0, this.size);
    }
    /**
     * Создаёт нулевой вектор длины length.
     *
     * @param length длина возвращаемого вектора
     * @return нулевой вектор длины length
     */
    public static MyVector getZeroVector(int length) {
        double[] zeroVector = new double[length];
        for (int i = 0; i < length; i++) {
            zeroVector[i] = 0;
        }
        return new MyVector(zeroVector);
    }

    public MyVector sum(MyVector vector){
        MyVector result = new MyVector(this.size);
        for (int i = 0; i < this.size; i++) {
            result.set(i, this.get(i) + vector.get(i));
        }
        return result;
    }

    public MyVector diff(MyVector vector){
        MyVector result = new MyVector(this.size);
        for (int i = 0; i < this.size; i++) {
            result.set(i, this.get(i) - vector.get(i));
        }
        return result;
    }

    public MyVector mul(double a) {
        MyVector result = new MyVector(this.size);
        for (int i = 0; i < this.size; i++) {
            result.set(i, this.get(i) * a);
        }
        return result;
    }

    public MyVector div(double a) {
        MyVector result = new MyVector(this.size);
        for (int i = 0; i < this.size; i++) {
            result.set(i, this.get(i) / a);
        }
        return result;
    }

    public double get(int i){
        return vectorData[i];
    }

    public void set(int i, double element){
        vectorData[i] = element;
    }

    public MyVector copy(){
        return new MyVector(this.vectorData);
    }

    public void printVector(){
        for (int i = 0; i < size; i++) {
            System.out.print(vectorData[i] + " ");
        }
        System.out.println();
    }

    public double calcNorm() {
        double maximum = -1;
        for (int i = 0; i < size; i++) {
            if (Math.abs(vectorData[i]) > maximum){
                maximum = Math.abs(vectorData[i]);
            }
        }
        return maximum;
    }
}
