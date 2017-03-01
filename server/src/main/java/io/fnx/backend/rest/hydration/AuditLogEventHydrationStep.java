package io.fnx.backend.rest.hydration;

import com.googlecode.objectify.Key;
import io.fnx.backend.auth.CallContext;
import io.fnx.backend.domain.UserEntity;
import io.fnx.backend.domain.eventlog.AuditLogEventEntity;
import io.fnx.backend.tools.hydration.HydrationRecipeStep;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AuditLogEventHydrationStep implements HydrationRecipeStep<AuditLogEventEntity, CallContext> {

    @Override
    public List<Key<?>> getDependencies(AuditLogEventEntity auditLogEventEntity, CallContext callContext) {
        List<Key<?>> keys = new ArrayList<>();
        keys.add(auditLogEventEntity.getChangedBy());
        return keys;

    }

    @Override
    public void executeStep(AuditLogEventEntity auditLogEventEntity, CallContext callContext, Map<Key<Object>, Object> map) {
        UserEntity changedBy = (UserEntity) map.get(auditLogEventEntity.getChangedBy());
        auditLogEventEntity.setChangedByName(changedBy.getName());
    }

}
