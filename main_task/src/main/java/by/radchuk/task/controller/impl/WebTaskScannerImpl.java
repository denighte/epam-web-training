package by.radchuk.task.controller.impl;

import by.radchuk.task.controller.ControllerException;
import by.radchuk.task.controller.WebTask;
import by.radchuk.task.controller.WebTaskFactory;
import by.radchuk.task.controller.WebTaskScanner;
import by.radchuk.task.controller.annotation.WebHandler;
import by.radchuk.task.util.ClassReflections;
import lombok.var;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WebTaskScannerImpl implements WebTaskScanner {
    //could be @Autowired
    private WebTaskFactory factory = new WebTaskFactoryImpl();

    @Override
    public Collection<WebTask> scan(String clsPackage) throws ControllerException {
        List<WebTask> tasks = new ArrayList<>();
        try {
            List<Class> classes = ClassReflections.builder().load(clsPackage)
                    .filter(WebHandler.class).get();
            for (var cls : classes) {
                tasks.addAll(factory.create(cls));
            }
        } catch (IOException | ControllerException exception) {
            throw new ControllerException("Failed to scan the package!", exception);
        }
        return tasks;
    }
}
