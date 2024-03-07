package exercise.one;

import static exercise.one.Matrix.printVertor;
import static exercise.one.Matrix.printMatrix;

public class Main {

    public static void main(String[] args) {
        double[][] matrixA = {{8.29381, 0.995516, -0.560617},
                              {0.995516, 6.298198, 0.595772},
                              {-0.560617, 0.595772, 4.997407}};
        double[] vectorB = { 0.766522, 3.844422, 5.239231};
        printVertor(new LinearSystemsSolver(matrixA, vectorB).solve());
        printMatrix(new Matrix(matrixA).findInverseMatrix());
    }
}
