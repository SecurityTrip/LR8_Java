package functions.meta;

import functions.Function;

public class Shift implements Function {

    private final Function f;
    private final double shiftX;
    private final double shiftY;

    public Shift(Function f, double shiftX, double shiftY) {

        this.f = f;
        this.shiftX = shiftX;
        this.shiftY = shiftY;

    }

    @Override
    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder() + shiftX;
    }

    @Override
    public double getRightDomainBorder() {
        return f.getRightDomainBorder() + shiftX;
    }

    @Override
    public double getFunctionValue(double x) {
        return f.getFunctionValue(x) + shiftY;
    }

}
