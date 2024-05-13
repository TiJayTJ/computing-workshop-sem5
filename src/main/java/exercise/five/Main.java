package exercise.five;

import static additional.Formatter.divide;
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
    static String[] TABLE_HEADER = new String[]{"x_i", "A_i", "B_i", "C_i", "G_i", "S_i", "T_i", "Y_i"};

    public static void main(String[] args) {
        DifferentialEquation equation = new DifferentialEquation(A, B, P, Q, R, F);
        BoundaryConditions conditions = new BoundaryConditions(0, 0, -1, 0, 1, 0);
        DiffEquationSolver solver = new DiffEquationSolver(equation);

        System.out.println("Решения с обычной сеткой\n");

        MyMatrix resultH1_10 = solver.solveAcc1(N_10, conditions);
        MyMatrix resultH1_20 = solver.solveAcc1(N_20, conditions);
        System.out.printf("Number of nodes:\t%d%n", N_10);
        printTable(TABLE_HEADER, resultH1_10);
        System.out.println("n = 10; f(x) - Ly = ");
        solver.calcDiscrepancy(resultH1_10).printVector();

        divide();

        System.out.printf("Number of nodes:\t%d%n", N_20);
        printTable(TABLE_HEADER, resultH1_20);
        System.out.println("n = 20; f(x) - Ly = ");
        solver.calcDiscrepancy(resultH1_20).printVector();

        divide();

        MyVector clSolutionH1 = solver.clarifyRichardson(1, resultH1_10, resultH1_20);
        MyMatrix clResultH1_10 = resultH1_10.copy();
        clResultH1_10.setColumn(7, clSolutionH1);
        System.out.println("n = 20; f(x) - Ly_r = ");
        solver.calcDiscrepancy(clResultH1_10).printVector();

        divide();

        System.out.println("Решения со смещённой сеткой\n");

        MyMatrix resultH2_10 = solver.solveAcc2(N_10, conditions);
        MyMatrix resultH2_20 = solver.solveAcc2(N_20, conditions);
        System.out.printf("Number of nodes:\t%d%n", N_10);
        printTable(TABLE_HEADER, resultH2_10);
        System.out.println("n = 10; f(x) - Ly = ");
        solver.calcDiscrepancy(resultH2_10).printVector();

        divide();

        System.out.printf("Number of nodes:\t%d%n", N_20);
        printTable(TABLE_HEADER, resultH2_20);
        System.out.println("n = 20; f(x) - Ly = ");
        solver.calcDiscrepancy(resultH2_20).printVector();

        divide();

        MyVector clSolutionH2 = solver.clarifyRichardson(1, resultH1_10, resultH1_20);
        MyMatrix clResultH2_10 = resultH2_10.copy();
        clResultH2_10.setColumn(7, clSolutionH2);
        System.out.println("n = 20; f(x) - Ly_r = ");
        solver.calcDiscrepancy(clResultH2_10).printVector();
    }
}
