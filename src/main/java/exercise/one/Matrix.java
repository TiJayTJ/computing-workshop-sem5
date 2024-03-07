package exercise.one;

import java.util.Arrays;

public class Matrix {

    double[][] matrixData;
    int rows;
    int columns;

    /**
     * Конструктор.
     * <p>Заносит значения в поля</p>
     *
     * @param matrix матрица
     */
    public Matrix(double[][] matrix) {
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
        double[] unitVector = getZeroVector(rows);
        double[] resultX;
        for (int i = 0; i < rows; i++) {
            unitVector[i] = 1;
            resultX = new LinearSystemsSolver(matrixData, unitVector).solve();
            for (int j = 0; j < rows; j++) {
                inverseMatrix[j][i] = resultX[j];
            }
            unitVector[i] = 0;
        }
        return inverseMatrix;
    }

    /**
     * @param length длина ожидаемого вектора
     * @return нулевой вектор длины length
     */
    private double[] getZeroVector(int length) {
        double[] zeroVector = new double[length];
        for (int i = 0; i < length; i++) {
            zeroVector[i] = 0;
        }
        return zeroVector;
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

    /**
     * Выводит вектор м в консоль.
     *
     * @param v вектор
     */
    public static void printVertor(double[] v) {
        System.out.println(Arrays.toString(v) + "\n");
    }
}
