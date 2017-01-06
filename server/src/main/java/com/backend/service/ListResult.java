package com.backend.service;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Holder for collection of results.
 * Provides page information
 */
public class ListResult<T> implements Iterable<T> {

    private final List<T> data;
    private final int page;

    public ListResult(List<T> data, int page) {
        if (data == null) data = Lists.newLinkedList();
        this.data = data;
        this.page = page;
    }

    public ListResult(Collection<T> data, int page) {
        if (data == null) data = Lists.newLinkedList();
        this.data = Lists.newArrayList(data);
        this.page = page;
    }

    public List<T> getData() {
        return data;
    }

    public int getPage() {
        return page;
    }

    public static <T> ListResult<T> empty() {
        return new ListResult<T>(Lists.<T>newLinkedList(), 0);
    }

    @Override
    public Iterator<T> iterator() {
        return data.iterator();
    }
}
