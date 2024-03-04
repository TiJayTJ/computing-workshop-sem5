package exercise1;

import java.util.Arrays;

/**
 * Решает системы линейных уравнений по схеме гаусса единственного деления с выдачей диагностики в
 * случае слишком малого ведущего элемента.
 * Принимает линейные уравнения вида AX = B
 *
 * @author Nurkhan Takhaviev <nurhantahaviev@gmail.com>
 */
public class LinearSystemsSolver {

    final long n;
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
    }

    public void solve(){
        Arrays.stream(startForwardRunning())
            .forEach(arr -> System.out.println(Arrays.toString(arr)));
    }

    private double[][] startForwardRunning(){
        double tmp;
        double[][] a = matrixA.clone();
        for (int k = 1; k <= n; k++) {
            tmp = a[k-1][k-1];
            for (int j = k; j <= n + 1; j++) {
                a[k-1][j-1] = a[k-1][j-1] / tmp;
            }
            for (int i = k + 1; i <= n; i++) {
                tmp = a[i-1][k-1];
                for (int j = k; j <= n + 1; j++) {
                    a[i-1][j-1] = a[i-1][j-1] - a[k-1][j-1] * tmp;
                }
            }
        }
        return a;
    }
}
