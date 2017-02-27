package io.fnx.backend.domain.eventlog;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;
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
    private DateTime occuredOn;

    private Key<UserEntity> changedBy;


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

    public DateTime getOccuredOn() {
        return occuredOn;
    }

    public void setOccuredOn(DateTime occuredOn) {
        this.occuredOn = occuredOn;
    }

    public Key<UserEntity> getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(Key<UserEntity> changedBy) {
        this.changedBy = changedBy;
    }

}
