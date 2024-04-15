package exercise.five;

import additional.algebra.MyMatrix;
import additional.differential_equations.BoundaryConditions;
import additional.differential_equations.DifferentialEquation;
import additional.algebra.MyVector;
import lombok.Data;

@Data
public class DiffEquationSolver {

    DifferentialEquation equation;

    public DiffEquationSolver(DifferentialEquation equation){
        this.equation = equation;
    }

    public MyVector calcDiscrepancy(MyMatrix result){
        MyVector vectorX = result.getColumn(0);
        MyVector solution = result.getColumn(7);
        int size = solution.getSize();
        double node;
        double h = (equation.getB() - equation.getA()) / size;
        MyVector discrepancy = new MyVector(size);
        MyVector derivative1Y = new MyVector(size);
        MyVector derivative2Y = new MyVector(size);
        MyVector derivative1P = new MyVector(size);

        // Вычисление производных в крайних узлах
        derivative1Y.set(0, (solution.get(1) - solution.get(0)) / h);
        derivative1Y.set(size - 1, (solution.get(size - 1) - solution.get(size - 2)) / h);
        derivative1P.set(0, (equation.getP().apply(vectorX.get(1)) -
            equation.getP().apply(vectorX.get(0))) / h);
        derivative1P.set(size - 1, (equation.getP().apply(vectorX.get(size - 1)) -
            equation.getP().apply(vectorX.get(size - 2))) / h);

        // Вычисление производных в оставшихся узлах
        for (int i = 1; i < size - 1; i++) {
            derivative1Y.set(i, (solution.get(i + 1) - solution.get(i - 1)) / (2 * h));
            derivative2Y.set(i, (solution.get(i + 1) - 2 * solution.get(i) + solution.get(i - 1)) / (h * h));
            derivative1P.set(i, (equation.getP().apply( vectorX.get(i + 1)) -
                equation.getP().apply( vectorX.get(i - 1))) / (2 * h));
        }

        // Сравниваем Ly = -p(x)y''(x) + (-p'(x) + q(x))y'(x) + r(x)y(x) и f(x)
        for (int i = 0; i < size; i++) {
            node = vectorX.get(i);
            discrepancy.set(i, -equation.getP().apply(node) * derivative2Y.get(i) +
                (-derivative1P.get(i) + equation.getQ().apply(node)) * derivative1Y.get(i) +
                equation.getR().apply(node) * solution.get(i) - equation.getF().apply(node));
        }

        return discrepancy;
    }

    public MyMatrix solve(int n, BoundaryConditions conditions){
        MyMatrix result = new MyMatrix(n+1, 8);
        findVectorsABCG(n, conditions, result);
        return findSolutionY(n, result);
    }

    /**
     * Находит коэффициенты для каждого узла.
     *
     * @param n количество узлов
     * @param cond граничные условия
     */
    private void findVectorsABCG(int n, BoundaryConditions cond, MyMatrix result){
        double a = equation.getA();
        double b = equation.getB();
        double h = (b - a) / (n+1);
        MyVector vectorA = new MyVector(n + 1);
        MyVector vectorB = new MyVector(n + 1);
        MyVector vectorC = new MyVector(n + 1);
        MyVector vectorG = new MyVector(n + 1);
        MyVector vectorX = new MyVector(n + 1);

        // Вычисление первых и последних элементов
        vectorX.set(0, a);
        vectorA.set(0, 0);
        vectorB.set(0, h * cond.getAlfa1() + cond.getAlfa2());
        vectorC.set(0, cond.getAlfa2());
        vectorG.set(0, -h * cond.getAlfa());
        vectorA.set(n, cond.getBetta2());
        vectorB.set(n, h * cond.getBetta1() + cond.getBetta2());
        vectorC.set(n, 0);
        vectorG.set(n, -h * cond.getBetta());
        vectorX.set(n, b);

        // Вычисление оставшихся элементов
        for (int i = 1; i < n; i++) {
            vectorX.set(i, a + h * i);
            vectorA.set(i, -equation.getP().apply(vectorX.get(i)) -
                equation.getQ().apply(vectorX.get(i)) * h / 2);
            vectorC.set(i, -equation.getP().apply(vectorX.get(i)) +
                equation.getQ().apply(vectorX.get(i)) * h / 2);
            vectorB.set(i, vectorA.get(i) + vectorC.get(i) - h * h * equation.getR().apply(vectorX.get(i)));
            vectorG.set(i, h * h * equation.getF().apply(vectorX.get(i)));
        }

        result.setColumn(0, vectorX);
        result.setColumn(1, vectorA);
        result.setColumn(2, vectorB);
        result.setColumn(3, vectorC);
        result.setColumn(4, vectorG);
    }

    private MyMatrix findSolutionY (int n, MyMatrix result){
        MyVector vectorA = result.getColumn(1);
        MyVector vectorB = result.getColumn(2);
        MyVector vectorC = result.getColumn(3);
        MyVector vectorG = result.getColumn(4);
        MyVector vectorS = new MyVector(n + 1);
        MyVector vectorT = new MyVector(n + 1);
        MyVector vectorY = new MyVector(n + 1);

        // Считаем коэффициенты S, T
        vectorS.set(0, vectorC.get(0) / vectorB.get(0));
        vectorT.set(0, -vectorG.get(0) / vectorB.get(0));
        for (int i = 1; i <= n; i++) {
            vectorS.set(i, vectorC.get(i) / (vectorB.get(i) - vectorA.get(i) * vectorS.get(i-1)));
            vectorT.set(i, (vectorA.get(i) * vectorT.get(i-1) - vectorG.get(i)) /
                (vectorB.get(i) - vectorA.get(i) * vectorS.get(i-1)));
        }

        // Посчитали решение Y
        vectorY.set(n, vectorT.get(n));

        for (int i = n-1; i >= 0; i--) {
            vectorY.set(i, vectorS.get(i) * vectorY.get(i+1) + vectorT.get(i));
        }

        result.setColumn(5, vectorS);
        result.setColumn(6, vectorT);
        result.setColumn(7, vectorY);

        return result;
    }
}
