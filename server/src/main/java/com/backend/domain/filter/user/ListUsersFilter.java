package com.backend.domain.filter.user;

import com.backend.domain.filter.Filter;
import com.backend.domain.filter.FilterLimits;
import com.googlecode.objectify.cmd.Query;

public class ListUsersFilter extends Filter {

    public ListUsersFilter(FilterLimits params) {
        super(params);
    }

    @Override
    public <T> Query<T> orderQuery(Query<T> query) {
        return query.order("-name");
    }
}
