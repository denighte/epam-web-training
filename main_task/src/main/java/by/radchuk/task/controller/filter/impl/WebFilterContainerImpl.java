package by.radchuk.task.controller.filter.impl;
import by.radchuk.task.controller.filter.AbstractFilter;
import by.radchuk.task.controller.filter.FilterInfo;
import by.radchuk.task.controller.filter.WebFilterContainer;
import by.radchuk.task.controller.ControllerException;
import by.radchuk.task.util.StringView;
import lombok.AllArgsConstructor;
import lombok.var;

import java.util.*;
import java.util.stream.Collectors;

public class WebFilterContainerImpl<T extends AbstractFilter> implements WebFilterContainer<T> {
    private Map<StringView, List<FilterWrapper<T>>> container = new HashMap<>();
    private FilterProcessor processor = new FilterProcessor();

    @Override
    public void addFilter(T filter) throws ControllerException {
        FilterInfo info = processor.getInfo(filter.getClass());
        addFilter(filter, info);
    }

    @Override
    public void addFilter(T filter, FilterInfo info) {
        FilterWrapper<T> wrapper = new FilterWrapper<>(filter, info.getPriority());
        for (String mapping : info.getMapping()) {
            StringView containerMapping = new StringView(mapping);
            container.putIfAbsent(containerMapping, new ArrayList<>());
            List<FilterWrapper<T>> filters = container.get(containerMapping);
            filters.add(wrapper);
        }
    }

    @Override
    public void init() {
        for (var list : container.values()) {
            list.sort((lhs, rhs) -> Integer.compare(rhs.priority, lhs.priority));
        }
    }

    @Override
    public Collection<T> getFilters(String requestUri) {
        StringView uri = new StringView(requestUri);
        List<FilterWrapper<T>> filters = new ArrayList<>(container.getOrDefault(uri, Collections.emptyList()));
        int uriDepth = uri.count('/');
        for(int i = 0; i < uriDepth; ++i) {
            uri.add("/*");
            filters.addAll(container.getOrDefault(uri, Collections.emptyList()));
            uri.setEnd(uri.lastIndexOf('/'));
            uri.setEnd(uri.lastIndexOf('/'));
        }
        return filters.stream().map(wrapper -> wrapper.filter).collect(Collectors.toList());
    }

    @AllArgsConstructor
    private static class FilterWrapper<T> {
        T filter;
        int priority;
    }
}
