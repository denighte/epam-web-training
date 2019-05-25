package by.radchuk.task.controller.context;

import by.radchuk.task.controller.filter.WebFilterContainer;
import by.radchuk.task.controller.filter.RequestFilter;
import by.radchuk.task.controller.filter.ResponseFilter;
import by.radchuk.task.controller.security.SecurityFilter;
import by.radchuk.task.controller.task.WebTaskContainer;
import by.radchuk.task.dao.framework.ConnectionManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ControllerContext {
    private WebTaskContainer taskContainer;
    private WebFilterContainer<RequestFilter> requestFilterContainer;
    private WebFilterContainer<ResponseFilter> responseFilterContainer;
    private ConnectionManager connectionManager;
    private SecurityFilter securityFilter;
}
