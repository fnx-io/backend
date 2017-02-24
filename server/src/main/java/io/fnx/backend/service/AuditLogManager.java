package io.fnx.backend.service;

import com.googlecode.objectify.Key;
import io.fnx.backend.domain.eventlog.AuditLogEventEntity;
import io.fnx.backend.service.filter.AuditLogEventFilter;

public interface AuditLogManager {

    AuditLogEventEntity createAuditLogEvent(Key eventTarget, String eventMessage);

    AuditLogEventEntity findById(Long id);

    ListResult<AuditLogEventEntity> listAuditLogEvents(AuditLogEventFilter filter);

}
