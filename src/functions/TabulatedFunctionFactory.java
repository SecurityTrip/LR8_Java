package functions;

public interface TabulatedFunctionFactory {

    TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) throws InappropriateFunctionPointException;
    TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) throws InappropriateFunctionPointException;
    TabulatedFunction createTabulatedFunction(FunctionPoint[] points) throws InappropriateFunctionPointException;


}
