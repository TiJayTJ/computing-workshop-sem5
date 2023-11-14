package linear_algebraic_system;

import java.util.Arrays;

/**
 * ALSMatrix - linear algebraic system matrix
 * Класс реализует матрицу и базовые функции для работы с ней
 *
 * @author TiJay
 */
public class ALSMatrix {
  double[][] matrix;
  int rowSize;
  int columnSize;

  public ALSMatrix(double[][] matrix){
    this.matrix = Arrays.copyOf(matrix, matrix.length);
    this.rowSize = matrix.length;
    this.columnSize = matrix[0].length;
  }

  public void printMatrix(){
    for (int i = 0; i < rowSize; i++) {
      System.out.print("|\t");
      for (int j = 0; j < columnSize; j++) {
        System.out.print(matrix[i][j] + "\t|\t");
      }
      System.out.println();
    }
  }

  public ALSMatrix transposeMatrix(){
    double[][] transposedArray = new double[columnSize][rowSize];
    for (int i = 0; i < columnSize; i++) {
      for (int j = 0; j < rowSize; j++) {
        transposedArray[i][j] = matrix[j][i];
      }
    }
    return new ALSMatrix(transposedArray);
  }

  public ALSMatrix findCofactorMatrix(){
    double[][] cofactorMatrix = new double[rowSize][columnSize];

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        cofactorMatrix[i][j] = findMatrixDeterminant(findMinor(matrix, i, j));
        if ((i + j) % 2 != 0){ cofactorMatrix[i][j] *= -1; }
      }
    }
    return new ALSMatrix(cofactorMatrix).transposeMatrix();
  }

  /**
   * @param matrix матрица, минор которой мы хотим найти
   * @param row индекс удаляемой строки
   * @param column индекс удаляемого столбца
   * @return минор элемента матрицы с индексами row и column
   */
  public double[][] findMinor(double[][] matrix, int row, int column){
    if (matrix.length != matrix[0].length){return new double[0][0];}    // checking the squareness of the matrix
    double[][] minor = new double[matrix.length-1][matrix.length-1];
    int skipRow = 0;
    int skipColumn = 0;
    for (int i = 0; i < matrix.length - 1; i++) {
      if (i == row){skipRow = 1;}
      for (int j = 0; j < matrix.length - 1; j++) {
        if (j == column){skipColumn = 1;}
        minor[i][j] = matrix[i + skipRow][j + skipColumn];
      }
      skipColumn = 0;
    }

    return minor;
  }

  public double findMatrixDeterminant(){
    if (rowSize == 0) {
      return 0;
    } else if (rowSize == 1) {
      return matrix[0][0];
    } else if (rowSize == 2) {
      return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
    }

    double determinant = 0;
    double summand;
    for (int i = 0; i < columnSize; i++) {
      summand = matrix[0][i] * findMatrixDeterminant(findMinor(matrix, 0, i));
      if (i % 2 != 0){ summand *= -1; }
      determinant += summand;
    }
    return determinant;
  }

  public double findMatrixDeterminant(double[][] matrix){
    if (matrix.length == 0) {
      return 0;
    } else if (matrix.length == 1) {
      return matrix[0][0];
    } else if (matrix.length == 2) {
      return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
    }

    double determinant = 0;
    double summand;
    for (int i = 0; i < matrix.length; i++) {
      summand = matrix[0][i] * findMatrixDeterminant(findMinor(matrix, 0, i));
      if (i % 2 != 0){ summand *= -1; }
      determinant += summand;
    }
    return determinant;
  }

  public ALSMatrix findInverseMatrix(){
    ALSMatrix cofactorMatrix = findCofactorMatrix();
    double matrixDeterminant = findMatrixDeterminant(matrix);
    double[][] inverseArray = new double[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        inverseArray[i][j] = cofactorMatrix.getMatrix()[i][j] / matrixDeterminant;
      }
    }
    return new ALSMatrix(inverseArray);
  }

  /**
   * @param inputMatrix матрица, на которую умножается исходная матрица
   *
   * @return результат умножения исходной матрицы на матрицу inputMatrix справа
   */
  public ALSMatrix multiplyByMatrix(ALSMatrix inputMatrix){
    double[][] result = new double[rowSize][inputMatrix.columnSize];
    double[][] matrix2 = inputMatrix.getMatrix();

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < inputMatrix.columnSize; j++) {
        result[i][j] = 0;
        for (int k = 0; k < columnSize; k++) {
          result[i][j] += matrix[i][k] * matrix2[k][j];
        }
      }
    }
    return new ALSMatrix(result);
  }

  public double[][] getMatrix(){
    return matrix;
  }

}
