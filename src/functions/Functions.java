package functions;

import functions.meta.*;

public class Functions {

    private Functions() {};

    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power) {
        return new Power(f, power);
    }

    public static Function sum(Function f, Function g) {
        return new Sum(f, g);
    }

    public static Function mult(Function f, Function g) {
        return new Mult(f, g);
    }

    public static Function composition(Function f, Function g) {
        return new Composition(f, g);
    }

    public static double integral(Function f, double leftX, double rightX, double step) {

        if (leftX < f.getLeftDomainBorder() || rightX > f.getRightDomainBorder())
            throw new IllegalArgumentException("Integral bounds must be inside domain borders");

        double x, sum = 0.0;
        for (x = leftX; x + step <= rightX; x += step) {

            double a = Math.abs(f.getFunctionValue(x));
            double b = Math.abs(f.getFunctionValue(x + step));
            sum += (a + b) * step / 2.0;

        }

        double a = Math.abs(f.getFunctionValue(x));
        double b = Math.abs(f.getFunctionValue(rightX));
        sum += (a + b) * (rightX - x) / 2.0;

        return sum;

    }

}
