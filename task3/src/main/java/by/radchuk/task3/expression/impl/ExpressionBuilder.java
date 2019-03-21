package by.radchuk.task3.expression.impl;

/** Компилятор выражений */
public class ExpressionBuilder {

    private String expression; // Строка с исходным выражением
    private int p = 0; // текущая позиция

    public static Expression build(String expression) {
        ExpressionBuilder builder = new ExpressionBuilder(expression);
        builder.skip(" ");
        Expression expr = builder.build(0);
        return expr;
    }

    private ExpressionBuilder(String expression) {
        this.expression = expression;
    }


    /**
     * Построить узел выражения
     */
    Expression build(int state) {
        if (lastState(state)) {
            Expression ex = null;
            boolean isMinus = startWith("~");
            if (isMinus)
                skip("~");

            if (startWith("(")) {
                skip("(");
                ex = build(0);
                skip(")");
            } else
                ex = readSingle();
            if (isMinus)
                ex = new Expression.Unary(ex, "~");
            return ex;
        }

        /* Строим первый операнд */
        Expression a1 = build(state + 1);

        // строим последущие операнды
        String op = null;
        while ((op = readStateOperator(state)) != null) {
            Expression a2 = build(state + 1);
            a1 = new Expression.Binary(a1, a2, op);

        }
        return a1;
    }

    private static String[][] states = new String[][]{
            {"|"},
            {"^"},
            {"&"},
            {">>>", ">>", "<<"}
    };

    private boolean lastState(int s) {
        return s + 1 >= states.length;
    }

    private boolean startWith(String s) {
        return expression.startsWith(s, p);
    }

    private void skip(String s) {
        if (startWith(s))
            p += s.length();
        while (p < expression.length() && expression.charAt(p) == ' ')
            p++;
    }


    private String readStateOperator(int state) {
        String[] ops = states[state];
        for (String s : ops) {
            if (startWith(s)) {
                skip(s);
                return s;
            }
        }
        return null;
    }

    /**
     * считываем из потока "простое" значение (имя переменной, число или строку)
     *
     * @return
     */
    private Expression readSingle() {
        int p0 = p;

        // в потоке не строка => число или переменная
        while (p < expression.length()) {
            if (!(Character.isDigit(expression.charAt(p))))
                break;
            p++;
        }

        Expression ex = null;
        if (p > p0) {
            String s = expression.substring(p0, p);
            skip(" ");
            // из потока прочитали число
            long x = Long.parseLong(s);
            return new Expression.Num(x);
        }
        return null;
    }
}
