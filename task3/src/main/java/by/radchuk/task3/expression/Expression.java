package by.radchuk.task3.expression;

import lombok.AllArgsConstructor;

import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;

/**
 * Arithmetical expression class.
 */
public abstract class Expression {
    /**
     * Executes expression.
     * @return result value.
     */
    public abstract long execute();

    /** Tree node — «Number». */
    @AllArgsConstructor
    static class Num extends Expression {
        /**
         * value of Num.
         */
        private final long value;

        /**
         * returns Num value.
         * @return Num node value
         */
        @Override
        public long execute() {
            return value;
        }
    }

    /** Tree node. — «Unary operator» */
    @AllArgsConstructor
    static class Unary extends Expression {
        /**
         * expression.
         */
        private final Expression expr;
        /**
         * unary operator.
         */
        private final LongUnaryOperator operator;

        /**
         * applies unary operator to expression.
         * @return operator result.
         */
        @Override
        public long execute() {
            long value = expr.execute();
            return operator.applyAsLong(value);
        }
    }

    /** Tree node — «Binary operator». */
    @AllArgsConstructor
    static class Binary extends Expression {
        /**
         * left operand.
         */
        private final Expression lhs;
        /**
         * right operand.
         */
        private final Expression rhs;
        /**
         * binary operator.
         */
        private final LongBinaryOperator operator;

        /**
         * applies binary operator to expression.
         * @return operator result.
         */
        @Override
        public long execute() {
            long o1 = lhs.execute();
            long o2 = rhs.execute();
            return operator.applyAsLong(o1, o2);
        }
    }
}
