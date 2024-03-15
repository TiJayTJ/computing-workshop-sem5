package exercise.additional;

import exercise.one.LinSystemSolver;
import lombok.Data;
import Jama.Matrix;
import Jama.EigenvalueDecomposition;

@Data
public class MyMatrix {

    private double[][] matrixData;
    private int rows;
    private int columns;

    /**
     * Конструктор, создаёт матрицу размера rows на columns.
     *
     * @param rows    количество строк
     * @param columns количество столбцов
     */
    public MyMatrix(int rows, int columns) {
        this.matrixData = new double[rows][columns];
        this.rows = rows;
        this.columns = columns;
    }

    /**
     * Конструктор, создаёт матрицу на основе двойного массива.
     *
     * @param matrix двойной массив
     */
    public MyMatrix(double[][] matrix) {
        this.matrixData = matrix.clone();
        this.rows = matrix.length;
        this.columns = matrix[0].length;
    }

    /**
     * Находит обратную матрицу используя схему Гаусса с выбором главного элемента.
     *
     * @return обратная матрица
     */
    public MyMatrix findInverseMatrix() {
        MyMatrix inverseMatrix = new MyMatrix(rows, rows);
        MyVector unitVector = MyVector.getZeroVector(rows);
        MyVector resultX;
        for (int i = 0; i < rows; i++) {
            unitVector.set(i, 1);
            resultX = new LinSystemSolver(this, unitVector).solveGaussLeadElem();
            for (int j = 0; j < rows; j++) {
                inverseMatrix.set(j, i, resultX.get(j));
            }
            unitVector.set(i, 0);
        }
        return inverseMatrix;
    }

    /**
     * Делит матрицу на верхне-треугольную и нижне-треугольную по главной диагонали.
     * <p>Главная диагональ достаётся верхней части</p>
     * @return верхне и нижне диагональные матрицы
     */
    public TwoElements<MyMatrix, MyMatrix> divideToLeftRight(){
        double[][] hLeft = new double[rows][rows];
        double[][] hRight = new double[rows][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                if(i < j){
                    hLeft[i][j] = matrixData[i][j];
                    hRight[i][j] = 0;
                }
                else{
                    hLeft[i][j] = 0;
                    hRight[i][j] = matrixData[i][j];
                }
            }
        }

        return new TwoElements<>(new MyMatrix(hLeft), new MyMatrix(hRight));
    }

    /**
     * Создаёт единичную матрицу.
     *
     * @param size размер
     * @return единичная матрица
     */
    public static MyMatrix makeUnitMatrix(int size){
        double[][] result = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j){
                    result[i][j] = 1;
                }
                else {
                    result[i][j] = 0;
                }
            }
        }
        return new MyMatrix(result);
    }

    /**
     * Суммирует данную матрицу с матрицей matrix.
     *
     * @param matrix матрица для суммирования
     * @return сумма матриц
     */
    public MyMatrix sum(MyMatrix matrix) {
        double[][] sumMatrix = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                sumMatrix[i][j] = this.get(i, j) + matrix.get(i, j);
            }
        }
        return new MyMatrix(sumMatrix);
    }

    /**
     * Вычисляет разность между данной матрицей и матрицей matrix.
     *
     * @param matrix матрица для вычитания
     * @return разность матриц
     */
    public MyMatrix diff(MyMatrix matrix) {
        double[][] sumMatrix = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                sumMatrix[i][j] = this.get(i, j) - matrix.get(i, j);
            }
        }
        return new MyMatrix(sumMatrix);
    }

    /**
     * Умножает матрицу на скаляр.
     *
     * @param a скаляр
     * @return произведение матрицы на скаляр
     */
    public MyMatrix mul(double a){
        MyMatrix result = new MyMatrix(this.rows, this.columns);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                result.set(i, j, a * get(i, j));
            }
        }
        return result;
    }

    /**
     * Умножает матрицу на вектор справа.
     *
     * @param vector вектор
     * @return результат умножения
     */
    public MyVector mul(MyVector vector){
        MyVector result = new MyVector(this.rows);
        double sum;
        for (int i = 0; i < this.rows; i++) {
            sum = 0;
            for (int j = 0; j < this.columns; j++) {
                sum += vector.get(j) * get(i, j);
            }
            result.set(i, sum);
        }
        return result;
    }

    /**
     * Умножает данную матрицу на матрицу matrix справа.
     *
     * @param matrix матрица
     * @return результат умножения
     */
    public MyMatrix mul(MyMatrix matrix){
        double[][] result = new double[this.rows][matrix.getColumns()];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < matrix.getColumns(); j++) {
                result[i][j]= 0;
                for (int k = 0; k < this.columns; k++) {
                    result[i][j] += get(i, k) * matrix.get(k, j);
                }
            }
        }
        return new MyMatrix(result);
    }

    public double get(int i, int j) {
        return matrixData[i][j];
    }

    public void set(int i, int j, double element) {
        matrixData[i][j] = element;
    }

    /**
     * Создаёт копию данной матрицы.
     *
     * @return данная матрица
     */
    public MyMatrix copy() {
        double[][] matrixCopy = new double[this.rows][this.columns];
        for (int i = 0; i < this.rows; i++) {
            System.arraycopy(matrixData[i], 0, matrixCopy[i], 0, this.columns);
        }
        return new MyMatrix(matrixCopy);
    }


    /**
     * Выводит данную матрицу в консоль.
     */
    public void printMatrix() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < this.getColumns(); j++) {
                System.out.print(this.get(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Выводит матрицу m в консоль.
     *
     * @param m матрица
     */
    public static void printMatrix(MyMatrix m) {
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getColumns(); j++) {
                System.out.print(m.get(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Вычисляет бесконечную норму данной матрицы.
     *
     * @return норма
     */
    public double calcNorm() {
        double tmp;
        double maxTmp = -1;
        for (int i = 0; i < rows; i++) {
            tmp = 0;
            for (int j = 0; j < columns; j++) {
                tmp += Math.abs(matrixData[i][j]);
            }
            if (tmp > maxTmp) {
                maxTmp = tmp;
            }
        }
        return maxTmp;
    }

    /**
     * Вычисляет спектральный радиус данной матрицы.
     *
     * @return спектральный радиус
     */
    public double calcSpectralRadius() {
        double spectralRadius = -1;
        Matrix A = new Matrix(matrixData);

        // Вычисляем собственные числа
        EigenvalueDecomposition e = A.eig();
        Matrix D = e.getD();

        for (int i = 0; i < rows; i++) {
            if (Math.abs(D.get(i, i)) > spectralRadius){
                spectralRadius = Math.abs(D.get(i, i));
            }
        }

        return spectralRadius;
    }
}
