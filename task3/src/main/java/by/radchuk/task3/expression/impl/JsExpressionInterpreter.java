package by.radchuk.task3.expression.impl;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.expression.ExpressionInterpreter;
import lombok.extern.slf4j.Slf4j;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Expression interpreter class.
 * Uses javascript to evaluate expressions.
 */
@Slf4j
public final class JsExpressionInterpreter implements ExpressionInterpreter {
    /**
     * Script Engine which used to evaluate expressions.
     */
    private static final ScriptEngine SCRIPT_ENGINE
            = new ScriptEngineManager().getEngineByName("JavaScript");


    @Override
    public Number eval(final String expression) throws TextException {
        Object result;
        try {
            result = SCRIPT_ENGINE.eval(expression);
        } catch (ScriptException exception) {
            log.error("Expression interpretation error,"
                      + "given expression = {}", expression);
            throw new TextException(exception);
        }
        return (Number) result;
    }
}
