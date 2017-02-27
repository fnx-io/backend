package io.fnx.backend.service.filter;

import io.fnx.backend.domain.FileCategory;
import io.fnx.backend.domain.filter.Filter;
import io.fnx.backend.domain.filter.FilterLimits;
import com.googlecode.objectify.cmd.Query;

public class ListFilesFilter extends Filter {

    public final FileCategory category;
	public final String set;

    public ListFilesFilter(FileCategory category, String set, FilterLimits limits) {
        super(limits);
        this.category = category;
        this.set = set;
    }

    @Override
    public <T> Query<T> filterQuery(Query<T> query) {
        if (category != null) {
            query = query.filter("category", category);
        }
	    if (set != null) {
		    query = query.filter("set", set);
	    }
        return query;
    }

    @Override
    public <T> Query<T> orderQuery(Query<T> query) {
        return query.order("-uploaded");
    }
}
