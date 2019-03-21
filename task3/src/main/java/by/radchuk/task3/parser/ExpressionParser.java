package by.radchuk.task3.parser;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.expression.ExpressionInterpreter;
import by.radchuk.task3.expression.impl.JsExpressionInterpreter;
import by.radchuk.task3.model.TextElement;
import by.radchuk.task3.model.TextElementType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Expression parser.
 */
@Slf4j
class ExpressionParser implements AbstractParser {
    /**
     * next parser in the chain.
     */
    private static final AbstractParser NEXT_PARSER = new CharParser();

    /**
     * expression interpreter.
     * evaluates given string representation of arithmetical expression.
     */
    private ExpressionInterpreter interpreter = new JsExpressionInterpreter();
    /**
     * parses expression from a string with data.
     * @param data TextElement data.
     * @return expression instance as TextElement.
     * @throws TextException in case parse error.
     */
    //private static final Pattern EXPRESSION_PATTERN = Pattern.compile("");
    @Override
    public TextElement parse(final String data) throws TextException {
        log.info("Parsing expression ...");
        List<TextElement> childrenElements = new ArrayList<>();
        for (int i = 0; i < data.length(); ++i) {
            childrenElements.add(NEXT_PARSER.parse(
                    Character.toString(data.charAt(i)))
            );
        }
        Number value;
        try {
            value = interpreter.eval(data).intValue();
        } catch (TextException exception) {
            log.error("Can't parse the given expression!, expression={}", data);
            throw new TextException(exception);
        }
        log.info("Expression parsed, creating expression instance ...");
        return new TextElement(TextElementType.EXPRESSION, childrenElements) {
            @Override
            public String toString() {
                return value.toString();
            }
        };
    }
}
