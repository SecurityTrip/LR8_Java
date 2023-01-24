package functions.meta;

import functions.Function;

public class Sum implements Function {

    private final Function f;
    private final Function g;

    public Sum(Function f, Function g) {

        this.f = f;
        this.g = g;

    }

    @Override
    public double getLeftDomainBorder() {
        return Math.max(f.getLeftDomainBorder(), g.getLeftDomainBorder());
    }

    @Override
    public double getRightDomainBorder() {
        return Math.min(f.getRightDomainBorder(), g.getRightDomainBorder());
    }

    @Override
    public double getFunctionValue(double x) {
        return f.getFunctionValue(x) + g.getFunctionValue(x);
    }

}
