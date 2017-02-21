package io.fnx.backend.domain;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;
import io.fnx.backend.tools.ofy.OfyUtils;
import org.joda.time.DateTime;

@Entity
@Unindex
public class DelayedTaskEntity {

    public final static boolean DEFAULT_ARCHIVE = false;

    @Id
    private String id;

    private String task;

    @Index
    private DateTime eventTime;
    @Index
    private DateTime delayUntil;
    @Index
    private DateTime processed;
    private boolean archive;

    public DelayedTaskEntity() {
        this.archive = DEFAULT_ARCHIVE;
    }

    public Key<DelayedTaskEntity> getKey() {
        return createKey(id);
    }

    public static Key<DelayedTaskEntity> createKey(String name) {
        return OfyUtils.nameToKey(DelayedTaskEntity.class, name);
    }

    public boolean isProcessed() {
        return processed != null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public DateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(DateTime eventTime) {
        this.eventTime = eventTime;
    }

    public DateTime getDelayUntil() {
        return delayUntil;
    }

    public void setDelayUntil(DateTime delayUntil) {
        this.delayUntil = delayUntil;
    }

    public DateTime getProcessed() {
        return processed;
    }

    public void setProcessed(DateTime processed) {
        this.processed = processed;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public boolean isArchive() {
        return archive;
    }
}
