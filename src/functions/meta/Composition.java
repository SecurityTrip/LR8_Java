package functions.meta;

import functions.Function;

public class Composition implements Function {

    private final Function f;
    private final Function g;

    public Composition(Function f, Function g) {

        this.f = f;
        this.g = g;

    }

    @Override
    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return f.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        return g.getFunctionValue(f.getFunctionValue(x));
    }

}
