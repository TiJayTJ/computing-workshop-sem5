package exercise.one.additional;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatrixPair {
    MyMatrix matrix1;
    MyMatrix matrix2;
    MyVector vector1;

    public MatrixPair(MyMatrix matrix1, MyMatrix matrix2){
        this.matrix1 = matrix1.copy();
        this.matrix2 = matrix2.copy();
    }
    public MatrixPair(MyMatrix matrix1, MyVector vector1){
        this.matrix1 = matrix1.copy();
        this.vector1 = vector1.copy();
    }
}
