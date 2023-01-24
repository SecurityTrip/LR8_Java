package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable, Cloneable {

    private double x;
    private double y;

    FunctionPoint() {
        x = 0.0;
        y = 0.0;
    }

    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public FunctionPoint(FunctionPoint point) {
        x = point.getX();
        y = point.getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof FunctionPoint point))
            return false;

        if (Double.compare(x, point.x) != 0)
            return false;

        return Double.compare(y, point.y) == 0;

    }

    @Override
    public int hashCode() {
        return (Double.valueOf(x).hashCode() + "x_point".hashCode()) ^ (Double.valueOf(y).hashCode() + "y_point".hashCode());
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
