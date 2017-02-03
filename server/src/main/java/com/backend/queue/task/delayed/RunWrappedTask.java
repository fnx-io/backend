package com.backend.queue.task.delayed;

import com.backend.queue.task.Task;

public class RunWrappedTask extends Task {

    private final String taskUrl;

    public RunWrappedTask(String taskUrl) {
        this.taskUrl = taskUrl;
    }

    @Override
    public String taskUrl() {
        return taskUrl;
    }
}
