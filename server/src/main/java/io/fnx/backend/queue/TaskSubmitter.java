package io.fnx.backend.queue;

import io.fnx.backend.queue.task.Task;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.common.base.Preconditions;
import org.jboss.logging.Logger;

import static java.lang.String.format;

/**
 * Helps with submitting tasks into queues
 */
public class TaskSubmitter {

    private static final Logger log = Logger.getLogger(TaskSubmitter.class);

    private final Queue queue;

    public TaskSubmitter(Queue queue) {
        Preconditions.checkNotNull(queue, "Queue must not be null!");
        this.queue = queue;
    }

    /**
     * Given a task, will construct the url to be queued in current queue
     * @param task the task to submit in current queue
     * @return the handle for submitted task
     */
    public TaskHandle submit(Task task) {
        String jobUrl = format("%s%s", task.taskRoot(), task.taskUrl());
        log.info("Scheduling task " + jobUrl + " into " + queue.getQueueName());
        final TaskOptions opts = TaskOptions.Builder.withUrl(jobUrl);
        return queue.add(opts.method(TaskOptions.Method.GET));
    }
}
