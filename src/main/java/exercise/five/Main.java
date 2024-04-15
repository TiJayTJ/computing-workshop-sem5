package exercise.five;

import static additional.algebra.MyMatrix.printTable;

import additional.Function;
import additional.algebra.MyMatrix;
import additional.algebra.MyVector;
import additional.differential_equations.BoundaryConditions;
import additional.differential_equations.DifferentialEquation;

public class Main {

    public static final int N_10 = 10;
    public static final int N_20 = 20;
    public static final int B = 1;
    public static final int A = -1;
    static final Function P = x -> (x + 4) / (x + 5);
    static final Function Q = x -> 0;
    static final Function R = x -> Math.exp(x / 4);
    static final Function F = x -> 2 - x;

    public static void main(String[] args) {
        DifferentialEquation equation = new DifferentialEquation(A, B, P, Q, R, F);
        BoundaryConditions conditions = new BoundaryConditions(0, 0, -1, 0, 1, 0);
        DiffEquationSolver solver = new DiffEquationSolver(equation);
        MyMatrix result = solver.solve(N_10, conditions);

        printTable(new String[]{"x_i", "A_i", "B_i", "C_i", "G_i", "S_i", "T_i", "Y_i"}, result);

        solver.calcDiscrepancy(result).printVector();
    }
}
