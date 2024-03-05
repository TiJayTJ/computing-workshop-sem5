package exercise.one;

public class Main {

    public static void main(String[] args) {
        double[][] matrixA = {{8.29381, 0.995516, -0.560617, 0.766522},
                              {0.995516, 6.298198, 0.595772, 3.844422},
                              {-0.560617, 0.595772, 4.997407, 5.239231}};
        new LinearSystemsSolver(matrixA).solve();
    }
}
