package additional.algebra;

import com.jakewharton.fliptables.FlipTable;
import lombok.Data;

@Data
public class MyVector {
    private String[] vectorName;
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

    public MyVector (String[] array){
        this.vectorName = new String[array.length];
        this.size = array.length;
        System.arraycopy(array, 0, this.vectorName, 0, this.size);
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

    public double mulScalar(MyVector prev) {
        double sum = 0;
        for (int i = 0; i < size; i++) {
            sum += vectorData[i] * prev.get(i);
        }
        return sum;
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
        String[][] str = new String[size-1][1];
        String[] strFirst = new String[]{String.format("%.8e", vectorData[0])};
        if(size > 1){
            for (int i = 0; i < size - 1; i++) {
                str[i][0] = String.format("%.8e", vectorData[i+1]);
            }
        }
        System.out.print(FlipTable.of(strFirst, str));
    }

    public double calcInfinityNorm() {
        double maximum = -1;
        for (int i = 0; i < size; i++) {
            if (Math.abs(vectorData[i]) > maximum){
                maximum = Math.abs(vectorData[i]);
            }
        }
        return maximum;
    }

    public double calcTwoNorm(){
        double sum = 0;
        for (int i = 0; i < size; i++) {
            sum += Math.pow(vectorData[i], 2);
        }
        return Math.pow(sum, 0.5);
    }

    public void normalize(){
        double norm = this.calcTwoNorm();
        for (int i = 0; i < size; i++) {
            vectorData[i] = vectorData[i] / norm;
        }
    }
}
