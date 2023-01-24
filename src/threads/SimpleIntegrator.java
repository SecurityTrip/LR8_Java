package threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable {

    private final SimpleTask task;

    public SimpleIntegrator(SimpleTask task) {
        this.task = task;
    }

    @Override
    public void run() {

        double leftX, rightX, step, result;
        for (int i = 0; i < task.getTasksCount(); ++i) {

            while (!task.isReady()) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            synchronized (task) {

                leftX = task.getLeftX();
                rightX = task.getRightX();
                step = task.getStep();
                result = Functions.integral(task.getFunction(), leftX, rightX, step);
                task.setReady(false);

            }

            System.out.printf("Result %f %f %f %f%n", leftX, rightX, step, result);

        }

    }

}
