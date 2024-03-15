package exercise.additional;

import lombok.Data;

@Data
public class ThreeElements<T, E, F> {
    T one;
    E two;
    F thee;

    public ThreeElements(T matrix1, E matrix2, F vector1){
        this.one = matrix1;
        this.two = matrix2;
        this.thee = vector1;
    }
}
