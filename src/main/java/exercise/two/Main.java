package exercise.two;

import static exercise.additional.Formatter.divide;

import exercise.additional.MyMatrix;
import exercise.additional.MyVector;
import exercise.additional.ThreeElements;
import exercise.additional.TwoElements;
import exercise.one.LinSystemSolver;

public class Main {

    static final double EPSILON = 0.001;

    public static void main(String[] args) {
        MyMatrix matrixA = new MyMatrix(new double[][]{
            {8.29381, 0.995516, -0.560617},
            {0.995516, 6.298198, 0.595772},
            {-0.560617, 0.595772, 4.997407}});
        MyVector vectorB = new MyVector(new double[]{0.766522, 3.844422, 5.239231});
        LinSystemSolver lssAb = new LinSystemSolver(matrixA, vectorB);
        MyVector trueSolution = lssAb.solveGaussLeadElem();
        LinSystemIterativeSolver lssHg = new LinSystemIterativeSolver(matrixA, vectorB);
        TwoElements<Integer, Double> pairIntDouble = lssHg.kForPrioriEval(EPSILON);
        int kForPriori = pairIntDouble.getOne();
        double priori = pairIntDouble.getTwo();
        ThreeElements<MyVector, Integer, Double> solutionKPriori = lssHg.solveSimpleIteration(EPSILON, false);
        int numbOfIterations = solutionKPriori.getTwo();
        double posteriori = solutionKPriori.getThee();
        ThreeElements<MyVector, Integer, Double> solutionKPrioriLust = lssHg.solveSimpleIteration(EPSILON, true);
        TwoElements<MyVector, Integer> vectorInt = lssHg.solveSeidel(EPSILON);

        divide();
        printTrueSolution(trueSolution);
        divide();
        printHNormAndK(lssHg, kForPriori);
        divide();
        printSimpIter(trueSolution, lssHg, solutionKPriori, numbOfIterations, posteriori);
        divide();
        printSimpIterLuster(trueSolution, solutionKPrioriLust);
        divide();
        printSeidel(lssHg, vectorInt);
        divide();
        printUpperRelaxation(lssHg);
        divide();
    }

    private static void printUpperRelaxation(LinSystemIterativeSolver lssHg) {
        MyVector solution;
        System.out.println("Решение методов верхней релаксации:");
        solution = lssHg.solveUpperRelaxation(EPSILON);
        solution.printVector();
    }

    private static void printSeidel(LinSystemIterativeSolver lssHg,
        TwoElements<MyVector, Integer> vectorInt) {
        MyVector solution;
        System.out.println("Решение системы методом Зейделя:");
        solution = vectorInt.getOne();
        solution.printVector();
        System.out.printf("%nСпектральный радиус матрицы перехода:\t%f%n", lssHg.calcSeidelMatrix().calcSpectralRadius());
    }

    private static void printSimpIterLuster(MyVector trueSolution,
        ThreeElements<MyVector, Integer, Double> solutionKPrioriLust) {
        MyVector solution;
        System.out.println("Решение методом простой итерации с уточнением по Люстернику: ");
        solution = solutionKPrioriLust.getOne();
        solution.printVector();
        System.out.printf("%nФактическая погрешность:\t%f%n", trueSolution.diff(solution).calcNorm());
    }

    private static void printSimpIter(MyVector trueSolution, LinSystemIterativeSolver lssHg,
        ThreeElements<MyVector, Integer, Double> solutionKPriori, int numbOfIterations,
        double posteriori) {
        System.out.println("Решение системы методом простой итерации:");
        MyVector solution = solutionKPriori.getOne();
        solution.printVector();
        System.out.printf("%nФактическое число итераций:\t%d%n", numbOfIterations);
        System.out.printf("Фактическая погрешность:\t%f%n", trueSolution.diff(solution).calcNorm());
        System.out.printf("Апостериорная оценка:\t%f%n", posteriori);
        System.out.printf("Априорная оценка:\t%f%n", lssHg.calcPrioriEval(numbOfIterations));
    }

    private static void printTrueSolution(MyVector trueSolution) {
        System.out.println("Решение системы Ax = b методом Гаусса с выбором главного элемента:");
        trueSolution.printVector();
    }

    private static void printHNormAndK(LinSystemIterativeSolver lssHg, int kForPriori) {
        System.out.printf("Норма матрицы Н:\t%f%n", lssHg.getMatrixH().calcNorm());
        System.out.printf("k при которой априорная оценка ||x - x_k|| < epsilon:\t%d%n", kForPriori);
    }
}
