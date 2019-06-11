package org.api.script.framework.worker;


public abstract class WorkerHandler {

    private Worker currentWorker;

    /**
     * Handles the flow of the mission to decide what worker to execute.
     *
     * @return The worker to execute.
     */
    public abstract Worker decide();

    /**
     * Handles the execution of the workers and repetition.
     */
    public void work() {
        currentWorker = decide();
        if (currentWorker == null)
            return;

        currentWorker.work();
    }

    /**
     * Gets the current worker.
     *
     * @return The current worker.
     */
    public Worker getCurrent() {
        return currentWorker;
    }

}

