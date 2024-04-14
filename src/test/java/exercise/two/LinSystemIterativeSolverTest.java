package exercise.two;

import additional.algebra.MyMatrix;
import additional.algebra.MyVector;
import additional.multiple_return.ThreeElements;
import additional.multiple_return.TwoElements;
import exercise.one.LinSystemSolver;
import org.junit.Test;

public class LinSystemIterativeSolverTest {

    final static double EPSILON = 0.001;
    MyMatrix matrixA = new MyMatrix(new double[][]{
        {8.29381, 0.995516, -0.560617},
        {0.995516, 6.298198, 0.595772},
        {-0.560617, 0.595772, 4.997407}});
    MyVector vectorB = new MyVector(new double[]{0.766522, 3.844422, 5.239231});
    LinSystemSolver lssAb = new LinSystemSolver(matrixA, vectorB);
    MyVector trueSolution = lssAb.solveGaussLeadElem();

    @Test
    public void constructorTest(){
        LinSystemIterativeSolver lssHg = new LinSystemIterativeSolver(matrixA, vectorB);
        MyMatrix matrixH = lssHg.getMatrixH();
        MyVector vectorG = lssHg.getVectorG();
        trueSolution.printVector();
        matrixH.mul(trueSolution).sum(vectorG).printVector();
    }

    @Test
    public void spectralRadius(){
        System.out.println(matrixA.calcSpectralRadius());
    }

    @Test
    public void solveSimpleIteration() {
        LinSystemIterativeSolver lssHg = new LinSystemIterativeSolver(matrixA, vectorB);
        TwoElements<Integer, Double> stepAndPriori = lssHg.kForPrioriEval(EPSILON);

        ThreeElements<MyVector, Integer, Double> solutionStepPost = lssHg.solveSimpleIteration(EPSILON, false);
        MyVector solution = solutionStepPost.getFirst();
        int step = solutionStepPost.getSecond();
        double posteriori = solutionStepPost.getThird();
        ThreeElements<MyVector, Integer, Double> solutionStepPostLuster = lssHg.solveSimpleIteration(EPSILON, true);
        MyVector solutionLuster = solutionStepPostLuster.getFirst();

        System.out.println("Решение методом Гаусса с выбором ведущего элемента:");
        trueSolution.printVector();
        System.out.println("Решение методом простой итерации:");
        solution.printVector();
        System.out.printf("Номер шага:\t%d\n", step);
        System.out.printf("Номер шага k при априорной оценке:\t%d\n", stepAndPriori.getFirst());
        System.out.printf("Апостериорной оценка:\t%f\n", posteriori);
        System.out.printf("Априорная оценка:\t%f\n", stepAndPriori.getSecond());
        System.out.println("\nРешение методом простой итерации с уточнением по Люстернику:");
        solutionLuster.printVector();
        System.out.printf("Отклонение обычного решения:\t%f\n", solution.diff(trueSolution).calcInfinityNorm());
        System.out.printf("Отклонение решения с уточнением по Люстернику:\t%f\n", solutionLuster.diff(trueSolution).calcInfinityNorm());
    }

    @Test
    public void solveSeidel() {
        LinSystemIterativeSolver lssHg = new LinSystemIterativeSolver(matrixA, vectorB);
        TwoElements<MyVector, Integer> solutionStep = lssHg.solveSeidel(EPSILON);
        MyVector solution = solutionStep.getFirst();
        int k = solutionStep.getSecond();

        System.out.println("Решение методом Гаусса с выбором ведущего элемента:");
        trueSolution.printVector();
        System.out.println("Решение методом Зейделя:");
        solution.printVector();
        System.out.printf("Отклонение:\t%f\n", solution.diff(trueSolution).calcInfinityNorm());
    }

    @Test
    public void kForPrioriEval() {
        LinSystemIterativeSolver lssHg = new LinSystemIterativeSolver(matrixA, vectorB);
        System.out.println(lssHg.kForPrioriEval(0.001).getFirst());
    }


    @Test
    public void calcSeidelMatrix() {
        LinSystemIterativeSolver lssHg = new LinSystemIterativeSolver(matrixA, vectorB);
        MyMatrix matrixSeidel = lssHg.calcSeidelMatrix();

        System.out.println("Матрица Зейделя:");
        matrixSeidel.printMatrix();
        System.out.printf("Спектральный радиус матрицы Зейделя:\t%f\n", matrixSeidel.calcSpectralRadius());
    }

    @Test
    public void solveUpperRelaxation() {
        LinSystemIterativeSolver lssHg = new LinSystemIterativeSolver(matrixA, vectorB);
        MyVector solution = lssHg.solveUpperRelaxation(EPSILON);

        System.out.println("Решение методом Гаусса с выбором ведущего элемента:");
        trueSolution.printVector();
        System.out.println("Решение методом верхней релаксации:");
        solution.printVector();
    }
}