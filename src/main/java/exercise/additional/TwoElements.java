package exercise.additional;

import lombok.Data;

@Data
public class TwoElements<T, E> {
    T one;
    E two;

    public TwoElements(T matrix1, E matrix2){
        this.one = matrix1;
        this.two = matrix2;
    }
}
