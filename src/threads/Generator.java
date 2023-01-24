package threads;

import functions.Function;
import functions.basic.Log;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Generator extends Thread {

    private final BlockingQueue<Task> queue;
    private final int tasksCount;

    public Generator(BlockingQueue<Task> queue, int tasksCount) {

        this.queue = queue;
        this.tasksCount = tasksCount;

    }

    @Override
    public void run() {

        Function function;
        double leftX, rightX, step;

        for (int i = 0; i < tasksCount; ++i) {

            function = new Log(new Random().nextDouble(9.0) + 1.0);
            leftX = new Random().nextDouble(100.0);
            rightX = new Random().nextDouble(100.0) + 100.0;
            step = new Random().nextDouble(1.0);

            try {

                queue.put(new Task(function, leftX, rightX, step));
                System.out.printf("Source %f %f %f%n", leftX, rightX, step);

            } catch (InterruptedException e) {

                break;

            }

        }

    }

}
