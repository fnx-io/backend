package com.backend.service.impl;

import com.backend.auth.AllowedForTrusted;
import com.backend.domain.DelayedTaskEntity;
import com.backend.queue.TaskSubmitter;
import com.backend.queue.TaskSubmitterFactory;
import com.backend.queue.task.delayed.ProcessDelayedTask;
import com.backend.queue.task.delayed.RunWrappedTask;
import com.backend.service.BaseService;
import com.backend.service.DelayedTaskService;
import com.google.common.base.Preconditions;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import io.fnx.backend.tools.authorization.AllowedForAdmins;
import io.fnx.backend.tools.random.Randomizer;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.String.format;

public class DelayedTaskServiceImpl extends BaseService implements DelayedTaskService {

    private static final Logger log = LoggerFactory.getLogger(DelayedTaskServiceImpl.class);

    private Randomizer randomizer;

    private TaskSubmitter systemSubmitter;

    @Override
    @AllowedForAdmins
    @AllowedForTrusted
    public DelayedTaskEntity createDelayedTask(String id, DateTime delayedUntil, final String taskUrl, final boolean archive) {
        Preconditions.checkArgument(!isNullOrEmpty(taskUrl), "Task url must not be empty!");
        if (delayedUntil == null) delayedUntil = DateTime.now();
        if (isNullOrEmpty(id)) id = randomizer.randomBase64(40);
        final Key<DelayedTaskEntity> taskKey = DelayedTaskEntity.createKey(id);
        final String taskId = id;
        final DateTime taskDelayedUntil = delayedUntil;
        return ofy().transact(new Work<DelayedTaskEntity>() {
            @Override
            public DelayedTaskEntity run() {
                final DelayedTaskEntity existingTask = ofy().load().key(taskKey).now();
                if (existingTask != null && existingTask.isProcessed()) {
                    log.info("Task [{}] exists and is already processed [{}]", taskId, existingTask.getProcessed().toString());
                    return existingTask;
                }

                final DelayedTaskEntity task = new DelayedTaskEntity();
                task.setId(taskId);
                task.setArchive(archive);
                task.setEventTime(DateTime.now());
                task.setDelayUntil(taskDelayedUntil);
                task.setTask(taskUrl);

                ofy().save().entity(task).now();
                return task;
            }
        });
    }

    @Override
    @AllowedForAdmins
    @AllowedForTrusted
    public List<DelayedTaskEntity> scheduledDelayedTasksProcessing(DateTime until) {
        if (until == null) until = DateTime.now();
        final List<DelayedTaskEntity> all = ofy().load().type(DelayedTaskEntity.class).filter("processed", null).filter("delayUntil <=", until).list();
        for (DelayedTaskEntity task : all) {
            systemSubmitter.submit(new ProcessDelayedTask(task.getId()));
        }

        return all;
    }

    @Override
    @AllowedForAdmins
    @AllowedForTrusted
    public DelayedTaskEntity process(final String id) {
        final Key<DelayedTaskEntity> taskKey = DelayedTaskEntity.createKey(id);

        return ofy().transact(new Work<DelayedTaskEntity>() {
            @Override
            public DelayedTaskEntity run() {
                final DelayedTaskEntity task = ofy().load().key(taskKey).now();
                if (task == null) return null;
                if (task.isProcessed()) {
                    log.info("Delayed task {} is already processed", id);
                    return task;
                }
                task.setProcessed(DateTime.now());

                ofy().save().entity(task).now();
                if (task.isArchive()) {
                    // change current task ID (and key)
                    task.setId(format("%s-ARCHIVED-%s-%s", id, task.getProcessed(), randomizer.randomBase64(15)));
                    // remove the old key to allow it to be reused and save "new" entity with archived key
                    ofy().delete().key(DelayedTaskEntity.createKey(id));
                    ofy().save().entity(task).now();
                }
                systemSubmitter.submit(new RunWrappedTask(task.getTask()));
                return task;
            }
        });
    }

    @Inject
    public void setRandomizer(Randomizer randomizer) {
        this.randomizer = randomizer;
    }

    @Inject
    public void setTaskQueueSubmitterFactory(TaskSubmitterFactory fac) {
        systemSubmitter = fac.getSystem();
    }
}
