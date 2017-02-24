package io.fnx.backend.service.filter;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import io.fnx.backend.domain.filter.Filter;
import io.fnx.backend.domain.filter.FilterLimits;

public class AuditLogEventFilter extends Filter {

    public final Key eventTarget;

    public AuditLogEventFilter(Key eventTarget, FilterLimits params) {
        super(params);
        this.eventTarget = eventTarget;
    }

    public AuditLogEventFilter(Long eventTargetId, FilterLimits params) {
        super(params);
        this.eventTarget = null; //TODO kurva
    }

    @Override
    public <T> Query<T> filterQuery(Query<T> query) {
        if (eventTarget != null) {
            query = query.filter("eventTarget", eventTarget);
        }
        return query;
    }

    @Override
    public <T> Query<T> orderQuery(Query<T> query) {
        return query.order("-occuredOn");
    }

}
