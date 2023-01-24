package threads;

import functions.Function;

public class Task {

    private Function function;
    private double leftX;
    private double rightX;
    private double step;

    public Task(Function function, double leftX, double rightX, double step) {

        this.function = function;
        this.leftX = leftX;
        this.rightX = rightX;
        this.step = step;

    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public double getLeftX() {
        return leftX;
    }

    public void setLeftX(double leftX) {
        this.leftX = leftX;
    }

    public double getRightX() {
        return rightX;
    }

    public void setRightX(double rightX) {
        this.rightX = rightX;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

}
