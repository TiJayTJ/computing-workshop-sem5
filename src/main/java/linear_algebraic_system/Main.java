package linear_algebraic_system;

public class Main {

  public static void main(String[] args) {
    double[][] matrixA = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    ALSMatrix solution = new ALSMatrix(matrixA);
    solution.printMatrix();
  }
}
