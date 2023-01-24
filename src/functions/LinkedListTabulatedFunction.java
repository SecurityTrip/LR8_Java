package functions;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListTabulatedFunction implements TabulatedFunction {

    public static class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory {

        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) throws InappropriateFunctionPointException {
            return new LinkedListTabulatedFunction(leftX, rightX, pointsCount);
        }

        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) throws InappropriateFunctionPointException {
            return new LinkedListTabulatedFunction(leftX, rightX, values);
        }

        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) throws InappropriateFunctionPointException {
            return new LinkedListTabulatedFunction(points);
        }

    }

    private static class FunctionNode {

        private FunctionPoint point;
        private FunctionNode prev;
        private FunctionNode next;

    }

    private int pointsCount = 0;
    private FunctionNode head;

    public LinkedListTabulatedFunction() {

        pointsCount = 0;

        head = new FunctionNode();
        head.prev = head;
        head.next = head;

    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException, InappropriateFunctionPointException {
        this(leftX, rightX, new double[pointsCount]);
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException, InappropriateFunctionPointException {

        if (leftX >= rightX)
            throw new IllegalArgumentException("Right domain border must be greater than left");

        if (values.length < 2)
            throw new IllegalArgumentException("Number of points must not be less than 2");

        head = new FunctionNode();
        head.prev = head;
        head.next = head;

        double interval = (rightX - leftX) / (values.length - 1);
        for (int index = 0; index < values.length; ++index)
            addPoint(new FunctionPoint(leftX + interval * index, values[index]));

    }

    public LinkedListTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException, InappropriateFunctionPointException {

        if (points.length < 2)
            throw new IllegalArgumentException("Number of points must not be less than 2");

        head = new FunctionNode();
        head.prev = head;
        head.next = head;

        for (int index = 0; index < points.length; ++index) {

            if (index > 0 && points[index].getX() < points[index - 1].getX())
                throw new IllegalArgumentException("Points must be sorted by X");

            addPoint(points[index]);

        }

    }

    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<>() {

            private FunctionNode index = head.next;

            @Override
            public boolean hasNext() {
                return index != head;
            }

            @Override
            public FunctionPoint next() throws NoSuchElementException {

                if (!hasNext())
                    throw new NoSuchElementException();

                FunctionPoint point = index.point;
                index = index.next;
                return point;

            }

            @Override
            public void remove() throws UnsupportedOperationException {
                throw new UnsupportedOperationException();
            }

        };
    }

    protected FunctionNode getNodeByIndex(int index) {

        FunctionNode current = head;
        while (index-- >= 0) current = current.next;
        return current;

    }

    protected FunctionNode addNodeToTail() {

        FunctionNode node = new FunctionNode();
        node.next = head;
        node.prev = head.prev;

        head.prev.next = node;
        head.prev = node;

        setPointsCount(getPointsCount() + 1);
        return node;

    }

    protected FunctionNode addNodeByIndex(int index) {

        FunctionNode node = new FunctionNode();
        FunctionNode current = getNodeByIndex(index);

        node.next = current;
        node.prev = current.prev;

        current.prev.next = node;
        current.prev = node;

        setPointsCount(getPointsCount() + 1);
        return node;

    }

    protected FunctionNode insertAfterNode(FunctionNode current) {

        FunctionNode node = new FunctionNode();

        node.prev = current;
        node.next = current.next;

        current.next.prev = node;
        current.next = node;

        setPointsCount(getPointsCount() + 1);
        return node;

    }

    protected FunctionNode deleteNodeByIndex(int index) {

        FunctionNode node = getNodeByIndex(index);

        node.prev.next = node.next;
        node.next.prev = node.prev;

        node.prev = null;
        node.next = null;

        setPointsCount(getPointsCount() - 1);
        return node;

    }

    public void print() {

        FunctionNode node = head;
        System.out.println();

        while ((node = node.next) != head)
            System.out.println(node.point.getX() + " " + node.point.getY());

    }

    public int getPointsCount() {
        return pointsCount;
    }

    public void setPointsCount(int count) {
        pointsCount = count;
    }

    public double getLeftDomainBorder() {
        return head.next.point.getX();
    }

    public double getRightDomainBorder() {
        return head.prev.point.getX();
    }

    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {

        if (!isCorrectPosition(index))
            throw new FunctionPointIndexOutOfBoundsException(index);

        return getNodeByIndex(index).point;

    }

    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {

        if (!isCorrectPosition(index))
            throw new FunctionPointIndexOutOfBoundsException(index);

        FunctionNode node = getNodeByIndex(index);
        if (!isClamped(node, point.getX()))
            throw new InappropriateFunctionPointException("Point must be clamped between its neighbours");

        node.point = point;

    }

    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {

        if (!isCorrectPosition(index))
            throw new FunctionPointIndexOutOfBoundsException(index);

        return getPoint(index).getX();

    }

    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {

        if (!isCorrectPosition(index))
            throw new FunctionPointIndexOutOfBoundsException(index);

        FunctionNode node = getNodeByIndex(index);
        if (!isClamped(node, x))
            throw new InappropriateFunctionPointException("Point must be clamped between its neighbours");

        node.point.setX(x);

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

        if (isOutOfBounds(x))
            return Double.NaN;

        FunctionNode node = head;
        while ((node = node.next) != head) {

            if (Double.compare(node.point.getX(), x) == 0)
                return node.point.getY();

            if (node.next != head && Double.compare(node.next.point.getX(), x) == 0)
                return node.next.point.getY();

            if (x >= node.point.getX() && x <= node.next.point.getX()) {

                FunctionPoint left = node.point;
                FunctionPoint right = node.next.point;

                double k = (right.getY() - left.getY()) / (right.getX() - left.getX());
                double b = right.getY() - k * right.getX();
                return k * x + b;

            }

        }

        return Double.NaN;

    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {

        FunctionNode position = head;
        FunctionNode current = head;

        while ((current = current.prev) != head) {

            if (point.getX() == current.point.getX())
                throw new InappropriateFunctionPointException("Function points must be different");

            if (point.getX() > current.point.getX()) {

                position = current;
                break;

            }

        }

        insertAfterNode(position).point = point;

    }

    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException {

        if (!isCorrectPosition(index))
            throw new FunctionPointIndexOutOfBoundsException(index);

        if (getPointsCount() <= 2)
            throw new IllegalStateException("Function must have at least 2 points");

        setPointsCount(getPointsCount() - 1);
        deleteNodeByIndex(index);

    }

    protected boolean isCorrectPosition(int index) {
        return index >= 0 && index < getPointsCount();
    }

    protected boolean isClamped(FunctionNode node, double x) {
        double leftX = node.prev == head ? getLeftDomainBorder() : node.prev.point.getX();
        double rightX = node.next == head ? getRightDomainBorder() : node.next.point.getX();
        return x > leftX && x < rightX;
    }

    protected boolean isOutOfBounds(double x) {
        return x < getLeftDomainBorder() || x > getRightDomainBorder();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

        out.writeInt(pointsCount);
        FunctionNode node = head;

        while ((node = node.next) != head)
            out.writeObject(node.point);

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        FunctionNode node = head;
        for (int index = 0, size = in.readInt(); index < size; ++index) {

            node = insertAfterNode(node);
            node.point = (FunctionPoint) in.readObject();

        }

    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder("{");
        FunctionNode node = head;

        while ((node = node.next) != head) {

            if (node.prev != head) builder.append(", ");
            builder.append(node.point.toString());

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
            if (!getPoint(index).equals(f.getPoint(index)))
                return false;

        return true;

    }

    @Override
    public int hashCode() {

        int code = pointsCount;
        FunctionNode node = head;

        while ((node = node.next) != head)
            code ^= node.point.hashCode() * 3;

        return code;

    }

    @Override
    public Object clone() throws CloneNotSupportedException {

        LinkedListTabulatedFunction f = (LinkedListTabulatedFunction) super.clone();
        FunctionNode node = head;
        FunctionNode newNode;

        f.head = new FunctionNode();
        FunctionNode current = f.head;

        while ((node = node.next) != head) {

            newNode = new FunctionNode();
            newNode.point = (FunctionPoint) node.point.clone();
            newNode.prev = current;
            current.next = newNode;
            current = newNode;

        }

        current.next = f.head;
        f.head.prev = current;

        return f;

    }

}
