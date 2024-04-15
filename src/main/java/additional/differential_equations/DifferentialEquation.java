package additional.differential_equations;

import additional.Function;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DifferentialEquation {

    double a; // левая граница области определения функций
    double b; // правая граница области определения функций

    Function p;
    Function q;
    Function r;
    Function f;
}
