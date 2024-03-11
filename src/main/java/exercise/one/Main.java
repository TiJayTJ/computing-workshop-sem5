package exercise.one;

import static exercise.one.additional.MyMatrix.printMatrix;

import exercise.one.additional.MyMatrix;
import exercise.one.additional.MyVector;

public class Main {

    public static void main(String[] args) {
        String DIVIDING_LINE = "----------------------------------------------------------------------------------------------------";
        MyMatrix matrixA = new MyMatrix(new double[][]{
            {8.29381, 0.995516, -0.560617},
            {0.995516, 6.298198, 0.595772},
            {-0.560617, 0.595772, 4.997407}});
        MyVector vectorB = new MyVector(new double[]{0.766522, 3.844422, 5.239231});
        LinearSystemsSolver lssAB = new LinearSystemsSolver(matrixA, vectorB);
        MyMatrix matrixC = matrixA.copy();
        matrixC.set(0, 0, Math.pow(10, -8) * matrixA.get(0, 0));
        LinearSystemsSolver lssAC = new LinearSystemsSolver(matrixC, vectorB);

        System.out.println(DIVIDING_LINE);

        System.out.println("Расширенная матрица системы:\n");
        lssAB.getExtendedMatrix().printMatrix();

        System.out.println(DIVIDING_LINE);

        System.out.println("Решение системы Ax = b методом Гаусса единственного деления:\n");
        MyVector solution = lssAB.solveGauss();
        solution.printVector();
        System.out.println("Вектор невязки:\n");
        lssAB.getDiscrepancy(solution).printVector();

        System.out.println(DIVIDING_LINE);

        System.out.println("Решение системы Ax = b методом Гаусса с выбором главного элемента:\n");
        solution = lssAB.solveGaussLeadElem();
        solution.printVector();
        System.out.println("Вектор невязки:\n");
        lssAB.getDiscrepancy(solution).printVector();

        System.out.println(DIVIDING_LINE);

        System.out.println("Решение системы Cx = b методом Гаусса с выбором главного элемента:\n");
        solution = lssAC.solveGaussLeadElem();
        solution.printVector();
        System.out.println("Вектор невязки:\n");
        lssAB.getDiscrepancy(solution).printVector();

        System.out.println(DIVIDING_LINE);

        System.out.println("Решение системы Ax = b с использованием LU-разложения:\n");
        solution = lssAB.solveLU();
        solution.printVector();
        System.out.println("Вектор невязки:\n");
        lssAB.getDiscrepancy(solution).printVector();

        System.out.println(DIVIDING_LINE);

        System.out.println("Матрица А:\n");
        lssAB.getSquareMatrix().printMatrix();
        System.out.println("Её обратная матрица:\n");
        lssAB.getSquareMatrix().findInverseMatrix().printMatrix();

        System.out.println(DIVIDING_LINE);
    }
}
