package com.backend.queue;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Provider which can create Queues of given name;
 * <br/>
 *
 * For easier creation of these provider, let Guice inject {@link QueueProviderFactory} and then use
 * <code>queueProviderFactory.create("myQueue")</code>
 */
public class QueueProvider implements Provider<Queue> {

    private final String queueName;

    @Inject
    public QueueProvider(@Assisted String queueName) {
        this.queueName = queueName;
    }

    @Override
    public Queue get() {
        return QueueFactory.getQueue(queueName);
    }
}
