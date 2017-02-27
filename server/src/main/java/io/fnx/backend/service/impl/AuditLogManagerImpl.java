package io.fnx.backend.service.impl;

import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.Query;
import io.fnx.backend.domain.UserEntity;
import io.fnx.backend.domain.eventlog.AuditLogEventEntity;
import io.fnx.backend.service.AuditLogManager;
import io.fnx.backend.service.BaseService;
import io.fnx.backend.service.ListResult;
import io.fnx.backend.service.filter.AuditLogEventFilter;
import org.joda.time.DateTime;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class AuditLogManagerImpl extends BaseService implements AuditLogManager {

    @Override
    public AuditLogEventEntity createAuditLogEvent(Key eventTarget, String eventMessage) {
        checkArgument(eventTarget != null, "Target of event to log must not be empty!");
        checkArgument(!Strings.isNullOrEmpty(eventMessage), "Event message must not be empty!");

        final Key<AuditLogEventEntity> auditLogEventKey = ofy().factory().allocateId(AuditLogEventEntity.class);
        final AuditLogEventEntity event = new AuditLogEventEntity();
        event.setId(auditLogEventKey.getId());
        event.setEventTarget(eventTarget);
        event.setMessage(eventMessage);
        final UserEntity user = cc().getLoggedUser();
        if (user == null) {
            // TODO: anonynomous user
        } else {
            event.setChangedBy(user.getKey());
        }
        event.setOccuredOn(DateTime.now());

        return ofy().transact(new Work<AuditLogEventEntity>() {
            @Override
            public AuditLogEventEntity run() {
                ofy().save().entity(event).now(); // TODO: async save?
                return event;
            }
        });
    }

    @Override
    public ListResult<AuditLogEventEntity> listAuditLogEvents(final AuditLogEventFilter filter) {
        final Query<AuditLogEventEntity> query = ofy().load().type(AuditLogEventEntity.class);
        final List<AuditLogEventEntity> result = filter.query(query).list();
        return filter.result(result);
    }

}
