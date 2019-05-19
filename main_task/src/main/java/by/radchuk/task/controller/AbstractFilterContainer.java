package by.radchuk.task.controller;

import java.util.Collection;

public interface AbstractFilterContainer<T> {
    void addFilter(Class<T> filter) throws ControllerException;
    /**
     * Called after adding all filters.
     * Could be used to sort filters in container(eg by priority)
     */
    void init();
    Collection<T> getFilters(String requestUri);
}
