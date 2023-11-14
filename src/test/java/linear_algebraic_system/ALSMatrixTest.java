package linear_algebraic_system;

import org.junit.jupiter.api.*;

/**
 * Unit test for ALSSolverTest
 */
class ALSMatrixTest {
  private final String PATH = "src/test/java/linear_algebraic_system/";

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void transposeMatrix() {
    double[][] matrix = MatrixParser.parseMatrix(PATH + "matrixA_transpose_4x3.txt");
    ALSMatrix transposedMatrix = new ALSMatrix(matrix).transposeMatrix();
    double[][] trueTransposedMatrix = MatrixParser.parseMatrix(PATH + "matrixA_3x4.txt");
    Assertions.assertArrayEquals(trueTransposedMatrix, transposedMatrix.getMatrix());
  }

  @Test
  void findCofactorMatrix() {
    double[][] matrix = MatrixParser.parseMatrix(PATH + "matrixC_5x5.txt");
    ALSMatrix cofactorMatrix = new ALSMatrix(matrix).findCofactorMatrix();
    double[][] trueCofactorMatrix = MatrixParser.parseMatrix(PATH + "matrixC_cof_5x5.txt");
    Assertions.assertArrayEquals(trueCofactorMatrix, cofactorMatrix.getMatrix());
  }

  @Test
  void getMinor() {
    double[][] matrix = MatrixParser.parseMatrix(PATH + "matrixC_5x5.txt");
    ALSMatrix matrixObj = new ALSMatrix(matrix);
    double[][] matrixMinor = matrixObj.findMinor(matrix, 2, 3);
    double[][] trueMatrix = MatrixParser.parseMatrix(PATH + "matrixC_minor_5x5.txt");
    Assertions.assertArrayEquals(trueMatrix, matrixMinor);
  }

  @Test
  void findMatrixDeterminant() {
    double[][] matrix = {
        {1, 2, 3, 4, 5},
        {9, 5, 7, 7, 0},
        {-3, 8, 10, 2, 1},
        {-6, 3, 6, 3, 2},
        {4, 2, 3, 1, 8}  };
    ALSMatrix matrixObj = new ALSMatrix(matrix);
    double matrixDeterminant = matrixObj.findMatrixDeterminant();

    double trueMatrixDeterminant = -5775;
    Assertions.assertEquals(trueMatrixDeterminant, matrixDeterminant);
  }

  @Test
  void findInverseMatrix() {
    double[][] matrix = {
        {1, 2, 3, 4, 5},
        {9, 5, 7, 7, 0},
        {-3, 8, 10, 2, 1},
        {-6, 3, 6, 3, 2},
        {4, 2, 3, 1, 8}  };
    ALSMatrix inverseMatrix = new ALSMatrix(matrix).findInverseMatrix();
    double[][] trueInverseMatrix = {
        { -0.1151515151515151514, 0.069090909090909090887, -0.028571428571428571413, -0.015064935064935064962, 0.079307359307359307337 },
        { 0.64848484848484848422, -0.1890909090909090907, 0.37142857142857142848, -0.56779220779220779194, -0.30978354978354978326 },
        { -0.62424242424242424265, 0.17454545454545454559, -0.2, 0.45818181818181818161, 0.30060606060606060588  },
        { 0.30909090909090909087, 0.01454545454545454548, -0.02857142857142857145, -0.033246753246753246717, -0.18129870129870129866 },
        { 0.09090909090909090909, -0.054545454545454545454, -0.0, -0.018181818181818181818, 0.072727272727272727272} };
    Assertions.assertArrayEquals(trueInverseMatrix, inverseMatrix.getMatrix());
  }

  @Test
  void multiplyByMatrix(){
    double[][] matrixA = MatrixParser.parseMatrix(PATH + "matrixA_transpose_4x3.txt");
    double[][] matrixB = MatrixParser.parseMatrix(PATH + "matrixB_3x2.txt");
    ALSMatrix matrixAObject = new ALSMatrix(matrixA);
    ALSMatrix matrixBObject = new ALSMatrix(matrixB);
    double[][] trueMultiplication = MatrixParser.parseMatrix(PATH + "matrixAB_4x2.txt");
    Assertions.assertArrayEquals(trueMultiplication, matrixAObject.multiplyByMatrix(matrixBObject).getMatrix());
  }

  @Test
  void parseMatrix(){
    double[][] matrixA = MatrixParser.parseMatrix(PATH + "matrixA_transpose_4x3.txt");
    Assertions.assertNotNull(matrixA);
    MatrixParser.printMatrix(matrixA);
  }
}