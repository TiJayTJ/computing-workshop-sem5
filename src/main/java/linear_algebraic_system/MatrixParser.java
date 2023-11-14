package linear_algebraic_system;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Служебный класс парсит текстовые входные данные из файла в массив, выводит массив
 *
 * @author TiJay
 */
public class MatrixParser {

  private MatrixParser(){}

  public static double[][] parseMatrix(String filePath) {
    Scanner scanner;
    try {
      scanner = new Scanner(new File(filePath));
      int rows = scanner.nextInt();
      int columns = scanner.nextInt();
      double[][] matrix = new double[rows][columns]; // коэффициенты перед x
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < columns; j++) {
          matrix[i][j] = scanner.nextInt();
        }
      }
      scanner.close();
      return matrix;
    } catch (FileNotFoundException e) {
      System.out.println("Файл не найден!");
      e.printStackTrace();
      return new double[0][0];
    }
  }

  public static void printMatrix(double[][] matrix){
    for (double[] doubles : matrix) {
      for (double element : doubles) {
        System.out.print(element + "\t|\t");
      }
      System.out.println();
    }
  }
}
