package exercise.three_four;

import static additional.Formatter.divide;

import additional.algebra.MyMatrix;
import additional.algebra.MyVector;
import additional.multiple_return.ThreeElements;
import additional.multiple_return.TwoElements;

public class Main {

    static final double EPSILON6 = 1.E-6;
    static final double EPSILON3 = 1.E-3;

    public static void main(String[] args) {
        MyMatrix matrixA = new MyMatrix(new double[][]{
            {-1.48213, -0.05316, 1.08254},
            {-0.05316, 1.13958, 0.01617},
            {1.08254, 0.01617, -1.48271}});


        divide();

        printEigenValuesVectors(matrixA, EPSILON6);

        divide();

        printPowerLowMethod(matrixA, EPSILON3);

        divide();

        printScalarProductMethod(matrixA, EPSILON3);

        divide();

        printOppositeEigenValueVector(matrixA, EPSILON3);

        divide();

        printWielandtMethod(matrixA, 1, EPSILON3);

        divide();
    }

    private static void printWielandtMethod(MyMatrix matrixA, double approximateEigenValue, double epsilon) {
        TwoElements<Double, MyVector> eigenValueWielandt = matrixA.findEigenValueWielandt(epsilon, approximateEigenValue, false);
        System.out.printf("Уточнённое собственное число: %.20e%n", eigenValueWielandt.getFirst());
        System.out.println("Вектор уточнённого собственного числа: ");
        eigenValueWielandt.getSecond().printVector();

        eigenValueWielandt = matrixA.findEigenValueWielandt(epsilon, approximateEigenValue, true);
        System.out.printf("Уточнённое по Эйткену собственное число: %.20e%n", eigenValueWielandt.getFirst());
    }

    private static void printOppositeEigenValueVector(MyMatrix matrixA, double epsilon) {
        TwoElements<Double, MyVector> eigenValueOpposite = matrixA.findEigenValueOpposite(epsilon);
        System.out.printf("Противоположная граница спектра методом скалярных произведений: %.3e%n", eigenValueOpposite.getFirst());
        System.out.println("Вектор противоположной границы спектра: ");
        eigenValueOpposite.getSecond().printVector();
    }

    private static void printScalarProductMethod(MyMatrix matrixA, double epsilon) {
        ThreeElements<Double, MyVector, Integer> maxEigenValueVector = matrixA.findMaxEigenScalarProduct(epsilon);
        System.out.printf("Максимальное собственное число методом скалярных произведений: %.3e%n", maxEigenValueVector.getFirst());
        System.out.println("Вектор максимального собственного числа: ");
        maxEigenValueVector.getSecond().printVector();
        System.out.printf("Количество итераций: %d%n", maxEigenValueVector.getThird());
    }

    private static void printPowerLowMethod(MyMatrix matrixA, double epsilon) {
        ThreeElements<Double, MyVector, Integer> maxEigenValueVector = matrixA.findMaxEigenPowerLaw(epsilon);
        System.out.printf("Максимальное собственное число степенным методом: %.3e%n", maxEigenValueVector.getFirst());
        System.out.println("Вектор максимального собственного числа: ");
        maxEigenValueVector.getSecond().printVector();
        System.out.printf("Количество итераций: %d%n", maxEigenValueVector.getThird());
    }

    private static void printEigenValuesVectors(MyMatrix matrixA, double epsilon) {
        ThreeElements<MyMatrix, MyMatrix, Integer> valVec = matrixA.findEigenValueVector(epsilon);
        MyMatrix eigenValues = valVec.getFirst();
        MyMatrix eigenVectors = valVec.getSecond();
        System.out.println("Собственные значения: ");
        eigenValues.printMatrix();
        System.out.println("Собственные вектора: ");
        eigenVectors.printMatrix();
    }
}
