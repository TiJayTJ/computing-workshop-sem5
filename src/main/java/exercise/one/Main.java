package exercise.one;

import static additional.Formatter.divide;

import additional.algebra.MyMatrix;
import additional.algebra.MyVector;

public class Main {

    public static void main(String[] args) {
        MyMatrix matrixA = new MyMatrix(new double[][]{
            {8.29381, 0.995516, -0.560617},
            {0.995516, 6.298198, 0.595772},
            {-0.560617, 0.595772, 4.997407}});
        MyVector vectorB = new MyVector(new double[]{0.766522, 3.844422, 5.239231});

        MyMatrix matrixAx = new MyMatrix(new double[][]{
            {1, 2, 3},
            {4, 8, 1},
            {2, 3, 5}});
        MyVector vectorBx = new MyVector(new double[]{17, 24, 28});

        LinSystemSolver lssAb = new LinSystemSolver(matrixA, vectorB);
        MyMatrix matrixC = matrixAx.copy();
        matrixC.set(0, 0, Math.pow(10, -8) * matrixAx.get(0, 0));
        LinSystemSolver lssCb = new LinSystemSolver(matrixC, vectorBx);

        divide();
        printExtendedMatrix(lssAb);
        divide();
        printAGaussSolution(lssAb);
        divide();
        printALeadElemSolution(lssAb);
        divide();
        printCLeadElemSolution(lssCb);
        divide();
        printALUSolution(lssAb);
        divide();
//        printInverseA(lssAb);
//        divide();
    }

    private static void printInverseA(LinSystemSolver lssAb) {
        System.out.println("Матрица А:\n");
        lssAb.getSquareMatrix().printMatrix();
        System.out.println("Её обратная матрица:\n");
        lssAb.getSquareMatrix().findInverseMatrix().printMatrix();
        System.out.println("Результат умножения:");
        lssAb.getSquareMatrix().mul(lssAb.getSquareMatrix().findInverseMatrix()).printMatrix();
    }

    private static void printALUSolution(LinSystemSolver lssAb) {
        System.out.println("а это матрица А");  //
        lssAb.getSquareMatrix().printMatrix();  //
        System.out.println("а это расширенная матрица A");  //
        lssAb.getExtendedMatrix().printMatrix();  //

        System.out.println("Решение системы Ax = b с использованием LU-разложения:\n");
        MyVector solution = lssAb.solveLU();
        System.out.println("а это решение");    //
        solution.printVector();
        System.out.println("а это Вектор невязки:\n");
        lssAb.getDiscrepancy(solution).printVector();
    }

    private static void printCLeadElemSolution(LinSystemSolver lssCb) {
        System.out.println("Система Cx = b ");
        lssCb.getExtendedMatrix().printMatrix();
        System.out.println("\nРешение с использованием метода Гаусса:\n");
        MyVector solution = lssCb.solveLU();
        solution.printVector();
        System.out.println("\nВектор невязки:\n");
        lssCb.getDiscrepancy(solution).printVector();
        System.out.println("\nРешение с использованием метода Гаусса с выбором главного элемента:\n");
        solution = lssCb.solveGaussLeadElem();
        solution.printVector();
        System.out.println("\nВектор невязки:\n");
        lssCb.getDiscrepancy(solution).printVector();
    }

    private static void printALeadElemSolution(LinSystemSolver lssAb) {
        System.out.println("Решение системы Ax = b методом Гаусса с выбором главного элемента:\n");
        MyVector solution = lssAb.solveGaussLeadElem();
        solution.printVector();
        System.out.println("Вектор невязки:\n");
        lssAb.getDiscrepancy(solution).printVector();
    }

    private static void printAGaussSolution(LinSystemSolver lssAb) {
        System.out.println("Решение системы Ax = b методом Гаусса единственного деления:\n");
        MyVector solution = lssAb.solveGauss();
        solution.printVector();
        System.out.println("Вектор невязки:\n");
        lssAb.getDiscrepancy(solution).printVector();
    }

    private static void printExtendedMatrix(LinSystemSolver lssAb) {
        System.out.println("Расширенная матрица системы:\n");
        lssAb.getExtendedMatrix().printMatrix();
    }
}
