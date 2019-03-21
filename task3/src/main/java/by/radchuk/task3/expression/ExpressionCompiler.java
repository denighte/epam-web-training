package by.radchuk.task3.expression;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;

/** Arithmetical expression compiler. */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExpressionCompiler {

    /**
     * String with expression.
     */
    private String expression;
    /**
     * Current state machine status.
     */
    private int position;

    /**
     * Compiles given string to expression tree.
     * @param expression string representation of expression.
     * @return Expression tree instance.
     */
    public static Expression compile(final String expression) {
        ExpressionCompiler builder = new ExpressionCompiler(expression, 0);
        builder.skip(" ");
        return builder.compile(0);
    }

    /**
     * Builds expression node.
     * @param state current state machine status.
     * @return Expression instance.
     */
    private Expression compile(final int state) {
        if (lastState(state)) {
            Expression ex = null;
            boolean isBitwiseCompliment = startWith("~");
            if (isBitwiseCompliment) {
                skip("~");
            }

            if (startWith("(")) {
                skip("(");
                ex = compile(0);
                skip(")");
            } else {
                ex = readSingle();
            }
            if (isBitwiseCompliment) {
                ex = new Expression
                        .Unary(ex, UnaryOperator.fromString("~").getOperator());
            }
            return ex;
        }

        // Build first operand
        Expression lhs = compile(state + 1);

        // Build next operands
        String op;
        while ((op = readStateOperator(state)) != null) {
            Expression rhs = compile(state + 1);
            lhs = new Expression.Binary(lhs,
                                rhs,
                                BinaryOperator.fromString(op).getOperator());
        }
        return lhs;
    }

    /**
     * State machine rules.
     */
    private static String[][] states = new String[][]{
            {"|"},
            {"^"},
            {"&"},
            {">>>", ">>", "<<"}
    };

    /**
     * is last state of the state machine.
     * @param state current state
     * @return true, if last, otherwise false.
     */
    private boolean lastState(final int state) {
        return state + 1 >= states.length;
    }

    /**
     * checks whether expression starts with given s.
     * @param s string.
     * @return true, if starts, otherwise false.
     */
    private boolean startWith(final String s) {
        return expression.startsWith(s, position);
    }

    /**
     * skips given s.
     * @param s string.
     */
    private void skip(final String s) {
        if (startWith(s)) {
            position += s.length();
        }
        while (position < expression.length()
                && expression.charAt(position) == ' ') {
            ++position;
        }
    }

    /**
     * reads next state of the state machine.
     * @param state int representation of the state.
     * @return string representation of the state.
     */
    private String readStateOperator(final int state) {
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
     * read "simple" value from stream (num).
     * @return Expression instance (Num).
     */
    private Expression readSingle() {
        int p0 = position;

        while (position < expression.length()
            && Character.isDigit(expression.charAt(position))) {
            ++position;
        }

        if (position > p0) {
            String s = expression.substring(p0, position);
            skip(" ");
            long x = Long.parseLong(s);
            return new Expression.Num(x);
        }
        return null;
    }

    /**
     * Binary operators enum.
     */
    @AllArgsConstructor
    enum BinaryOperator {
        /**
         * Zero fill right shift.
         */
        ZERO_FILL_RIGHT_SHIFT(">>>", (lhs, rhs) -> lhs >>> rhs),
        /**
         * Right shift.
         */
        RIGHT_SHIFT(">>", (lhs, rhs) -> lhs >> rhs),
        /**
         * Left shift.
         */
        LEFT_SHIFT("<<", (lhs, rhs) -> lhs << rhs),
        /**
         * Bitwise xor.
         */
        BITWISE_XOR("^", (lhs, rhs) -> lhs ^ rhs),
        /**
         * Bitwise or.
         */
        BITWISE_OR("|", (lhs, rhs) -> lhs | rhs),
        /**
         * Bitwise and.
         */
        BITWISE_AND("&", (lhs, rhs) -> lhs & rhs);

        /**
         * string representation of the operator.
         */
        private String string;
        /**
         * operator as functional interface.
         */
        @Getter
        private LongBinaryOperator operator;

        /**
         * get BinaryOperator from its string representation.
         * @param str string representation of the operator.
         * @return BinaryOperator instance.
         */
        static BinaryOperator fromString(final String str) {
            for (BinaryOperator op : BinaryOperator.values()) {
                if (op.string.equalsIgnoreCase(str)) {
                    return op;
                }
            }
            return null;
        }
    }

    /**
     * Unary operators enum.
     */
    @AllArgsConstructor
    enum UnaryOperator {
        /**
         * Bitwise compliment.
         */
        BITWISE_COMPLIMENT("~", value -> ~value);

        /**
         * string representation of the operator.
         */
        private String string;
        /**
         * operator as functional interface.
         */
        @Getter
        private LongUnaryOperator operator;

        /**
         * get UnaryOperator from its string representation.
         * @param str string representation of the operator.
         * @return UnaryOperator instance.
         */
        static UnaryOperator fromString(final String str) {
            for (UnaryOperator op : UnaryOperator.values()) {
                if (op.string.equalsIgnoreCase(str)) {
                    return op;
                }
            }
            return null;
        }
    }
}
