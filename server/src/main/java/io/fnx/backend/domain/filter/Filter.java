package io.fnx.backend.domain.filter;

import io.fnx.backend.service.ListResult;
import com.googlecode.objectify.cmd.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Helper for simple query filtering
 */
public class Filter {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    public static final int DEFAULT_PAGE_SIZE = 20;

    private final FilterLimits params;

    public Filter(final FilterLimits params) {
        if (params == null) {
            this.params = FilterLimits.DEFAULT;
        } else {
            this.params = params;
        }
    }

    public <T> ListResult<T> result(List<T> list) {
        return new ListResult<T>(list, params.getPage());
    }

    public <T> Query<T> filterQuery(Query<T> query) {
        // noop
        return query;
    }

    public <T> Query<T> orderQuery(Query<T> query) {
        // noop
        return query;
    }

    public <T> Query<T> limitQuery(Query<T> query) {
        return limitQuery(params, query);
    }

    public static <T> Query<T> limitQuery(FilterLimits params, Query<T> query) {
        if (params.isAllPages()) {
            return query;
        } else {
            return query.offset(params.getOffset()).limit(params.getPageSize());
        }
    }

    public <T> Query<T> query(Query<T> query) {
        final Query<T> filtered = filterQuery(query);
        final Query<T> limited = limitQuery(filtered);
        Query<T> result = orderQuery(limited);
        log.info("Filtering: "+result.toString());
        return result;
    }

    public int getPage() {
        return params.getPage();
    }

    public int getPageSize() {
        return params.getPageSize();
    }

    public FilterLimits getParams() {
        return params;
    }
}
