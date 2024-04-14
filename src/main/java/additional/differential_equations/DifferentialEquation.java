package additional.differential_equations;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DifferentialEquation {

    double a; // левая граница области определения функций
    double b; // правая граница области определения функций

    public double findFunctionP(double x) {
        return (x + 4) / (x + 5);
//        return 2 / (2 + x);
    }

    public double findFunctionQ(double x) {
        return 0;
//        return (1 + x) / 2;
    }

    public double findFunctionR(double x) {
        return Math.exp(x / 4);
//        return Math.cos(x / 2);
    }

    public double findFunctionF(double x) {
        return 2 - x;
//        return 1 + x / 2;
    }
}
