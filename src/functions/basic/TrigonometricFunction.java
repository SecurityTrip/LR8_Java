package functions.basic;

import functions.Function;

abstract class TrigonometricFunction implements Function {

    @Override
    public double getLeftDomainBorder() {
        return -Double.MAX_VALUE;
    }

    @Override
    public double getRightDomainBorder() {
        return Double.MAX_VALUE;
    }

}
