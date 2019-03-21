package by.radchuk.task3.expression.impl;

import by.radchuk.task3.exception.TextException;
/**
 * Вычислимое Выражение
 */
public abstract class Expression {
    /** Вычислить выражение для даных значений переменных */
    public abstract long execute() throws Exception;

    /** Узел дерева — «Число» */
    static class Num extends Expression {
        private final long value;

        Num(long x) {
            value = x;
        }

        @Override
        public long execute() {
            return value;
        }
    }

    /** Узел дерева — «Унарный оператор» */
    static class Unary extends Expression {
        private final Expression expr;
        private final boolean not;

        Unary(Expression e, String oper) {
            expr = e;
            not = "~".equals(oper);
        }

        @Override
        public long execute() throws Exception {
            long o = expr.execute();
            if(not)
                return ~o;
            return (Long)o;
        }
    }

    /** Узел дерева — «Бинарный оператор» */
    static class Binary extends Expression {
        private final Expression x1;
        private final Expression x2;
        private final String op;

        Binary(Expression x1, Expression x2, String op) {
            this.x1 = x1;
            this.x2 = x2;
            this.op = op;
        }

        @Override
        public long execute() throws Exception {
            Object o1 = x1.execute();
            Object o2 = x2.execute();
            return execNum((Long)o1, (Long)o2);
        }

        private long execNum(long n1, long n2) throws TextException {
            if(">>>".equals(op))
                return n1 >>> n2;
            if(">>".equals(op))
                return n1 >> n2;
            if("<<".equals(op))
                return n1 << n2;
            if("&".equals(op))
                return n1 & n2;
            if("^".equals(op))
                return n1 ^ n2;
            if("|".equals(op))
                return n1 | n2;
            throw new TextException("Illegal Long operator: " + op);
        }
    }
}
