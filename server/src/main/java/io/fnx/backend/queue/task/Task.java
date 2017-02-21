package io.fnx.backend.queue.task;

import io.fnx.backend.util.Constants;

public abstract class Task {

    public String taskRoot() {
        return Constants.TASK_ROOT;
    }

    public abstract String taskUrl();
}
