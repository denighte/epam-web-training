package by.radchuk.task2.app;

import by.radchuk.task2.context.EnvironmentContext;
import by.radchuk.task2.controller.UserLogic;
import by.radchuk.task2.entity.User;
import by.radchuk.task2.factory.ConcurrentUserFactory;
import by.radchuk.task2.factory.UserFactory;
import by.radchuk.task2.stock.HttpsStockExchange;
import by.radchuk.task2.util.UserPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static int LIFE_TIME = 5000;

    public static void main(String[] args) throws Exception{
        EnvironmentContext context = EnvironmentContext.getInstance();
        UserFactory factory = new ConcurrentUserFactory();
        List<User> users = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(5);

        for(String userData : context.getUsersData()) {
            User user = factory.create(userData);
            users.add(user);
            service.submit(new UserLogic(user));
        }

        Thread printer = new Thread(new UserPrinter(users));
        printer.setDaemon(true);
        printer.start();

        Thread.sleep(LIFE_TIME);
        service.shutdownNow();
        HttpsStockExchange.getInstance().getThreadPool().shutdownNow();
    }
}
