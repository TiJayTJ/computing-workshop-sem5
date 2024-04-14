package additional.algebra;

import additional.multiple_return.ThreeElements;
import additional.multiple_return.TwoElements;
import com.jakewharton.fliptables.FlipTable;
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
     * Конструктор, создаёт пустую матрицу.
     */
    public MyMatrix() {
        this.matrixData = null;
        this.rows = 0;
        this.columns = 0;
    }

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
        String[] strFirst = new String[columns];
        for (int i = 0; i < columns; i++) {
            strFirst[i] = String.format("%.8e", matrixData[0][i]);
        }

        String[][] str = new String[rows-1][columns];
        if(rows > 1){
            for (int i = 0; i < rows - 1; i++) {
                for (int j = 0; j < columns; j++) {
                    str[i][j] = String.format("%.8e", matrixData[i+1][j]);
                }
            }
        }
        System.out.println(FlipTable.of(strFirst, str));
    }

    /**
     * Выводит матрицу m в консоль.
     *
     * @param m матрица
     */
    public static void printMatrix(MyMatrix m) {
        int rows = m.getRows();
        int columns = m.getColumns();

        String[] strFirst = new String[columns];
        for (int i = 0; i < columns; i++) {
            strFirst[i] = String.format("%.8e", m.getMatrixData()[0][i]);
        }

        String[][] str = new String[rows-1][columns];
        if(rows > 1){
            for (int i = 0; i < rows - 1; i++) {
                for (int j = 0; j < columns; j++) {
                    str[i][j] = String.format("%.8e", m.getMatrixData()[i+1][j]);
                }
            }
        }
        System.out.println(FlipTable.of(strFirst, str));
    }

    public static void printTable(String[] h, MyMatrix m) {
        int rows = m.getRows();
        int columns = m.getColumns();

        String[] strFirst = new String[columns];
        for (int i = 0; i < columns; i++) {
            strFirst[i] = String.format("%s", h[i]);
        }

        String[][] str = new String[rows][columns];
        if(rows > 0){
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    str[i][j] = String.format("%.8e", m.getMatrixData()[i][j]);
                }
            }
        }
        System.out.println(FlipTable.of(strFirst, str));
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

    /**
     * Находит собственные значиния и вектора методом Якоби.
     *
     * @param epsilon точность приближения
     * @return 1) диагональная матрица с собственными числами, 2) матрица, столбцы которой являются собственными векторами
     */
    public ThreeElements<MyMatrix, MyMatrix, Integer> findEigenValueVector(double epsilon) {
        int k = 0;
        double[][] matrixA = matrixData;
        MyMatrix eigenVectors = MyMatrix.makeUnitMatrix(rows);
        MyVector eigenVector;
        TwoElements<Integer, Integer> IkJk;
        IkJk = findIkJk(matrixA);
        int ik = IkJk.getFirst();
        int jk = IkJk.getSecond();
        double d;
        double c;
        double s;

        while (Math.abs(matrixA[ik][jk]) >= epsilon){
            d = calcCoefD(ik, jk, matrixA);
            c = calcCoefC(ik, jk, matrixA, d);
            s = calcCoefS(ik, jk, matrixA, d);

            matrixA = calcAForEigen(matrixA, ik, jk, c, s);
            MyMatrix matrixV = buildTransitionMatrix(ik, jk, c, s);
            eigenVectors = eigenVectors.mul(matrixV);
            k += 1;

            IkJk = findIkJk(matrixA);
            ik = IkJk.getFirst();
            jk = IkJk.getSecond();
        }

        for (int i = 0; i < columns; i++) {
            eigenVector = eigenVectors.getColumn(i);
            eigenVector.normalize();
            eigenVectors.setColumn(i, eigenVector);
        }

        return new ThreeElements<>(new MyMatrix(matrixA), eigenVectors, k);
    }

    public void setColumn(int column, MyVector eigenVector) {
        for (int i = 0; i < rows; i++) {
            matrixData[i][column] = eigenVector.get(i);
        }
    }

    /**
     * Получить столбец матрицы.
     *
     * @param column индекс столбца
     * @return столбец матрицы
     */
    public MyVector getColumn(int column) {
        double[] vector = new double[rows];
        for (int i = 0; i < rows; i++) {
            vector[i] = matrixData[i][column];
        }
        return new MyVector(vector);
    }

    public void setRow(int row, MyVector eigenVector) {
        for (int i = 0; i < columns; i++) {
            matrixData[row][i] = eigenVector.get(i);
        }
    }

    /**
     * Получить строку матрицы.
     *
     * @param row индекс строки
     * @return строка матрицы
     */
    public MyVector getRow(int row) {
        double[] vector = new double[columns];
        for (int i = 0; i < columns; i++) {
            vector[i] = matrixData[row][i];
        }
        return new MyVector(vector);
    }

    /**
     * Строит матрицу перехода для вращения матрицы (применяется в поиске собственных чисел).
     *
     * @param ik индекс строки наибольшего по модулю элемента сверху диагонали
     * @param jk индекс столбца наибольшего по модулю элемента сверху диагонали
     * @param c косинус угла
     * @param s синус угла
     * @return матрицу перехода
     */
    private MyMatrix buildTransitionMatrix(int ik, int jk, double c, double s) {
        double[][] result = new double[rows][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                if(i == j){
                    if(i != ik && i != jk){
                        result[i][i] = 1;
                    }
                } else{
                    if(i != ik && i != jk && j != ik && j != jk){
                        result[i][j] = 0;
                    }
                }
            }
        }
        result[ik][ik] = c;
        result[jk][jk] = c;
        result[ik][jk] = -s;
        result[jk][ik] = s;
        return new MyMatrix(result);
    }

    private double[][] calcAForEigen(double[][] a, int ik, int jk, double c, double s) {
        double[][] result = new double[rows][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                if (i != ik && i != jk && j != ik && j != jk) {
                    result[i][j] = a[i][j];
                } else if (i != ik && i != jk) {
                    result[i][ik] = c * a[i][ik] + s * a[i][jk];
                    result[ik][i] = result[i][ik];
                    result[i][jk] = -s * a[i][ik] + c * a[i][jk];
                    result[jk][i] = result[i][jk];
                }
                else{
                    result[ik][ik] = c * c * a[ik][ik] + 2 * c * s * a[ik][jk] + s * s * a[jk][jk];
                    result[jk][jk] = s * s * a[ik][ik] - 2 * c * s * a[ik][jk] + c * c * a[jk][jk];
                    result[ik][jk] = 0;
                    result[jk][ik] = 0;
                }
            }
        }
        return result;
    }

    private double calcCoefD(int ik, int jk, double[][] a) {
        return Math.sqrt(Math.pow(a[ik][ik] - a[jk][jk], 2) + 4 * Math.pow(a[ik][jk], 2));
    }

    private double calcCoefC(int ik, int jk, double[][] a, double d) {
        return Math.sqrt(0.5 * (1 + Math.abs(a[ik][ik] - a[jk][jk]) / d));
    }

    private double calcCoefS(int ik, int jk, double[][] a, double d) {
        return Math.signum(a[ik][jk] * (a[ik][ik] - a[jk][jk])) * calcCoefC(ik, jk, a, -d);
    }

    private TwoElements<Integer, Integer> findIkJk(double[][] matrix) {
        int iMax = -1;
        int jMax = -1;
        double maxElem = -1;
        double tmp;
        int size = matrix.length;
        for (int i = 0; i < size; i++) {
            for (int j = i+1; j < size; j++) {
                tmp = Math.abs(matrix[i][j]);
                if (tmp > maxElem){
                    maxElem = tmp;
                    iMax = i;
                    jMax = j;
                }
            }
        }
        return new TwoElements<>(iMax, jMax);
    }

    /**
     * Находит максимальное собственное значение и вектор, соответствующий этому значению.
     * <p>Использует степенной метод</p>
     *
     * @param epsilon точность вычисления
     * @return максимальное собственное значение, вектор, соответствующий этому значению
     */
    public ThreeElements<Double, MyVector, Integer> findMaxEigenPowerLaw(double epsilon){
        int k = 0;
        double maxEigenValue = 0;
        double prev;
        ThreeElements<MyMatrix, MyMatrix, Integer> valVec = findEigenValueVector(1.e-6);
        MyMatrix eigenVectors = valVec.getSecond();
        MyVector vectorMaxEigenValue = MyVector.getZeroVector(rows);
        for (int i = 0; i < columns; i++) {
            vectorMaxEigenValue = vectorMaxEigenValue.sum(eigenVectors.getColumn(i));
        }

        do{
            k++;
            prev = vectorMaxEigenValue.get(0);
            vectorMaxEigenValue = mul(vectorMaxEigenValue);
            maxEigenValue = vectorMaxEigenValue.get(0) / prev;
        } while (posterioriForEigen(maxEigenValue, vectorMaxEigenValue) >= epsilon);
        vectorMaxEigenValue.normalize();
        return new ThreeElements<>(maxEigenValue, vectorMaxEigenValue, k);
    }

    public ThreeElements<Double, MyVector, Integer> findMaxEigenScalarProduct(double epsilon){
        int k = 0;
        double maxEigenValue = 0;
        ThreeElements<MyMatrix, MyMatrix, Integer> valVec = findEigenValueVector(1.e-6);
        MyMatrix eigenVectors = valVec.getSecond();
        MyVector prev;
        MyVector vectorMaxEigenValue = MyVector.getZeroVector(rows);
        for (int i = 0; i < columns; i++) {
            vectorMaxEigenValue = vectorMaxEigenValue.sum(eigenVectors.getColumn(i));
        }

        do{
            k++;
            prev = vectorMaxEigenValue.copy();
            vectorMaxEigenValue = mul(vectorMaxEigenValue);
            maxEigenValue = vectorMaxEigenValue.mulScalar(prev) / prev.mulScalar(prev);
        } while (posterioriForEigen(maxEigenValue, vectorMaxEigenValue) >= epsilon);
        vectorMaxEigenValue.normalize();

        return new ThreeElements<>(maxEigenValue, vectorMaxEigenValue, k);
    }

    /**
     * Находит апостериорную оценку для степенного метода.
     *
     * @param eigenValue собственное число
     * @param vectorEigenValue собственный вектор
     * @return апостериорная оценка
     */
    private double posterioriForEigen(double eigenValue, MyVector vectorEigenValue) {
        double numerator = mul(vectorEigenValue).diff(vectorEigenValue.mul(eigenValue)).calcTwoNorm();
        double denominator = vectorEigenValue.calcTwoNorm();
        return numerator / denominator;
    }

    public TwoElements<Double, MyVector> findEigenValueOpposite(double epsilonForMaxEigen) {
        ThreeElements<Double, MyVector, Integer> maxEigenValueVector = findMaxEigenScalarProduct(epsilonForMaxEigen);
        double maxEigenValue = maxEigenValueVector.getFirst();
        MyMatrix matrixB = this.diff(makeUnitMatrix(this.rows).mul(maxEigenValue));
        maxEigenValueVector = matrixB.findMaxEigenScalarProduct(epsilonForMaxEigen);
        return new TwoElements<>(maxEigenValueVector.getFirst() + maxEigenValue, maxEigenValueVector.getSecond());
    }

    public TwoElements<Double, MyVector> findEigenValueWielandt(double epsilon, double approximateEigenValue, boolean refine) {
        int k = 0;
        double maxEigenValueW;
        double[] values = new double[3];
        int valuesLastInd = values.length - 1;
        values[valuesLastInd] = approximateEigenValue;
        ThreeElements<MyMatrix, MyMatrix, Integer> valVec = findEigenValueVector(epsilon);
        MyMatrix eigenVectors = valVec.getSecond();
        MyVector vectorMaxEigenValue = MyVector.getZeroVector(rows);
        for (int i = 0; i < columns; i++) {
            vectorMaxEigenValue = vectorMaxEigenValue.sum(eigenVectors.getColumn(i));
        }
        MyMatrix matrixW = this.diff(makeUnitMatrix(this.rows).mul(approximateEigenValue));
        MyVector prevVector;


        do{
            k++;
            for (int i = 0; i < valuesLastInd; i++) {
                values[i] = values[i + 1];
            }
            prevVector = vectorMaxEigenValue.copy();
            vectorMaxEigenValue = matrixW.findInverseMatrix().mul(prevVector);
            maxEigenValueW = vectorMaxEigenValue.mulScalar(prevVector) / prevVector.mulScalar(prevVector);
            values[valuesLastInd] = 1 / maxEigenValueW + approximateEigenValue;
        } while (Math.abs(values[valuesLastInd] - values[valuesLastInd - 1]) >= epsilon);
        vectorMaxEigenValue.normalize();

        if (refine &&
            k >= 2 &&
            Math.abs(values[valuesLastInd]) < Math.abs(values[valuesLastInd - 1]) &&
            Math.abs(values[valuesLastInd - 1]) < Math.abs(values[valuesLastInd - 2])) {
            values[valuesLastInd] = (values[valuesLastInd] * values[valuesLastInd - 2] -
                Math.pow(values[valuesLastInd - 1], 2)) /
                (values[valuesLastInd] - 2 * values[valuesLastInd - 1] + values[valuesLastInd - 2]);
        }

        return new TwoElements<>(values[valuesLastInd], vectorMaxEigenValue);
    }
}
