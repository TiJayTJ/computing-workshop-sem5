package exercise.one;

import static exercise.one.additional.MyMatrix.printMatrix;

import exercise.one.additional.MyMatrix;
import exercise.one.additional.MyVector;

public class Main {

    public static void main(String[] args) {
        double[][] matrixA = {{8.29381, 0.995516, -0.560617},
                              {0.995516, 6.298198, 0.595772},
                              {-0.560617, 0.595772, 4.997407}};
        MyVector vectorB = new MyVector(new double[]{0.766522, 3.844422, 5.239231});

        LinearSystemsSolver lss = new LinearSystemsSolver(matrixA, vectorB);
        System.out.println("Решение методом Гаусса:\n");
        lss.solveGauss().printVector();
        System.out.println("Решение методом Гаусса с выбором главного элемента:\n");
        lss.solveGaussLeadElem().printVector();
        System.out.println("Обратная матрица:\n");
        printMatrix(new MyMatrix(matrixA).findInverseMatrix());
        System.out.println("Решение системы с использованием LU-разложения:\n");
        lss.solveLU().printVector();
    }
}
