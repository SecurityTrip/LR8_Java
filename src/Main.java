import documents.TabulatedFunctionDoc;
import functions.*;
import functions.basic.Cos;
import functions.basic.Exp;
import functions.basic.Log;
import functions.basic.Sin;
import threads.*;

import java.io.*;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    public static void main(String[] args) throws InappropriateFunctionPointException {

        test();

    }

    public static void test() {
        //
        TabulatedFunction f = null;
        try {
            f = new LinkedListTabulatedFunction(-10.0, 10.0, 5);
        } catch (InappropriateFunctionPointException e) {
            throw new RuntimeException(e);
        }
        for (FunctionPoint p : f) {
            System.out.println(p);
        }

        //
        Function function = new Cos();
        TabulatedFunction tf = null;
        try {
            tf = TabulatedFunctions.tabulate(function, 0, Math.PI, 11);
        } catch (InappropriateFunctionPointException e) {
            throw new RuntimeException(e);
        }
        System.out.println(tf.getClass());

        TabulatedFunctions.setTabulatedFunctionFactory(new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        try {
            tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        } catch (InappropriateFunctionPointException e) {
            throw new RuntimeException(e);
        }
        System.out.println(tf.getClass());

        TabulatedFunctions.setTabulatedFunctionFactory(new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        try {
            tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        } catch (InappropriateFunctionPointException e) {
            throw new RuntimeException(e);
        }
        System.out.println(tf.getClass());

        //
        TabulatedFunction func = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class,
                0.0, 10.0, 3
        );
        System.out.println(func.getClass());
        System.out.println(func);

        func = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class,
                0.0, 10.0, new double[] { 0.0, 10.0 }
        );
        System.out.println(func.getClass());
        System.out.println(func);

        func = TabulatedFunctions.createTabulatedFunction(
                LinkedListTabulatedFunction.class,
                new FunctionPoint[] {
                        new FunctionPoint(0.0, 0.0),
                        new FunctionPoint(10.0, 10.0)
                }
        );
        System.out.println(func.getClass());
        System.out.println(func);

        func = TabulatedFunctions.tabulate(
                LinkedListTabulatedFunction.class,
                new Sin(), 0, Math.PI, 11
        );
        System.out.println(func.getClass());
        System.out.println(func);
    }

    public static void complicatedThreads() throws InterruptedException {

        final int TASKS_COUNT = 200;
        BlockingQueue<Task> queue = new LinkedBlockingQueue<>(1);

        Thread generator = new Generator(queue, TASKS_COUNT);
        Thread integrator = new Integrator(queue, TASKS_COUNT);

        generator.start();
        integrator.start();

        Thread.sleep(50);
        generator.interrupt();
        integrator.interrupt();

    }

    public static void simpleThreads() {

        SimpleTask task = new SimpleTask();
        task.setTasksCount(100);

        Thread generator = new Thread(new SimpleGenerator(task));
        Thread integrator = new Thread(new SimpleIntegrator(task));

        generator.start();
        integrator.start();

    }

    public static void nonThread() throws InterruptedException {

        SimpleTask task = new SimpleTask();
        task.setTasksCount(100);

        for (int i = 0; i < task.getTasksCount(); ++i) {

            task.setFunction(new Log(new Random().nextDouble(9.0) + 1.0));
            task.setLeftX(new Random().nextDouble(100.0));
            task.setRightX(new Random().nextDouble(100.0) + 100.0);
            task.setStep(new Random().nextDouble(1.0));
            System.out.printf("Source %f %f %f%n", task.getLeftX(), task.getRightX(), task.getStep());

            double result = Functions.integral(task.getFunction(),  task.getLeftX(), task.getRightX(), task.getStep());
            System.out.printf("Result %f %f %f %f%n", task.getLeftX(), task.getRightX(), task.getStep(), result);

        }

    }

    public static void printIntegral() {

        // различается в 7 знаке после запятой
        System.out.println(Functions.integral(new Exp(), 0.0, 1.0, 0.001));

    }

    public static void printDoc() throws InappropriateFunctionPointException {

        TabulatedFunctionDoc tfd = new TabulatedFunctionDoc();
        tfd.tabulateFunction(new Exp(), -1.0, 1.0, 3);
        System.out.println(tfd.isModified());
        System.out.println(tfd.isFileNameAssigned());
        tfd.saveFunctionAs("exp.json");
        System.out.println(tfd.isModified());
        System.out.println(tfd.isFileNameAssigned());
        tfd.loadFunction("exp.json");
        System.out.println(tfd.isModified());
        System.out.println(tfd.isFileNameAssigned());
        System.out.println(tfd.getFunctionValue(0.0));

    }

    public static void printTrigonometric() throws InappropriateFunctionPointException {

        Function sin = new Sin();
        Function cos = new Cos();

        TabulatedFunction tabSin = TabulatedFunctions.tabulate(sin, 0, 2 * Math.PI, 2000);
        TabulatedFunction tabCos = TabulatedFunctions.tabulate(cos, 0, 2 * Math.PI, 2000);

        Function one = Functions.sum(Functions.power(tabSin, 2), Functions.power(tabCos, 2));

        for (double x = 0.0; x <= 2 * Math.PI; x += 0.1) {

            System.out.println("Sin & Cos: " + sin.getFunctionValue(x) + ", " + cos.getFunctionValue(x));
            System.out.println("Tab Sin & Tab Cos: " + tabSin.getFunctionValue(x) + ", " + tabCos.getFunctionValue(x));
            System.out.println("Tab Sin ^ 2 + Tab Cos ^ 2: " + one.getFunctionValue(x));
            System.out.println();

        }

        System.out.println();

    }

    public static void printExponent() throws InappropriateFunctionPointException, IOException {

        final String FILE_NAME = "exp.txt";
        TabulatedFunction tabExp = TabulatedFunctions.tabulate(new Exp(), 0.0, 10.0, 11);

        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            TabulatedFunctions.writeTabulatedFunction(tabExp, writer);
        }

        TabulatedFunction tabExpRead;
        try (FileReader reader = new FileReader(FILE_NAME)) {
            tabExpRead = TabulatedFunctions.readTabulatedFunction(reader);
        }

        for (double x = 0.0; x <= 10.0; x += 1.0)
            System.out.println("Tab Exp & Tab Exp Read: " + tabExp.getFunctionValue(x) + " " + tabExpRead.getFunctionValue(x));
        System.out.println();

    }

    public static void printLogarithm() throws InappropriateFunctionPointException, IOException, ClassNotFoundException {

        final String TXT_FILE_NAME = "log.txt";
        final String OBJ_FILE_NAME = "log.obj";

        TabulatedFunction tabLog = TabulatedFunctions.tabulate(new Log(Math.E), 0.0, 10.0, 11);

        try (FileOutputStream os = new FileOutputStream(TXT_FILE_NAME)) {
            TabulatedFunctions.outputTabulatedFunction(tabLog, os);
        }

        TabulatedFunction tabLogRead;
        try (FileInputStream is = new FileInputStream(TXT_FILE_NAME)) {
            tabLogRead = TabulatedFunctions.inputTabulatedFunction(is);
        }

        for (double x = 0.0; x <= 10.0; x += 1.0)
            System.out.println("Tab Log & Tab Log Read: " + tabLog.getFunctionValue(x) + " " + tabLogRead.getFunctionValue(x));
        System.out.println();

        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(OBJ_FILE_NAME))) {

            os.writeObject(tabLog);
            os.flush();

        }

        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(OBJ_FILE_NAME))) {
            tabLogRead = (TabulatedFunction) is.readObject();
        }

        for (double x = 0.0; x <= 10.0; x += 1.0)
            System.out.println("Tab Log & Tab Log Read Serial: " + tabLog.getFunctionValue(x) + " " + tabLogRead.getFunctionValue(x));
        System.out.println();

    }

    public static void printService() throws InappropriateFunctionPointException, CloneNotSupportedException {

        TabulatedFunction tabSin = TabulatedFunctions.tabulate(new Sin(), 0, 2 * Math.PI, 2000);
        System.out.println(tabSin);
        System.out.println();

        TabulatedFunction tabSin2 = TabulatedFunctions.tabulate(new Sin(), 0, 2 * Math.PI, 2000);
        System.out.println("Equals: " + (tabSin.equals(tabSin2) ? "yes" : "no"));

        tabSin2.setPointY(0, tabSin.getPointY(0) + 0.001);
        System.out.println("Equals: " + (tabSin.equals(tabSin2) ? "yes" : "no"));

        System.out.println();
        System.out.println("Tab Sin 2 Point Hash: " + tabSin.getPoint(1).hashCode());
        System.out.println("Tab Sin Hash: " + tabSin.hashCode());
        System.out.println("Tab Sin 2 Hash: " + tabSin2.hashCode());
        System.out.println();

        TabulatedFunction tabSin3 = (TabulatedFunction) tabSin.clone();
        System.out.println(tabSin);
        System.out.println(tabSin3);
        System.out.println();
        tabSin.setPointY(0, 100.0);
        System.out.println(tabSin);
        System.out.println(tabSin3);

    }

}
