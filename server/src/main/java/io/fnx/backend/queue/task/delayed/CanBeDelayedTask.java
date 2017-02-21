package io.fnx.backend.queue.task.delayed;

import io.fnx.backend.queue.task.Task;

public abstract class CanBeDelayedTask extends Task {

    /**
     * @return should return true, if this given task has ID.
     */
    public abstract boolean hasId();

    /**
     * Returns the ID of this task. Tasks with IDs can be schedule over and over
     * again, and when there is a task with same ID already scheduled, it will be overwritten
     * by the new task and the original version will be discarded.
     * <p>
     *     Use case example: notifying users, that their favourite project has changed.
     *     We want to notify them only once, so we will derive the ID of the task from the ID of the project <code>prj-changed-123</code>.
     *     If the user changes the project again, before the notification had been processed, new task will be scheduled,
     *     and the old one discarded. So users will receive only single notification, not 2 in fast succession.
     * </p>
     * @return the ID of this task, or null if <code>hasId()</code> returned <code>false</code>
     */
    public abstract String getId();

}
