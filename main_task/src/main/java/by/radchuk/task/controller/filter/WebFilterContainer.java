package by.radchuk.task.controller.filter;

import by.radchuk.task.controller.ControllerException;

import java.util.Collection;

public interface WebFilterContainer<T> {
    /**
     * Adds <b>annotated</b> filter to filter container.
     * @param filter filter to add.
     */
    void addFilter(T filter) throws ControllerException;
    /**
     * Adds <b>NOT annotated</b> filter to filter container.
     * @param filter filter to add.
     * @param info <code>FilterInfo</code> object containing filter information.
     */
    void addFilter(T filter, FilterInfo info);
    /**
     * Called after adding all filters.
     * Could be used to sort filters in container(eg by priority)
     */
    void init();
    Collection<T> getFilters(String requestUri);
}
