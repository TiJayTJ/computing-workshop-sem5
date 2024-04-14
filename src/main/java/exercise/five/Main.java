package exercise.five;

import static additional.algebra.MyMatrix.printTable;

import additional.algebra.MyMatrix;
import additional.algebra.MyVector;
import additional.differential_equations.BoundaryConditions;
import additional.differential_equations.DifferentialEquation;

public class Main {

    public static void main(String[] args) {
        int n10 = 10;
        int n20 = 20;
        double a = -1;
        double b = 1;
        DifferentialEquation equation = new DifferentialEquation(a, b);
        BoundaryConditions conditions = new BoundaryConditions(0, 0, -1, 0, 1, 0);
        DiffEquationSolver solver = new DiffEquationSolver(equation);
        MyMatrix solution = new MyMatrix(n10 + 1,8);
        MyVector vectorY = solver.solve(n10, conditions);

        solution.setColumn(7, vectorY);
        solution.setColumn(0, solver.getVectorX());
        solution.setColumn(1, solver.getVectorA());
        solution.setColumn(2, solver.getVectorB());
        solution.setColumn(3, solver.getVectorC());
        solution.setColumn(4, solver.getVectorG());
        solution.setColumn(5, solver.getVectorS());
        solution.setColumn(6, solver.getVectorT());

        printTable(new String[]{"x_i", "A_i", "B_i", "C_i", "G_i", "S_i", "T_i", "Y_i"}, solution);

        solver.calcDiscrepancy((equation.getB()-equation.getA()) / n10, vectorY).printVector();
    }
}
