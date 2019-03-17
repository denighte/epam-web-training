package by.radchuk.task3.expression;

import by.radchuk.task3.exception.TextException;

/**
 * Arithmetical expression interpreter.
 * Evaluates the expression, given in a string form.
 */
public interface ExpressionInterpreter {
    /**
     * evaluates the expression.
     * @param expression string representation of expression.
     * @return result of the given expression.
     * @throws TextException in case interpretation error.
     */
    Number eval(String expression) throws TextException;
}
