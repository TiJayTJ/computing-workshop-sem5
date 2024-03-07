package exercise.one;

/**
 * Решает системы линейных уравнений.
 * <p>Решает системы линейных уравнений по схеме гаусса единственного деления с выдачей диагностики в
 *  случае слишком малого ведущего элемента</p>
 *  <p>Принимает линейные уравнения вида AX = 0</p>
 */
public class LinearSystemsSolver {

    static final double EPSILON = 0.000001;
    private final double[][] extendedMatrix; // матрица n x n+1
    private final double[][] squareMatrix; //
    private final int rows;
    private final int columns;

    /**
     * Конструктор для системы Ax = B.
     *
     * @param squareMatrixA матрица
     * @param vectorB вектор
     */
    public LinearSystemsSolver(double[][] squareMatrixA, double[] vectorB) {
        this.squareMatrix = squareMatrixA;
        this.rows = squareMatrixA.length;
        this.columns = rows + 1;
        this.extendedMatrix = makeExtendedMatrix(squareMatrixA, vectorB);
    }

    private double[][] makeExtendedMatrix(double[][] squareMatrixA, double[] vectorB) {
        double[][] extMatrix = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                extMatrix[i][j] = squareMatrixA[i][j];
            }
        }
        for (int i = 0; i < rows; i++) {
            extMatrix[i][columns - 1] = vectorB[i];
        }
        return extMatrix;
    }

    /**
     * Решает системы линейных уравнений.
     *
     * @return решение в виде вектора
     */
    public double[] solve() {
        return runReverse(runForward(this.extendedMatrix));
    }

    /**
     * Исходная система сводится к эквивалентной системе с верхней треугольной матрицей.
     *
     * @param matrix расширенная матрица
     * @return Треугольная матрица типа double[][]
     */
    private double[][] runForward(double[][] matrix) {
        double leadingElement;
        int indexToSwap;
        double[][] a = matrix.clone();

        for (int k = 1; k <= rows; k++) {
            leadingElement = a[k - 1][k - 1];
            indexToSwap = k - 1;
            // выбор наибольшего ведущего элемента
            for (int i = k; i <= rows; i++) {
                if (Math.abs(a[i - 1][k - 1]) >= leadingElement) {
                    leadingElement = a[i - 1][k - 1];
                    indexToSwap = i;
                }
            }
            if (indexToSwap != k - 1) {
                double temp;
                for (int i = 1; i <= rows + 1; i++) {
                    temp = a[indexToSwap - 1][i - 1];
                    a[indexToSwap - 1][i - 1] = a[k - 1][i - 1];
                    a[k - 1][i - 1] = temp;
                }
            }
            if (leadingElement < EPSILON) {
                System.out.println("Слишком малый ведущий элемент. Возможна большая погрешность");
            }
            // вычисление значений треугольной матрицы
            for (int j = k; j <= rows + 1; j++) {
                a[k - 1][j - 1] = a[k - 1][j - 1] / leadingElement;
            }
            // обнуление значений под треугольной матрицей
            for (int i = k + 1; i <= rows; i++) {
                leadingElement = a[i - 1][k - 1];
                for (int j = k; j <= rows + 1; j++) {
                    a[i - 1][j - 1] = a[i - 1][j - 1] - a[k - 1][j - 1] * leadingElement;
                }
            }
        }
        return a;
    }

    /**
     * Вычисляется решение системы.
     *
     * @param a матрица системы линейного уравнения
     * @return вектор-решение системы линейной системы уравнения
     */
    private double[] runReverse(double[][] a) {
        double[] resultX = new double[rows];
        for (int i = rows; i >= 1; i--) {
            double tmp = 0;
            for (int j = i + 1; j <= rows; j++) {
                tmp += a[i - 1][j - 1] * resultX[j - 1];
            }
            resultX[i - 1] = a[i - 1][rows] - tmp;
        }
        return resultX;
    }

    /**
     * @return возвращает расширенную матрицу
     */
    public final double[][] getExtendedMatrix() {
        return extendedMatrix;
    }
    /**
     * @return возвращает квадратную матрицу
     */
    public final double[][] getSquareMatrix() {
        return squareMatrix;
    }
    /**
     * @return возвращает количество строк
     */
    public final int getRows() {
        return rows;
    }
    /**
     * @return возвращает количество столбцов
     */
    public final int getColumns() {
        return columns;
    }
}
