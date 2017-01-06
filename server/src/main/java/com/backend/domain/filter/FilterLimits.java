package com.backend.domain.filter;

import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

public class FilterLimits {
        private final int page;
        private final int pageSize;
        private final boolean allPages;

        public static final FilterLimits DEFAULT = new FilterLimits(0, false);
        public static final FilterLimits ALL = new FilterLimits(0, true);

        public FilterLimits(final int page, final int pageSize, final boolean allPages) {
            this.page = page;
            this.pageSize = pageSize;
            this.allPages = allPages;
        }

        public FilterLimits(final int page, final boolean allPages) {
            this(page, Filter.DEFAULT_PAGE_SIZE, allPages);
        }

        /**
         * @return which page to start at (we start always at 0 when filter needs to list all pages)
         */
        public int getPage() {
            if (allPages) {
                return 0;
            } else {
                return page;
            }
        }

        public FilterLimits withPageSize(int newPageSize) {
            Preconditions.checkArgument(newPageSize > 0, format("Invalid pagesize: %d", newPageSize));
            return new FilterLimits(page, newPageSize, allPages);
        }

        public int getPageSize() {
            return pageSize;
        }

        public boolean isAllPages() {
            return allPages;
        }

        public int getOffset() {
            return getPage() * pageSize;
        }

        @Override
        public String toString() {
            return "FilterLimits{" +
                    "page=" + getPage() +
                    ", pageSize=" + pageSize +
                    ", allPages=" + allPages +
                    '}';
        }

        public <T> List<T> limitCollection(List<T> list) {
            if (list == null) return Collections.emptyList();
            final int start = getOffset();
            if (start >= list.size()) return Collections.emptyList();
            final int maxCount = list.size() - start;
            final int count = Math.min(maxCount, pageSize);
            if (count < 1) return Collections.emptyList();
            return list.subList(start, count);
        }
    }
