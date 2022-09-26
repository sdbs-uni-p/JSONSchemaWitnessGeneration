package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.MassiveTesting;

import java.util.concurrent.*;

public class TaskManager implements Callable<GenerateWitnessTaskV4> {

    protected long timeout;
    protected TimeUnit timeUnit;
    protected GenerateWitnessTaskV4 task;

    public TaskManager(long timeout, TimeUnit timeUnit, GenerateWitnessTaskV4 task) {
        this.timeout=timeout;
        this.timeUnit=timeUnit;
        this.task=task;
    }

    @Override
    public GenerateWitnessTaskV4 call() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        GenerateWitnessTaskV4 resTask;
        try {
            // Execution of the task without a timeout
            if (timeout==0)
                executor.submit(task).get();
            else
                executor.submit(task).get(timeout, timeUnit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            task.getResultMap().put(task.getCurrentOperation(),e.getClass().getSimpleName());
            task.hasException = true;
            Utils.addExceptionInformation(task.id,e,task.exceptionMap,task.currentOperation);
            task.getResultMap().put(Utils.cancelledAt,task.currentOperation);

        }
        resTask = task;
        executor.shutdown();
        return resTask;
    }
}
