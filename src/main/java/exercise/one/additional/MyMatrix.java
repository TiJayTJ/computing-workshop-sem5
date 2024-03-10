package exercise.one.additional;

import exercise.one.LinearSystemsSolver;
import java.util.Arrays;
import lombok.Data;

@Data
public class MyMatrix {

    double[][] matrixData;
    int rows;
    int columns;

    /**
     * Конструктор.
     * <p>Заносит значения в поля</p>
     *
     * @param matrix матрица
     */
    public MyMatrix(double[][] matrix) {
        this.matrixData = matrix.clone();
        this.rows = matrix.length;
        this.columns = matrix[0].length;
    }

    /**
     * Находит обратную матрицу используя схему Гаусса с выбором главного элемента.
     *
     * @return обратная матрица
     */
    public double[][] findInverseMatrix() {
        double[][] inverseMatrix = new double[rows][rows];
        MyVector unitVector = MyVector.getVectorWithUnit(rows);
        MyVector resultX;
        for (int i = 0; i < rows; i++) {
            unitVector.set(i, 1);
            resultX = new LinearSystemsSolver(matrixData, unitVector).solveGaussLeadElem();
            for (int j = 0; j < rows; j++) {
                inverseMatrix[j][i] = resultX.get(j);
            }
            unitVector.set(i, 0);
        }
        return inverseMatrix;
    }

    public double get(int i, int j){
        return matrixData[i][j];
    }

    public void set(int i, int j, double element){
        matrixData[i][j] = element;
    }

    public MyMatrix copy(){
        return new MyMatrix(this.getMatrixData());
    }

    /**
     * Выводит матрицу m в консоль.
     *
     * @param m матрица
     */
    public static void printMatrix(double[][] m) {
        Arrays.stream(m)
            .forEach(arr -> System.out.println(Arrays.toString(arr)));
        System.out.println();
    }
}
