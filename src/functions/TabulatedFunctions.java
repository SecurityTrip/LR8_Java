package functions;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

public class TabulatedFunctions {

    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    private TabulatedFunctions() {};

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory) {
        TabulatedFunctions.factory = factory;
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) throws InappropriateFunctionPointException {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) throws InappropriateFunctionPointException {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) throws InappropriateFunctionPointException {
        return factory.createTabulatedFunction(points);
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> classname, double leftX, double rightX, int pointsCount) {

        try {

            Class<? extends TabulatedFunction> loaded = (Class<? extends TabulatedFunction>) Class.forName(classname.getName());
            return loaded.getConstructor(new Class[]{ double.class, double.class, int.class })
                    .newInstance(leftX, rightX, pointsCount);

        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }

    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> classname, double leftX, double rightX, double[] values) {

        try {

            Class<? extends TabulatedFunction> loaded = (Class<? extends TabulatedFunction>) Class.forName(classname.getName());
            return loaded.getConstructor(new Class[]{ double.class, double.class, double[].class })
                    .newInstance(leftX, rightX, values);

        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }

    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> classname, FunctionPoint[] points) {

        try {

            Class<? extends TabulatedFunction> loaded = (Class<? extends TabulatedFunction>) Class.forName(classname.getName());
            return loaded.getConstructor(new Class[]{ FunctionPoint[].class })
                    .newInstance((Object) points);

        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }

    }

    public static TabulatedFunction tabulate(Function f, double leftX, double rightX, int pointsCount) throws InappropriateFunctionPointException {

        if (leftX < f.getLeftDomainBorder() || rightX > f.getRightDomainBorder())
            throw new IllegalArgumentException("Tabulated Function must not expand function bounds");

        TabulatedFunction function = TabulatedFunctions.createTabulatedFunction(leftX, rightX, pointsCount);
        for (int index = 0; index < pointsCount; ++index)
            function.setPointY(index, f.getFunctionValue(function.getPointX(index)));
        return function;

    }

    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> classname, Function f, double leftX, double rightX, int pointsCount) {

        if (leftX < f.getLeftDomainBorder() || rightX > f.getRightDomainBorder())
            throw new IllegalArgumentException("Tabulated Function must not expand function bounds");

        TabulatedFunction function = TabulatedFunctions.createTabulatedFunction(classname, leftX, rightX, pointsCount);
        for (int index = 0; index < pointsCount; ++index)
            function.setPointY(index, f.getFunctionValue(function.getPointX(index)));
        return function;

    }

    public static void outputTabulatedFunction(TabulatedFunction f, OutputStream out) throws IOException {

        StringBuilder encoded = new StringBuilder(String.valueOf(f.getPointsCount()));
        for (int index = 0; index < f.getPointsCount(); ++index) {

            encoded
                    .append(" ")
                    .append(f.getPointX(index))
                    .append(" ")
                    .append(f.getPointY(index));

        }

        out.write(encoded.toString().getBytes());
        out.flush();

    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException, InappropriateFunctionPointException {

        StringBuilder encoded = new StringBuilder();
        for (int ch; (ch = in.read()) != -1;)
            encoded.append((char) ch);

        String[] coords = encoded.toString().split(" ");
        int pointsCount = Integer.parseInt(coords[0]);

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int index = 0; index < pointsCount; ++index) {

            points[index] = new FunctionPoint(
                    Double.parseDouble(coords[index * 2 + 1]),
                    Double.parseDouble(coords[index * 2 + 2])
            );

        }

        return TabulatedFunctions.createTabulatedFunction(points);

    }

    public static TabulatedFunction inputTabulatedFunction(Class<? extends TabulatedFunction> classname, InputStream in) throws IOException {

        StringBuilder encoded = new StringBuilder();
        for (int ch; (ch = in.read()) != -1;)
            encoded.append((char) ch);

        String[] coords = encoded.toString().split(" ");
        int pointsCount = Integer.parseInt(coords[0]);

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int index = 0; index < pointsCount; ++index) {

            points[index] = new FunctionPoint(
                    Double.parseDouble(coords[index * 2 + 1]),
                    Double.parseDouble(coords[index * 2 + 2])
            );

        }

        return TabulatedFunctions.createTabulatedFunction(classname, points);

    }

    public static void writeTabulatedFunction(TabulatedFunction f, Writer out) throws IOException {

        StringBuilder encoded = new StringBuilder(String.valueOf(f.getPointsCount()));
        for (int index = 0; index < f.getPointsCount(); ++index) {

            encoded
                    .append(" ")
                    .append(f.getPointX(index))
                    .append(" ")
                    .append(f.getPointY(index));

        }

        out.write(encoded.toString());
        out.flush();

    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException, InappropriateFunctionPointException {

        final int BUFFER_SIZE = 1024;
        char[] buffer = new char[BUFFER_SIZE];

        in.read(buffer);
        String[] coords = String.valueOf(buffer).split(" ");

        int pointsCount = Integer.parseInt(coords[0]);
        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int index = 0; index < pointsCount; ++index) {

            points[index] = new FunctionPoint(
                Double.parseDouble(coords[index * 2 + 1]),
                Double.parseDouble(coords[index * 2 + 2])
            );

        }

        return TabulatedFunctions.createTabulatedFunction(points);

    }

    public static TabulatedFunction readTabulatedFunction(Class<? extends TabulatedFunction> classname, Reader in) throws IOException {

        final int BUFFER_SIZE = 1024;
        char[] buffer = new char[BUFFER_SIZE];

        in.read(buffer);
        String[] coords = String.valueOf(buffer).split(" ");

        int pointsCount = Integer.parseInt(coords[0]);
        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int index = 0; index < pointsCount; ++index) {

            points[index] = new FunctionPoint(
                Double.parseDouble(coords[index * 2 + 1]),
                Double.parseDouble(coords[index * 2 + 2])
            );

        }

        return TabulatedFunctions.createTabulatedFunction(classname, points);

    }

}
