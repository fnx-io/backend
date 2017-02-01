package com.backend.service.filter;

import com.backend.domain.FileCategory;
import com.backend.domain.filter.Filter;
import com.backend.domain.filter.FilterLimits;
import com.googlecode.objectify.cmd.Query;

public class ListFilesFilter extends Filter {

    public final FileCategory category;

    public ListFilesFilter(FileCategory category, FilterLimits limits) {
        super(limits);
        this.category = category;
    }

    @Override
    public <T> Query<T> filterQuery(Query<T> query) {
        if (category != null) {
            query = query.filter("category", category);
        }
        return query;
    }

    @Override
    public <T> Query<T> orderQuery(Query<T> query) {
        return query.order("-uploaded");
    }
}
