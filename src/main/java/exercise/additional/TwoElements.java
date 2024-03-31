package exercise.additional;

import lombok.Data;

@Data
public class TwoElements<T, E> {
    private T first;
    private E second;

    public TwoElements(T element1, E element2){
        this.first = element1;
        this.second = element2;
    }
}
