package io.fnx.backend.domain.eventlog;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.*;
import io.fnx.backend.domain.UserEntity;
import org.joda.time.DateTime;

import static io.fnx.backend.tools.ofy.OfyUtils.idToKey;

@Entity
@Unindex
public class AuditLogEventEntity {

    @Id
    private Long id;

    @Index
    private Key eventTarget;
    
    private String message;

    @Index
    private DateTime occurredOn;

    private Key<UserEntity> changedBy;

    @Ignore
    private String changedByName;

    public static Key<AuditLogEventEntity> createKey(Long id) {
        return idToKey(AuditLogEventEntity.class, id);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Key getEventTarget() {
        return eventTarget;
    }

    public void setEventTarget(Key eventTarget) {
        this.eventTarget = eventTarget;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DateTime getOccurredOn() {
        return occurredOn;
    }

    public void setOccurredOn(DateTime occurredOn) {
        this.occurredOn = occurredOn;
    }

    public Key<UserEntity> getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(Key<UserEntity> changedBy) {
        this.changedBy = changedBy;
    }

    public String getChangedByName() {
        return changedByName;
    }

    public void setChangedByName(String changedByName) {
        this.changedByName = changedByName;
    }

}
