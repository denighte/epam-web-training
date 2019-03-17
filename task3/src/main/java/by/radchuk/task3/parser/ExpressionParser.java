package by.radchuk.task3.parser;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.expression.ExpressionInterpreter;
import by.radchuk.task3.model.Expression;
import by.radchuk.task3.model.TextElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Expression parser.
 */
public class ExpressionParser implements AbstractParser {
    /**
     * next parser in the chain.
     */
    private static final AbstractParser NEXT_PARSER = new CharParser();

    /**
     * expression interpreter.
     * evaluates given string representation of arithmetical expression.
     */
    private ExpressionInterpreter interpreter;
    /**
     * parses expression from a string with data.
     * @param data TextElement data.
     * @return expression instance as TextElement.
     * @throws TextException in case parse error.
     */
    //private static final Pattern EXPRESSION_PATTERN = Pattern.compile("");
    @Override
    public TextElement parse(final String data) throws TextException {
        List<TextElement> childrenElements = new ArrayList<>();
        for (int i = 0; i < data.length(); ++i) {
            childrenElements.add(NEXT_PARSER.parse(
                    Character.toString(data.charAt(i)))
            );
        }
        //TODO: add number parsing.
        return new Expression(childrenElements, -1);
    }
}
