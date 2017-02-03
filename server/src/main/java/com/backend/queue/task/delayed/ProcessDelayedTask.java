package com.backend.queue.task.delayed;

import com.backend.queue.task.Task;

import static java.lang.String.format;

public class ProcessDelayedTask extends Task {

    public static final String URL_TEMPLATE = "/delayed/%s/process";

    public final String taskId;

    public ProcessDelayedTask(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String taskUrl() {
        return format(URL_TEMPLATE, taskId);
    }
}
