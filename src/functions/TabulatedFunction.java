package functions;

import java.io.Externalizable;
import java.util.Iterator;

public interface TabulatedFunction extends Function, Externalizable, Cloneable, Iterable<FunctionPoint> {

    void print();

    @Override
    Iterator<FunctionPoint> iterator();

    int getPointsCount();
    void setPointsCount(int count);

    FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException;
    void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;

    double getPointX(int index) throws FunctionPointIndexOutOfBoundsException;
    void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;

    double getPointY(int index) throws FunctionPointIndexOutOfBoundsException;
    void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException;

    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;
    void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException;

    Object clone() throws CloneNotSupportedException;

}
