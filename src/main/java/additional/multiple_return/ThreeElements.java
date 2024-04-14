package additional.multiple_return;

import lombok.Data;

@Data
public class ThreeElements<T, E, F> {
    private T first;
    private E second;
    private F third;

    public ThreeElements(T element1, E element2, F element3){
        this.first = element1;
        this.second = element2;
        this.third = element3;
    }
}
