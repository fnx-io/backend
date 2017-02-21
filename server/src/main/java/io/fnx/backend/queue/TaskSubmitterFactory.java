package io.fnx.backend.queue;

import io.fnx.backend.util.Constants;

import javax.inject.Inject;

public class TaskSubmitterFactory {

    private final TaskSubmitter system;

    @Inject
    public TaskSubmitterFactory(QueueProviderFactory queueFactory) {
        system = createTaskSubmitterForQueue(queueFactory.create(Constants.QUEUE_SYSTEM));
    }

    public TaskSubmitter getSystem() {
        return system;
    }

    private TaskSubmitter createTaskSubmitterForQueue(QueueProvider queueProvider) {
        return new TaskSubmitter(queueProvider.get());
    }
}
