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
     *
     * @param rows    количество строк
     * @param columns количество столбцов
     */
    public MyMatrix(int rows, int columns) {
        this.matrixData = new double[rows][columns];
        this.rows = rows;
        this.columns = columns;
    }

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
    public MyMatrix findInverseMatrix() {
        MyMatrix inverseMatrix = new MyMatrix(rows, rows);
        MyVector unitVector = MyVector.getVectorWithUnit(rows);
        MyVector resultX;
        for (int i = 0; i < rows; i++) {
            unitVector.set(i, 1);
            resultX = new LinearSystemsSolver(this, unitVector).solveGaussLeadElem();
            for (int j = 0; j < rows; j++) {
                inverseMatrix.set(j, i, resultX.get(j));
            }
            unitVector.set(i, 0);
        }
        return inverseMatrix;
    }

    /**
     * Умножает матрицу на вектор справа.
     *
     * @return результат умножения
     */
    public MyVector mul(MyVector vector){
        MyVector result = new MyVector(this.rows);
        double sum;
        for (int i = 0; i < this.rows; i++) {
            sum = 0;
            for (int j = 0; j < this.columns; j++) {
                sum += vector.get(j) * get(i, j);
            }
            result.set(i, sum);
        }
        return result;
    }

    public double get(int i, int j) {
        return matrixData[i][j];
    }

    public void set(int i, int j, double element) {
        matrixData[i][j] = element;
    }

    public MyMatrix copy() {
        double[][] matrixCopy = new double[this.rows][this.columns];
        for (int i = 0; i < this.rows; i++) {
            System.arraycopy(this.getMatrixData()[i], 0, matrixCopy[i], 0, this.columns);
        }
        return new MyMatrix(matrixCopy);
    }


    /**
     * Выводит матрицу m в консоль.
     */
    public void printMatrix() {
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getColumns(); j++) {
                System.out.print(this.get(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Выводит матрицу m в консоль.
     *
     * @param m матрица
     */
    public static void printMatrix(MyMatrix m) {
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getColumns(); j++) {
                System.out.print(m.get(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
