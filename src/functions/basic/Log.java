package functions.basic;

import functions.Function;

public class Log implements Function {

    private final double baseLog;

    public Log(double base) throws IllegalArgumentException {

        if (base < 0)
            throw new IllegalArgumentException("Base of log must be greater than 0");

        if (base == 1)
            throw new IllegalArgumentException("Base of log must not be 1");

        baseLog = Math.log(base);

    }

    @Override
    public double getLeftDomainBorder() {
        return -Double.MAX_VALUE;
    }

    @Override
    public double getRightDomainBorder() {
        return Double.MAX_VALUE;
    }

    @Override
    public double getFunctionValue(double x) {
        return Math.log(x) / baseLog;
    }
}
