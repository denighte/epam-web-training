package by.radchuk.task.controller.security;

import by.radchuk.task.controller.filter.FilterInfo;
import by.radchuk.task.controller.task.WebTask;
import lombok.var;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.prefs.Preferences;

public class SecurityFilterProcessor {
    private static final Preferences SECURITY_PREFERENCES
            = Preferences.userNodeForPackage(SecurityFilterProcessor.class);
    private static final String PRIORITY_LEVEL = "sec_filter_priority";
    private static final int DEFAULT_SECURITY_FILTER_PRIORITY_LEVEL = Integer.MAX_VALUE;
    private static final int SECURITY_FILTER_PRIORITY_LEVEL
            = SECURITY_PREFERENCES.getInt(PRIORITY_LEVEL, DEFAULT_SECURITY_FILTER_PRIORITY_LEVEL);

    public FilterInfo getInfo(Collection<WebTask> tasks) {
        List<String> mapping = new ArrayList<>();
        for(var task : tasks) {
            if (task.getSecurityLevel() != 0) {
                mapping.add(task.getPath());
            }
        }
        return new FilterInfo(mapping.toArray(new String[0]), SECURITY_FILTER_PRIORITY_LEVEL);
    }
}
