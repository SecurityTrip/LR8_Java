package functions;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTabulatedFunction implements TabulatedFunction {

    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {

        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) throws InappropriateFunctionPointException {
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        }

        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) throws InappropriateFunctionPointException {
            return new ArrayTabulatedFunction(leftX, rightX, values);
        }

        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) throws InappropriateFunctionPointException {
            return new ArrayTabulatedFunction(points);
        }

    }

    final static int ARRAY_RESERVE = 32;
    private int pointsCount = 0;
    private FunctionPoint[] points;

    public ArrayTabulatedFunction() {

        pointsCount = 0;
        points = new FunctionPoint[ARRAY_RESERVE];

    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException, InappropriateFunctionPointException {
        this(leftX, rightX, new double[pointsCount]);
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException, InappropriateFunctionPointException {

        if (leftX >= rightX)
            throw new IllegalArgumentException("Right domain border must be greater than left");

        if (values.length < 2)
            throw new IllegalArgumentException("Number of points must not be less than 2");

        points = new FunctionPoint[values.length + ARRAY_RESERVE];
        double interval = (rightX - leftX) / (values.length - 1);

        for (int index = 0; index < values.length; ++index)
            addPoint(new FunctionPoint(leftX + interval * index, values[index]));

    }

    public ArrayTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException, InappropriateFunctionPointException {

        if (points.length < 2)
            throw new IllegalArgumentException("Number of points must not be less than 2");

        this.points = new FunctionPoint[points.length + ARRAY_RESERVE];
        for (int index = 0; index < points.length; ++index) {

            if (index > 0 && points[index].getX() < points[index - 1].getX())
                throw new IllegalArgumentException("Points must be sorted by X");

            addPoint(points[index]);

        }

    }

    public void print() {

        System.out.println();
        for (int index = 0; index < getPointsCount(); ++index)
            System.out.println(getPointX(index) + " " + getPointY(index));

    }

    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < pointsCount;
            }

            @Override
            public FunctionPoint next() throws NoSuchElementException {

                if (!hasNext())
                    throw new NoSuchElementException();

                return points[index++];

            }

            @Override
            public void remove() throws UnsupportedOperationException {
                throw new UnsupportedOperationException();
            }

        };
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public void setPointsCount(int count) {
        pointsCount = count;
    }

    public double getLeftDomainBorder() {
        return getPoint(0).getX();
    }

    public double getRightDomainBorder() {
        return getPoint(getPointsCount() - 1).getX();
    }

    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {

        if (!isCorrectPosition(index))
            throw new FunctionPointIndexOutOfBoundsException(index);

        return points[index];

    }

    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {

        if (!isCorrectPosition(index))
            throw new FunctionPointIndexOutOfBoundsException(index);

        if (!isClamped(index, point.getX()))
            throw new InappropriateFunctionPointException("Point must be clamped between its neighbours");

        points[index] = point;

    }

    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {

        if (!isCorrectPosition(index))
            throw new FunctionPointIndexOutOfBoundsException(index);

        return getPoint(index).getX();

    }

    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {

        if (!isCorrectPosition(index))
            throw new FunctionPointIndexOutOfBoundsException(index);

        if (!isClamped(index, x))
            throw new InappropriateFunctionPointException("Point must be clamped between its neighbours");

        getPoint(index).setX(x);

    }

    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {

        if (!isCorrectPosition(index))
            throw new FunctionPointIndexOutOfBoundsException(index);

        return getPoint(index).getY();

    }

    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {

        if (!isCorrectPosition(index))
            throw new FunctionPointIndexOutOfBoundsException(index);

        getPoint(index).setY(y);

    }

    public double getFunctionValue(double x) {

        if (isOutOfBounds(x)) {

            System.out.println("123");
            return Double.NaN;

        }

        for (int index = 0; index < getPointsCount(); ++index) {

            if (Double.compare(getPointX(index), x) == 0)
                return getPointY(index);

            if (Double.compare(getPointX(index + 1), x) == 0)
                return getPointY(index + 1);

            if (x >= getPointX(index) && x <= getPointX(index + 1)) {

                FunctionPoint left = getPoint(index);
                FunctionPoint right = getPoint(index + 1);

                double k = (right.getY() - left.getY()) / (right.getX() - left.getX());
                double b = right.getY() - k * right.getX();
                return k * x + b;

            }

        }

        return Double.NaN;

    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {

        int position = 0;
        for (int index = getPointsCount() - 1; index >= 0; --index) {

            if (point.getX() == getPointX(index))
                throw new InappropriateFunctionPointException("Function points must be different");

            if (point.getX() > getPointX(index)) {

                position = index + 1;
                break;

            }

        }

        // Expand array when free cells is going out
        if (getPointsCount() == points.length - 1) {

            FunctionPoint[] old = new FunctionPoint[points.length];
            System.arraycopy(points, 0, old, 0, points.length);

            points = new FunctionPoint[points.length + ARRAY_RESERVE];
            System.arraycopy(old, 0, points, 0, old.length);

        }

        if (position < getPointsCount())
            System.arraycopy(points, position, points, position + 1, getPointsCount() - position);

        points[position] = point;
        setPointsCount(getPointsCount() + 1);

    }

    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException {

        if (!isCorrectPosition(index))
            throw new FunctionPointIndexOutOfBoundsException(index);

        if (getPointsCount() <= 2)
            throw new IllegalStateException("Function must have at least 2 points");

        setPointsCount(getPointsCount() - 1);
        if (index < getPointsCount())
            System.arraycopy(points, index + 1, points, index, getPointsCount() - index + 1);

    }

    protected boolean isCorrectPosition(int index) {
        return index >= 0 && index < getPointsCount();
    }

    protected boolean isClamped(int index, double x) {
        double leftX = index > 0 ? getPointX(index - 1) : getLeftDomainBorder();
        double rightX = index < getPointsCount() - 1 ? getPointX(index + 1) : getRightDomainBorder();
        return x > leftX && x < rightX;
    }

    protected boolean isOutOfBounds(double x) {
        return x < getLeftDomainBorder() || x > getRightDomainBorder();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

        out.writeInt(pointsCount);
        for (int index = 0; index < getPointsCount(); ++index)
            out.writeObject(getPoint(index));

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        pointsCount = in.readInt();
        points = new FunctionPoint[pointsCount + ARRAY_RESERVE];

        for (int index = 0; index < getPointsCount(); ++index)
            points[index] = (FunctionPoint) in.readObject();

    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder("{");
        for (int index = 0; index < getPointsCount(); ++index) {

            if (index != 0) builder.append(", ");
            builder.append(getPoint(index).toString());

        }

        builder.append("}");
        return builder.toString();

    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof TabulatedFunction f))
            return false;

        if (pointsCount != f.getPointsCount())
            return false;

        for (int index = 0; index < pointsCount; ++index)
            if (!points[index].equals(f.getPoint(index)))
                return false;

        return true;

    }

    @Override
    public int hashCode() {

        int code = pointsCount;
        for (int index = 0; index < pointsCount; ++index)
            code ^= points[index].hashCode() * 3;
        return code;

    }

    @Override
    public Object clone() throws CloneNotSupportedException {

        ArrayTabulatedFunction f = (ArrayTabulatedFunction) super.clone();
        f.points = new FunctionPoint[f.pointsCount + ARRAY_RESERVE];

        for (int index = 0; index < pointsCount; ++index)
            f.points[index] = (FunctionPoint) points[index].clone();
        return f;

    }

}
