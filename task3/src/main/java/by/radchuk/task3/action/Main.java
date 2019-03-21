package by.radchuk.task3.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        //Компилируем регулярку
        Pattern pattern = Pattern.compile("[^.]+?(?:\\.{3}|\\.|!|\\?)");
        //строка с текстом
        String data = "abc dfas dsaf... dfsfs fsdfsd\n\t adf. dsf!";
        //Передаем классу Matcher строку в которой будем искать совпадения.
        Matcher matcher = pattern.matcher(data);
        //перебираем все совпадения
        while (matcher.find())
            System.out.println(matcher.group(0));
        }
}
