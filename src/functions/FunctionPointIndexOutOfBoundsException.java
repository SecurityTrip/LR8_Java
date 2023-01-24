package functions;

public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException {

    public FunctionPointIndexOutOfBoundsException(String message) {
        super(message);
    }

    public FunctionPointIndexOutOfBoundsException(int index) {
        super(index);
    }

}
