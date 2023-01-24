package threads;

import functions.Functions;

import java.util.concurrent.BlockingQueue;

public class Integrator extends Thread {

    private final BlockingQueue<Task> queue;
    private final int tasksCount;

    public Integrator(BlockingQueue<Task> queue, int tasksCount) {

        this.queue = queue;
        this.tasksCount = tasksCount;

    }

    @Override
    public void run() {

        double leftX, rightX, step, result;
        for (int i = 0; i < tasksCount; ++i) {

            try {

                Task task = queue.take();
                leftX = task.getLeftX();
                rightX = task.getRightX();
                step = task.getStep();
                result = Functions.integral(task.getFunction(), leftX, rightX, step);
                System.out.printf("Result %f %f %f %f%n", leftX, rightX, step, result);

            } catch (InterruptedException e) {

                break;

            }

        }

    }

}
