package by.radchuk.task3.expression.impl;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.expression.ExpressionInterpreter;
import lombok.extern.slf4j.Slf4j;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

@Slf4j
public final class JsExpressionInterpreter implements ExpressionInterpreter {
    private static final ScriptEngine SCRIPT_ENGINE
            = new ScriptEngineManager().getEngineByName("JavaScript");


    @Override
    public long eval(String expression) throws TextException {
        Object result;
        try {
            result = SCRIPT_ENGINE.eval(expression);
        } catch (ScriptException exception) {
            log.error("Expression interpretation error, given expression = {}", expression);
            throw new TextException(exception);
        }
        return (Long)result;
    }
}
