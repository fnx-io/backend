package io.fnx.backend.rest;

import io.fnx.backend.domain.eventlog.AuditLogEventEntity;
import io.fnx.backend.service.AuditLogManager;
import io.fnx.backend.service.ListResult;
import io.fnx.backend.service.filter.AuditLogEventFilter;

import javax.inject.Inject;
import javax.ws.rs.*;

/**
 * REST endpoints for audit log events.
 */
@Path("/v1/audit-log/events")
public class AuditLogEventResource extends BaseResource {

    private final AuditLogManager auditLogManager;

	@Inject
	public AuditLogEventResource(AuditLogManager auditLogManager) {
		this.auditLogManager = auditLogManager;
	}

	@GET
	@Path("/{id}")
	public AuditLogEventEntity findById(@PathParam("id") Long id) {
		return auditLogManager.findById(id);
	}
	
	@GET
	public ListResult<AuditLogEventEntity> list(@QueryParam("eventTargetId") Long eventTargetId) {
		return auditLogManager.listAuditLogEvents(new AuditLogEventFilter(eventTargetId, filterLimits()));
	}

}
