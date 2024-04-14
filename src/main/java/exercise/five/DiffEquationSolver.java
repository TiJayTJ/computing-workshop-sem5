package exercise.five;

import additional.algebra.MyMatrix;
import additional.differential_equations.BoundaryConditions;
import additional.differential_equations.DifferentialEquation;
import additional.algebra.MyVector;
import lombok.Data;

@Data
public class DiffEquationSolver {

    DifferentialEquation equation;
    private MyVector vectorA;
    private MyVector vectorB;
    private MyVector vectorC;
    private MyVector vectorG;
    private MyVector vectorS;
    private MyVector vectorT;
    private MyVector vectorX;

    public DiffEquationSolver(DifferentialEquation equation){
        this.equation = equation;
    }

    public MyVector calcDiscrepancy(double h, MyVector solution){
        int size = solution.getSize();
        double node;
        MyVector discrepancy = new MyVector(size);
        MyVector derivative1Y = new MyVector(size);
        MyVector derivative2Y = new MyVector(size);
        MyVector derivative1P = new MyVector(size);

        // Вычисление производных в крайних узлах
        derivative1Y.set(0, (solution.get(1) - solution.get(0)) / h);
        derivative1Y.set(size - 1, (solution.get(size - 1) - solution.get(size - 2)) / h);
        derivative1P.set(0, (equation.findFunctionP(vectorX.get(1)) -
            equation.findFunctionP(vectorX.get(0))) / h);
        derivative1P.set(size - 1, (equation.findFunctionP(vectorX.get(size - 1)) -
            equation.findFunctionP(vectorX.get(size - 2))) / h);

        // Вычисление производных в оставшихся узлах
        for (int i = 1; i < size - 1; i++) {
            derivative1Y.set(i, (solution.get(i + 1) - solution.get(i - 1)) / (2 * h));
            derivative2Y.set(i, (solution.get(i + 1) - 2 * solution.get(i) + solution.get(i - 1)) / (h * h));
            derivative1P.set(i, (equation.findFunctionP( vectorX.get(i + 1)) -
                equation.findFunctionP( vectorX.get(i - 1))) / (2 * h));
        }

        // Сравниваем Ly = p(x)y''(x) + (p'(x) + q(x))y'(x) + r(x)y(x) и f(x)
        for (int i = 0; i < size; i++) {
            node = vectorX.get(i);
            discrepancy.set(i, equation.findFunctionP(node) * derivative2Y.get(i) +
                (derivative1P.get(i) + equation.findFunctionQ(node)) * derivative1Y.get(i) +
                equation.findFunctionR(node) * solution.get(i) - equation.findFunctionF(node));
        }

        return discrepancy;
    }

    public MyVector solve(int n, BoundaryConditions conditions){
        findVectorsABCG(n, conditions);
        return findSolutionY(n);
    }

    /**
     * Находит коэффициенты для каждого узла.
     *
     * @param n количество узлов
     * @param cond граничные условия
     */
    private void findVectorsABCG(int n, BoundaryConditions cond){
        double a = equation.getA();
        double b = equation.getB();
        double h = (b - a) / n;
        vectorA = new MyVector(n + 1);
        vectorB = new MyVector(n + 1);
        vectorC = new MyVector(n + 1);
        vectorG = new MyVector(n + 1);
        vectorX = new MyVector(n + 1);

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
            vectorA.set(i, -equation.findFunctionP(vectorX.get(i)) -
                equation.findFunctionQ(vectorX.get(i)) * h / 2);
            vectorC.set(i, -equation.findFunctionP(vectorX.get(i)) +
                equation.findFunctionQ(vectorX.get(i)) * h / 2);
            vectorB.set(i, vectorA.get(i) + vectorC.get(i) - h * h * equation.findFunctionR(vectorX.get(i)));
            vectorG.set(i, h * h * equation.findFunctionF(vectorX.get(i)));
        }
    }

    private MyVector findSolutionY (int n){
        vectorS = new MyVector(n + 1);
        vectorT = new MyVector(n + 1);
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

        return vectorY;
    }
}
