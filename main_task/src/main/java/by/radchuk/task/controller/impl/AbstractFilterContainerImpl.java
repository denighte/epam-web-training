package by.radchuk.task.controller.impl;
import by.radchuk.task.controller.AbstractFilter;
import by.radchuk.task.controller.AbstractFilterContainer;
import by.radchuk.task.controller.ControllerException;
import by.radchuk.task.util.StringView;
import lombok.AllArgsConstructor;
import lombok.var;

import java.util.*;
import java.util.stream.Collectors;

public class AbstractFilterContainerImpl<T extends AbstractFilter> implements AbstractFilterContainer<T> {
    private Map<StringView, List<FilterWrapper<T>>> container = new HashMap<>();
    private FilterProcessor processor = new FilterProcessor();

    @Override
    public void addFilter(Class<T> filterClass) throws ControllerException {
        FilterInfo info = processor.getInfo(filterClass);
        T filter;
        try {
            filter = filterClass.newInstance();
            filter.init();
        } catch (InstantiationException | IllegalAccessException exception) {
            throw new ControllerException("Can't find accessible no args constructor.", exception);
        }
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
            list.sort((lhs, rhs) -> Integer.compare(lhs.priority, rhs.priority));
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
