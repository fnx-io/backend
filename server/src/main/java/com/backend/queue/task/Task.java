package com.backend.queue.task;

import com.backend.util.Constants;

public abstract class Task {

    public String taskRoot() {
        return Constants.TASK_ROOT;
    }

    public abstract String taskUrl();
}
