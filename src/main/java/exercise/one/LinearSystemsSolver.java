package exercise.one;

import java.util.Arrays;

/**
 * Решает системы линейных уравнений по схеме гаусса единственного деления с выдачей диагностики в
 * случае слишком малого ведущего элемента. Принимает линейные уравнения вида AX = B
 *
 * @author Nurkhan Takhaviev <nurhantahaviev@gmail.com>
 */
public class LinearSystemsSolver {

    static final double EPSILON = 0.000001;
    final int n;
    final double[][] matrixA;
    double[] resultX;


    /**
     * Конструктор
     *
     * @param A матрица
     */
    public LinearSystemsSolver(double[][] A) {
        this.matrixA = A;
        this.n = matrixA.length;
        resultX = new double[n];
    }

    public void solve() {
        double[][] a = runForward();
        runReverse(a);
        printVector(resultX);
    }

    /**
     * Исходная система сводится к эквивалентной системе с верхней треугольной матрицей
     *
     * @return Треугольная матрица типа double[][]
     */
    private double[][] runForward() {
        double leadingElement;
        int indexToSwap;
        double[][] a = matrixA.clone();

        for (int k = 1; k <= n; k++) {
            leadingElement = a[k - 1][k - 1];
            indexToSwap = k - 1;
            // выбор наибольшего ведущего элемента
            for (int i = k; i <= n; i++) {
                if (Math.abs(a[i - 1][k - 1]) >= leadingElement) {
                    leadingElement = a[i - 1][k - 1];
                    indexToSwap = i;
                }
            }
            if (indexToSwap != k - 1) {
                double temp;
                for (int i = 1; i <= n + 1; i++) {
                    temp = a[indexToSwap - 1][i - 1];
                    a[indexToSwap - 1][i - 1] = a[k - 1][i - 1];
                    a[k - 1][i - 1] = temp;
                }
            }
            if (leadingElement < EPSILON) {
                System.out.println("Слишком малый ведущий элемент. Возможна большая погрешность");
            }
            // вычисление значений треугольной матрицы
            for (int j = k; j <= n + 1; j++) {
                a[k - 1][j - 1] = a[k - 1][j - 1] / leadingElement;
            }
            // обнуление значений под треугольной матрицей
            for (int i = k + 1; i <= n; i++) {
                leadingElement = a[i - 1][k - 1];
                for (int j = k; j <= n + 1; j++) {
                    a[i - 1][j - 1] = a[i - 1][j - 1] - a[k - 1][j - 1] * leadingElement;
                }
            }
        }
        return a;
    }

    /**
     * Вычисляется решение системы Ответом является вектор, который сохраняется в поле resultX типа
     * double[]
     *
     * @param a матрица системы линейного уравнения
     */
    private void runReverse(double[][] a) {
        for (int i = n; i >= 1; i--) {
            double tmp = 0;
            for (int j = i + 1; j <= n; j++) {
                tmp += a[i - 1][j - 1] * resultX[j - 1];
            }
            resultX[i - 1] = a[i - 1][n] - tmp;
        }
    }

    public void printMatrix(double[][] m) {
        Arrays.stream(m)
            .forEach(arr -> System.out.println(Arrays.toString(arr)));
    }

    public void printVector(double[] v) {
        System.out.println(Arrays.toString(v));
    }
}
