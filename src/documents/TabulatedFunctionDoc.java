package documents;

import com.google.gson.Gson;
import functions.*;

import java.io.*;
import java.util.Iterator;
import java.util.stream.Collectors;

public class TabulatedFunctionDoc implements TabulatedFunction {

    private TabulatedFunction tabulatedFunction;
    private String fileName;
    private boolean modified;

    public void newFunction(double leftX, double rightX, int pointsCount) throws InappropriateFunctionPointException {

        tabulatedFunction = TabulatedFunctions.createTabulatedFunction(leftX, rightX, pointsCount);
        modified = true;

    }

    public void tabulateFunction(Function function, double leftX, double rightX, int pointsCount) throws InappropriateFunctionPointException {

        tabulatedFunction = TabulatedFunctions.tabulate(function, leftX, rightX, pointsCount);
        modified = true;

    }

    public void saveFunctionAs(String fileName) {

        String serialized = (new Gson()).toJson(tabulatedFunction);
        try (FileWriter writer = new FileWriter(fileName)) {

            writer.write(serialized);

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

        this.fileName = fileName;
        modified = false;

    }

    public void loadFunction(String fileName) {

        String serialized;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            serialized = reader.lines().collect(Collectors.joining(System.lineSeparator()));

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

        tabulatedFunction = (new Gson()).fromJson(serialized, ArrayTabulatedFunction.class);
        modified = true;
        this.fileName = fileName;

    }

    public void saveFunction() {
        saveFunctionAs(this.fileName);
    }

    public boolean isModified() {
        return modified;
    }

    public boolean isFileNameAssigned() {
        return fileName != null;
    }

    @Override
    public void print() {
        tabulatedFunction.print();
    }

    @Override
    public Iterator<FunctionPoint> iterator() {
        return tabulatedFunction.iterator();
    }

    @Override
    public int getPointsCount() {
        return tabulatedFunction.getPointsCount();
    }

    @Override
    public void setPointsCount(int count) {
        tabulatedFunction.setPointsCount(count);
        modified = true;
    }

    @Override
    public double getLeftDomainBorder() {
        return tabulatedFunction.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return tabulatedFunction.getRightDomainBorder();
    }

    @Override
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        return tabulatedFunction.getPoint(index);
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        tabulatedFunction.setPoint(index, point);
        modified = true;
    }

    @Override
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        return tabulatedFunction.getPointX(index);
    }

    @Override
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        tabulatedFunction.setPointX(index, x);
        modified = true;
    }

    @Override
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        return tabulatedFunction.getPointY(index);
    }

    @Override
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        tabulatedFunction.setPointY(index, y);
        modified = true;
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        tabulatedFunction.addPoint(point);
        modified = true;
    }

    @Override
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException {
        tabulatedFunction.deletePoint(index);
        modified = true;
    }

    @Override
    public double getFunctionValue(double x) {
        return tabulatedFunction.getFunctionValue(x);
    }

    @Override
    public String toString() {
        return tabulatedFunction.toString();
    }

    @Override
    public int hashCode() {
        return tabulatedFunction.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof TabulatedFunctionDoc tfd)
            return tabulatedFunction.equals(tfd.tabulatedFunction);
        return tabulatedFunction.equals(obj);

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return tabulatedFunction.clone();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        tabulatedFunction.writeExternal(out);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        tabulatedFunction.readExternal(in);
    }

}
