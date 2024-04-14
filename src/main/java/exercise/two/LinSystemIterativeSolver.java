package exercise.two;

import additional.algebra.MyMatrix;
import additional.algebra.MyVector;
import additional.multiple_return.ThreeElements;
import additional.multiple_return.TwoElements;
import lombok.Data;

/**
 * Решает системы линейных уравнений.
 * <p>Используемые методы:</p>
 * <p>- простой итерации:</p>
 * <p>- метод простой итерации с приближением по Люстернику и без</p>
 * <p>- метод Зейделя</p>
 * <p>- метод верхней релаксации</p>
 */
@Data
public class LinSystemIterativeSolver {

    final MyMatrix matrixH;
    final MyVector vectorG;
    final MyVector beginningX;

    /**
     * Конструктор.
     * <p>Преобразует систему Ax = b в систему x = Hx + g, заполняя поля matrixH, vectorG и вектор
     * нулевой итерации beginningX</p>
     *
     * @param matrixA квадратная матрица А
     * @param vectorB вектор b
     */
    public LinSystemIterativeSolver(MyMatrix matrixA, MyVector vectorB) {
        this.matrixH = calcMatrixH(matrixA);
        this.vectorG = calcVectorG(matrixA, vectorB);
        beginningX = MyVector.getZeroVector(vectorB.getSize());
    }

    /**
     * Вычисляет матрицу H для системы x = Hx + g.
     *
     * @param matrixA квадратная матрица
     * @return матрица H
     */
    private MyMatrix calcMatrixH(MyMatrix matrixA) {
        int n = matrixA.getRows();
        double[][] mat = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    mat[i][j] = 0;
                } else {
                    mat[i][j] = -matrixA.get(i, j) / matrixA.get(i, i);
                }
            }
        }
        return new MyMatrix(mat);
    }

    /**
     * Вычисляет вектор g для системы x = Hx + g
     *
     * @param matrixA квадратная матрица А
     * @param vectorB вектор b
     * @return вектор g
     */
    private MyVector calcVectorG(MyMatrix matrixA, MyVector vectorB) {
        int n = vectorB.getSize();
        double[] vec = new double[n];
        for (int i = 0; i < n; i++) {
            vec[i] = vectorB.get(i) / matrixA.get(i, i);
        }
        return new MyVector(vec);
    }

    /**
     * Вычисляет решение системы уравнений методом простой итерации.
     *
     * @param epsilon    точность приближения
     * @param withLuster true - приближать конечный ответ методом Люстерника, false - нет
     * @return решение системы
     */
    public ThreeElements<MyVector, Integer, Double> solveSimpleIteration(double epsilon,
        boolean withLuster) {
        int k = 0;
        double posteriori;
        MyVector prevX;
        MyVector currX = beginningX.copy();
        do {
            prevX = currX.copy();
            currX = matrixH.mul(prevX).sum(vectorG);
            posteriori = currX.diff(prevX).calcInfinityNorm();
            k += 1;
        } while (posteriori >= epsilon);

        if (withLuster) {
            currX = prevX.sum(
                currX.diff(prevX).div(1 - matrixH.calcSpectralRadius()));
        }

        return new ThreeElements<>(currX, k, posteriori);
    }

    /**
     * Вычисляет решение системы методом Зейделя.
     *
     * @param epsilon точность приближения
     * @return решение системы.
     */
    public TwoElements<MyVector, Integer> solveSeidel(double epsilon) {
        int k = 0;
        double posteriori;
        MyVector prevX;
        MyVector currX = beginningX.copy();
        TwoElements<MyMatrix, MyMatrix> pairHLHR = matrixH.divideToLeftRight();
        MyMatrix matrixHL = pairHLHR.getFirst();
        MyMatrix matrixHR = pairHLHR.getSecond();

        do {
            prevX = currX.copy();
            currX = matrixHL.mul(currX).sum(matrixHR.mul(prevX).sum(vectorG));
            posteriori = currX.diff(prevX).calcInfinityNorm();
            k += 1;
        } while (posteriori >= epsilon);

        return new TwoElements<>(currX, k);
    }

    /**
     * Находит матрицу перехода H_seidel.
     *
     * @return матрица перехода
     */
    public MyMatrix calcSeidelMatrix() {
        TwoElements<MyMatrix, MyMatrix> pairHLHR = matrixH.divideToLeftRight();
        MyMatrix matrixHL = pairHLHR.getFirst();
        MyMatrix matrixHR = pairHLHR.getSecond();

        return MyMatrix.makeUnitMatrix(matrixH.getRows()).diff(matrixHL).
            findInverseMatrix().mul(matrixHR);
    }

    /**
     * Находит номер итерации для которой априорная оценка будет меньше epsilon.
     *
     * @param epsilon точность приближения
     * @return номер итерации
     */
    public TwoElements<Integer, Double> kForPrioriEval(double epsilon) {
        int k = 0;
        double priori = calcPrioriEval(k);
        while (priori >= epsilon) {
            k += 1;
            priori = calcPrioriEval(k);
        }
        return new TwoElements<>(k, priori);
    }

    /**
     * Вычисляет априорную оценку решения.
     *
     * @param k номер итерации
     * @return априорное решение
     */
    public double calcPrioriEval(int k) {
        return Math.pow(matrixH.calcNorm(), k) * (beginningX.calcInfinityNorm() +
            vectorG.calcInfinityNorm() / (1 - matrixH.calcNorm()));
    }

    /**
     * Вычисляет решение системы уравнений методом верхней релаксации.
     *
     * @param epsilon точность приближения
     * @return решение системы
     */
    public MyVector solveUpperRelaxation(double epsilon) {
        int n = vectorG.getSize();
        double calcElement;
        double qOptimal = 2 / (1 + Math.sqrt(1 - Math.pow(matrixH.calcSpectralRadius(), 2)));
        MyVector prev;
        MyVector curr = beginningX.copy();
        do {
            prev = curr.copy();
            for (int i = 0; i < n; i++) {
                calcElement = 0;
                for (int j = 0; j < i; j++) {
                    calcElement += matrixH.get(i, j) * curr.get(j);
                }
                for (int j = i + 1; j < n; j++) {
                    calcElement += matrixH.get(i, j) * curr.get(j);
                }
                calcElement += -curr.get(i) + vectorG.get(i);
                calcElement = curr.get(i) + qOptimal * calcElement;
                curr.set(i, calcElement);
            }
        } while (curr.diff(prev).calcInfinityNorm() >= epsilon);

        return curr;
    }
}
