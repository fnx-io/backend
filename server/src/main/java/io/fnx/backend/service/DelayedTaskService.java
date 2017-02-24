package io.fnx.backend.service;

import io.fnx.backend.domain.DelayedTaskEntity;
import org.joda.time.DateTime;

import java.util.List;

public interface DelayedTaskService {

    DelayedTaskEntity createDelayedTask(String id, DateTime delayedUntil, String taskUrl, boolean archive);

    List<DelayedTaskEntity> scheduledDelayedTasksProcessing(DateTime until);

    DelayedTaskEntity process(String id);
}