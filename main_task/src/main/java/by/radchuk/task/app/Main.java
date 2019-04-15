package by.radchuk.task.app;

import by.radchuk.task.dao.UserDao;
import by.radchuk.task.dao.framework.Executor;
import by.radchuk.task.dao.framework.H2Manger;
import by.radchuk.task.dao.framework.Queries;
import lombok.Cleanup;

public class Main {
    public static void main(String[] args) throws Exception {
        @Cleanup UserDao dao = new UserDao();
        System.out.println(dao.find(1));
    }
}
