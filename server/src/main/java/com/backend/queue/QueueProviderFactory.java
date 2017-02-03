package com.backend.queue;

/**
 * Factory which assists in creating <code>Provider&lt;Queue&gt;</code>
 */
public interface QueueProviderFactory {

    QueueProvider create(String queueName);
}
