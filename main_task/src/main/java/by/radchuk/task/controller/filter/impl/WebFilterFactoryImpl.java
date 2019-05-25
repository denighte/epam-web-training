package by.radchuk.task.controller.filter.impl;

import by.radchuk.task.controller.context.ControllerContext;
import by.radchuk.task.controller.filter.AbstractFilter;
import by.radchuk.task.controller.ControllerException;
import by.radchuk.task.controller.filter.WebFilterFactory;

public class WebFilterFactoryImpl<T extends AbstractFilter> implements WebFilterFactory<T> {
    private ControllerContext context;
    @Override
    public T create(Class<T> filterClass) throws ControllerException {
        T filter;
        try {
            filter = filterClass.newInstance();
            filter.init(context);
        } catch (InstantiationException | IllegalAccessException exception) {
            throw new ControllerException("Can't find accessible no args constructor.", exception);
        }
        return filter;
    }

    @Override
    public void setControllerContext(ControllerContext context) {
        this.context = context;
    }
}
