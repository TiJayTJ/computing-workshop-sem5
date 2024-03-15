package exercise.one;

import static exercise.additional.Formatter.divide;

import exercise.additional.MyMatrix;
import exercise.additional.MyVector;

public class Main {

    public static void main(String[] args) {
        MyMatrix matrixA = new MyMatrix(new double[][]{
            {8.29381, 0.995516, -0.560617},
            {0.995516, 6.298198, 0.595772},
            {-0.560617, 0.595772, 4.997407}});
        MyVector vectorB = new MyVector(new double[]{0.766522, 3.844422, 5.239231});
        LinSystemSolver lssAb = new LinSystemSolver(matrixA, vectorB);
        MyMatrix matrixC = matrixA.copy();
        matrixC.set(0, 0, Math.pow(10, -8) * matrixA.get(0, 0));
        LinSystemSolver lssAC = new LinSystemSolver(matrixC, vectorB);

        divide();

        System.out.println("Расширенная матрица системы:\n");
        lssAb.getExtendedMatrix().printMatrix();

        divide();

        System.out.println("Решение системы Ax = b методом Гаусса единственного деления:\n");
        MyVector solution = lssAb.solveGauss();
        solution.printVector();
        System.out.println("Вектор невязки:\n");
        lssAb.getDiscrepancy(solution).printVector();

        divide();

        System.out.println("Решение системы Ax = b методом Гаусса с выбором главного элемента:\n");
        solution = lssAb.solveGaussLeadElem();
        solution.printVector();
        System.out.println("Вектор невязки:\n");
        lssAb.getDiscrepancy(solution).printVector();

        divide();

        System.out.println("Решение системы Cx = b методом Гаусса с выбором главного элемента:\n");
        solution = lssAC.solveGaussLeadElem();
        solution.printVector();
        System.out.println("Вектор невязки:\n");
        lssAb.getDiscrepancy(solution).printVector();

        divide();

        System.out.println("Решение системы Ax = b с использованием LU-разложения:\n");
        solution = lssAb.solveLU();
        solution.printVector();
        System.out.println("Вектор невязки:\n");
        lssAb.getDiscrepancy(solution).printVector();

        divide();

        System.out.println("Матрица А:\n");
        lssAb.getSquareMatrix().printMatrix();
        System.out.println("Её обратная матрица:\n");
        lssAb.getSquareMatrix().findInverseMatrix().printMatrix();

        divide();
    }
}
