package exercise.one;

import exercise.one.additional.MatrixPair;
import exercise.one.additional.MyMatrix;
import exercise.one.additional.MyVector;

/**
 * <p>Решает системы линейных уравнений по схеме гаусса единственного деления с выдачей диагностики
 * в случае слишком малого ведущего элемента</p>
 * <p>Принимает линейные уравнения вида AX = B</p>
 */
public class LinearSystemsSolver {

    static final double EPSILON = 0.000001;
    private final double[][] extendedMatrix; // матрица n x n+1
    private final double[][] squareMatrix; //
    private final MyVector vectorB; //
    private final int rows;

    /**
     * Конструктор для системы Ax = B.
     *
     * @param squareMatrixA квадратная матрица
     * @param vectorB       вектор
     */
    public LinearSystemsSolver(double[][] squareMatrixA, MyVector vectorB) {
        this.squareMatrix = squareMatrixA.clone();
        this.vectorB = vectorB.copy();
        this.rows = squareMatrixA.length;
        this.extendedMatrix = makeExtendedMatrix();
    }

    private double[][] makeExtendedMatrix() {
        double[][] extMatrix = new double[rows][rows + 1];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                extMatrix[i][j] = squareMatrix[i][j];
            }
        }
        for (int i = 0; i < rows; i++) {
            extMatrix[i][rows] = vectorB.get(i);
        }
        return extMatrix;
    }

    public static double[][] makeExtendedMatrix(MyMatrix squareMatrixA, MyVector vectorB) {
        int rows = squareMatrixA.getRows();
        double[][] extMatrix = new double[rows][rows + 1];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                extMatrix[i][j] = squareMatrixA.get(i, j);
            }
        }
        for (int i = 0; i < rows; i++) {
            extMatrix[i][rows] = vectorB.get(i);
        }
        return extMatrix;
    }

    private MatrixPair remakeExtendedMatrix() {
        double[][] sqMatrix = new double[rows][rows];
        double[] vector = new double[rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                sqMatrix[i][j] = extendedMatrix[i][j];
            }
        }
        for (int i = 0; i < rows; i++) {
            vector[i] = extendedMatrix[i][rows];
        }

        return new MatrixPair(new MyMatrix(sqMatrix), new MyVector(vector));
    }

    public static MatrixPair remakeExtendedMatrix(double[][] extendedMatrix) {
        int rows = extendedMatrix.length;
        double[][] sqMatrix = new double[rows][rows];
        double[] vector = new double[rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                sqMatrix[i][j] = extendedMatrix[i][j];
            }
        }
        for (int i = 0; i < rows; i++) {
            vector[i] = extendedMatrix[i][rows];
        }

        return new MatrixPair(new MyMatrix(sqMatrix), new MyVector(vector));
    }

    /**
     * Решает системы линейных уравнений по схеме Гаусса с выбором главного элемента
     *
     * @return решение в виде вектора
     */
    public MyVector solveGaussLeadElem() {
        return runReverse(runForwardLeadElem(this.extendedMatrix));
    }

    /**
     * Решает системы линейных уравнений по схеме Гаусса с выбором главного элемента
     *
     * @return решение в виде вектора
     */
    public MyVector solveGaussLeadElem(double[][] extendedMatrix) {
        return runReverse(runForwardLeadElem(extendedMatrix));
    }

    /**
     * Исходная система сводится к эквивалентной системе с верхней треугольной матрицей.
     *
     * @param matrix расширенная матрица
     * @return Треугольная матрица типа double[][]
     */
    private double[][] runForwardLeadElem(double[][] matrix) {
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
     * @param a расширенная матрица системы линейного уравнения
     * @return вектор-решение системы линейной системы уравнения
     */
    private MyVector runReverse(double[][] a) {
        double[] resultX = new double[rows];
        for (int i = rows; i >= 1; i--) {
            double tmp = 0;
            for (int j = i + 1; j <= rows; j++) {
                tmp += a[i - 1][j - 1] * resultX[j - 1];
            }
            resultX[i - 1] = a[i - 1][rows] - tmp;
        }
        return new MyVector(resultX);
    }

    /**
     * Решает систему линейных уравнений.
     *
     * @return решение в виде вектора
     */
    public MyVector solveGauss() {
        return runReverse(runForward(this.extendedMatrix));
    }

    /**
     * Решает систему линейных уравнений.
     *
     * @return решение в виде вектора
     */
    public MyVector solveGauss(double[][] extendedMatrix) {
        return runReverse(runForward(extendedMatrix));
    }

    /**
     * Исходная система сводится к эквивалентной системе с верхней треугольной матрицей.
     *
     * @param matrix расширенная матрица
     * @return Треугольная матрица типа double[][]
     */
    private double[][] runForward(double[][] matrix) {
        double leadingElement;
        double[][] a = matrix.clone();

        for (int k = 1; k <= rows; k++) {
            leadingElement = a[k - 1][k - 1];

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
     * Решает систему линейных уравнений, используя LU-разложение.
     * @return решение системы
     */
    public MyVector solveLU() {
        MatrixPair pairLU = getLUY(extendedMatrix);
        return solveGaussLeadElem(makeExtendedMatrix(pairLU.getMatrix2(), pairLU.getVector1()));
    }

    /**
     * Решает систему линейных уравнений, используя LU-разложение.
     * @param extendedMatrix расширенная матрица системы
     * @return решение системы
     */
    public MyVector solveLU(double[][] extendedMatrix) {
        MatrixPair pairLU = getLUY(extendedMatrix);
        return solveGaussLeadElem(makeExtendedMatrix(pairLU.getMatrix2(), pairLU.getVector1()));
    }

    /**
     * Представляет матрицу А в виде L*U.
     *
     * @param matrixA - расширенная матрица
     * @return квадратные матрицы L и U
     */
    private MatrixPair getLUY(double[][] matrixA) {
        double[][] matrixL = new double[rows][rows];
        double[][] extendedMatrixU = new double[rows][rows + 1];
        double temp;

        for (int i = 0; i < rows; i++) {
            for (int j = i; j < rows; j++) {
                temp = 0;
                for (int k = 0; k < i - 1; k++) {
                    temp += matrixL[j][k] * extendedMatrixU[k][i];
                }
                matrixL[j][i] = matrixA[j][i] - temp;
            }
            for (int j = i; j <= rows; j++) {
                temp = 0;
                for (int k = 0; k < i - 1; k++) {
                    temp += matrixL[i][k] * extendedMatrixU[k][j];
                }
                extendedMatrixU[i][j] = (matrixA[i][j] - temp) / matrixL[i][i];
            }
        }
        MatrixPair mp = remakeExtendedMatrix(extendedMatrixU);
        return new MatrixPair(new MyMatrix(matrixL), mp.getMatrix1(), mp.getVector1());
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
}
