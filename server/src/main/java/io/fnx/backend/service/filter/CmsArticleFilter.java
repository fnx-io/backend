package io.fnx.backend.service.filter;

import com.googlecode.objectify.cmd.Query;
import io.fnx.backend.domain.filter.Filter;
import io.fnx.backend.domain.filter.FilterLimits;

public class CmsArticleFilter extends Filter {

    public final String type;

    public CmsArticleFilter(String type, FilterLimits limits) {
        super(limits);
        this.type = type;
    }

    @Override
    public <T> Query<T> filterQuery(Query<T> query) {
        if (type != null) {
            query = query.filter("type", type);
        }
        return query;
    }

    @Override
    public <T> Query<T> orderQuery(Query<T> query) {
        return query.order("-created");
    }
}
