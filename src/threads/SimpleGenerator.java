package threads;

import functions.Function;
import functions.basic.Log;

import java.util.Random;

public class SimpleGenerator implements Runnable {

    private final SimpleTask task;

    public SimpleGenerator(SimpleTask task) {
        this.task = task;
    }

    @Override
    public void run() {

        Function function;
        double leftX, rightX, step;

        for (int i = 0; i < task.getTasksCount(); ++i) {

            function = new Log(new Random().nextDouble(9.0) + 1.0);
            leftX = new Random().nextDouble(100.0);
            rightX = new Random().nextDouble(100.0) + 100.0;
            step = new Random().nextDouble(1.0);

            synchronized (task) {

                task.setFunction(function);
                task.setLeftX(leftX);
                task.setRightX(rightX);
                task.setStep(step);
                task.setReady(true);

            }

            System.out.printf("Source %f %f %f%n", leftX, rightX, step);

        }

    }

}
