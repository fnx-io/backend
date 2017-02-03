package com.backend.queue.task.delayed;

import com.backend.domain.DelayedTaskEntity;
import com.backend.queue.task.Task;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import org.joda.time.DateTime;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.String.format;

public class CreateDelayedTask extends Task {

    public final static String URL_TEMPLATE = "/delayed?";

    private final DateTime delayUntil;
    private final CanBeDelayedTask task;
    private final boolean archive;

    protected final Escaper escaper = UrlEscapers.urlFormParameterEscaper();

    public CreateDelayedTask(CanBeDelayedTask task, DateTime delayUntil) {
        this(task, delayUntil, DelayedTaskEntity.DEFAULT_ARCHIVE);
    }

    public CreateDelayedTask(CanBeDelayedTask task, DateTime delayUntil, boolean archive) {
        this.task = task;
        this.delayUntil = delayUntil;
        this.archive = archive;
    }

    @Override
    public String taskUrl() {
        StringBuilder url = new StringBuilder(URL_TEMPLATE);
        final String taskUrl = task.taskUrl();
        if (!isNullOrEmpty(taskUrl)) {
            url.append("taskUrl=").append(escaper.escape(taskUrl)).append("&");
        }
        if (delayUntil != null) {
            url.append("delayUntil=").append(delayUntil.getMillis()).append("&");
        }
        if (task.hasId() && !isNullOrEmpty(task.getId())) {
            url.append("taskId=").append(escaper.escape(task.getId())).append("&");
        } else if (task.hasId() && isNullOrEmpty(task.getId())) {
            throw new IllegalStateException(format("Task to delayed [%s] should have ID, but has not!", task));
        }
        url.append("archive=").append(archive).append("&");

        return url.toString();
    }
}
