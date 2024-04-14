package exercise.one;

import additional.algebra.MyMatrix;
import additional.algebra.MyVector;
import additional.multiple_return.TwoElements;

/**
 * Решает системы линейных уравнений вида AX = B.
 * <p> Методы, используемые для решения:</p>
 * <p>- по схеме гаусса единственного деления</p>
 * <p>- по схеме гаусса с выбором главного элемента</p>
 * <p>- с использованием LU-разложения</p>
 * <p>Также программа выдаёт диагностику в случае слишком малого ведущего элемента</p>
 */
public class LinSystemSolver {

    double epsilon = 1.E-3;
    private final MyMatrix extendedMatrix; // матрица n x n+1
    private final MyMatrix squareMatrix; //
    private final MyVector vectorB; //
    private final int rows;

    /**
     * Конструктор для системы Ax = B.
     *
     * @param squareMatrixA квадратная матрица
     * @param vectorB       вектор
     */
    public LinSystemSolver(MyMatrix squareMatrixA, MyVector vectorB) {
        this.squareMatrix = squareMatrixA.copy();
        this.vectorB = vectorB.copy();
        this.rows = squareMatrixA.getRows();
        this.extendedMatrix = makeExtendedMatrix();
    }

    public LinSystemSolver(MyMatrix squareMatrixA, MyVector vectorB, double epsilon) {
        this.squareMatrix = squareMatrixA.copy();
        this.vectorB = vectorB.copy();
        this.rows = squareMatrixA.getRows();
        this.extendedMatrix = makeExtendedMatrix();
        this.epsilon = epsilon;
    }

    /**
     * Расширяет матрицу А.
     *
     * @return расширенная матрица А
     */
    private MyMatrix makeExtendedMatrix() {
        double[][] extMatrix = new double[rows][rows + 1];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                extMatrix[i][j] = squareMatrix.get(i, j);
            }
        }
        for (int i = 0; i < rows; i++) {
            extMatrix[i][rows] = vectorB.get(i);
        }
        return new MyMatrix(extMatrix);
    }

    /**
     * Расширяет матрицу А.
     *
     * @param squareMatrixA квадратная матрица
     * @param vectorB       вектор
     * @return расширенная матрица А
     */
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

    /**
     * Разбивает расширенную матрицу на матрицу А и вектор b.
     *
     * @return пару: матрицу А, вектор b
     */
    private TwoElements<MyMatrix, MyVector> splitExtendedMatrix() {
        double[][] sqMatrix = new double[rows][rows];
        double[] vector = new double[rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                sqMatrix[i][j] = extendedMatrix.get(i, j);
            }
        }
        for (int i = 0; i < rows; i++) {
            vector[i] = extendedMatrix.get(i, rows);
        }

        return new TwoElements<>(new MyMatrix(sqMatrix), new MyVector(vector));
    }

    /**
     * Разбивает расширенную матрицу на матрицу А и вектор b.
     *
     * @param extendedMatrix расширенная матрица системы Ax = b
     * @return пару: матрицу А, вектор b
     */
    public static TwoElements<MyMatrix, MyVector> splitExtendedMatrix(MyMatrix extendedMatrix) {
        int rows = extendedMatrix.getRows();
        double[][] sqMatrix = new double[rows][rows];
        double[] vector = new double[rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                sqMatrix[i][j] = extendedMatrix.get(i, j);
            }
        }
        for (int i = 0; i < rows; i++) {
            vector[i] = extendedMatrix.get(i, rows);
        }

        return new TwoElements<>(new MyMatrix(sqMatrix), new MyVector(vector));
    }

    /**
     * Решает системы линейных уравнений по схеме Гаусса с выбором главного элемента.
     *
     * @return решение в виде вектора
     */
    public MyVector solveGaussLeadElem() {
        return runReverse(runForwardLeadElem(this.extendedMatrix.getMatrixData()));
    }

    /**
     * Решает системы линейных уравнений по схеме Гаусса с выбором главного элемента.
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

            if (leadingElement < epsilon) {
                System.out.println(
                    "( Слишком малый ведущий элемент. Возможна большая погрешность )");
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
        return runReverse(runForward(this.extendedMatrix.getMatrixData()));
    }

    /**
     * Решает систему линейных уравнений.
     *
     * @return решение в виде вектора
     */
    public MyVector solveGauss(MyMatrix extendedMatrix) {
        return runReverse(runForward(extendedMatrix.getMatrixData()));
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

            if (leadingElement < epsilon) {
                System.out.println(
                    "( Слишком малый ведущий элемент. Возможна большая погрешность )");
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
     *
     * @return решение системы
     */
    public MyVector solveLU() {
        TwoElements<MyMatrix, MyVector> duoUY = getUY(extendedMatrix);
        return solveGaussLeadElem(makeExtendedMatrix(duoUY.getFirst(), duoUY.getSecond()));
    }

    /**
     * Решает систему линейных уравнений, используя LU-разложение.
     *
     * @param extendedMatrix расширенная матрица системы
     * @return решение системы
     */
    public MyVector solveLU(MyMatrix extendedMatrix) {
        TwoElements<MyMatrix, MyVector> trioLUY = getUY(extendedMatrix);
        return solveGaussLeadElem(makeExtendedMatrix(trioLUY.getFirst(), trioLUY.getSecond()));
    }

    /**
     * Представляет матрицу А в виде L*U.
     *
     * @param matrixA - расширенная матрица А
     * @return квадратные матрицы L и U
     */
    private TwoElements<MyMatrix, MyVector> getUY(MyMatrix matrixA) {
        System.out.println("Testim");
        matrixA.printMatrix();
        MyMatrix matrixL = new MyMatrix(rows, rows);
        MyMatrix extendedMatrixU = new MyMatrix(rows, rows + 1);
        double temp;

        for (int i = 0; i < rows; i++) {
            for (int j = i; j < rows; j++) {
                temp = 0;
                for (int k = 0; k < i - 1; k++) {
                    temp += matrixL.get(j, k) * extendedMatrixU.get(k, i);
                }
                matrixL.set(j, i, matrixA.get(j, i) - temp);
            }
            for (int j = i; j <= rows; j++) {
                temp = 0;
                for (int k = 0; k < i - 1; k++) {
                    temp += matrixL.get(i, k) * extendedMatrixU.get(k, j);
                }
                extendedMatrixU.set(i, j, (matrixA.get(i, j) - temp) / matrixL.get(i, i));
            }
        }
        return splitExtendedMatrix(extendedMatrixU);
    }

    /**
     * Находит невязку по формуле b - Ax.
     *
     * @param x решение системы
     * @return невязка
     */
    public MyVector getDiscrepancy(MyVector x) {
        return this.vectorB.diff(this.squareMatrix.mul(x));
    }

    /**
     * @return расширенная матрица A
     */
    public final MyMatrix getExtendedMatrix() {
        return extendedMatrix;
    }

    /**
     * @return квадратная матрица A
     */
    public final MyMatrix getSquareMatrix() {
        return squareMatrix;
    }

    /**
     * @return размерность матрицы А
     */
    public final int getRows() {
        return rows;
    }
}
