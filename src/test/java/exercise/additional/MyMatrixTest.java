package exercise.additional;

import additional.algebra.MyMatrix;
import additional.multiple_return.ThreeElements;
import org.junit.jupiter.api.Test;

class MyMatrixTest {
    final static double EPSILON = 1.E-6;

    @Test
    void findEigenValueVector() {
        MyMatrix matrixA = new MyMatrix(new double[][]{
            {-1.48213, -0.05316, 1.08254},
            {-0.05316, 1.13958, 0.01617},
            {1.08254, 0.01617, -1.48271}});

        ThreeElements<MyMatrix, MyMatrix, Integer> result = matrixA.findEigenValueVector(EPSILON);
        result.getFirst().printMatrix();
        result.getSecond().printMatrix();

        System.out.println(Math.signum(0));
    }
}