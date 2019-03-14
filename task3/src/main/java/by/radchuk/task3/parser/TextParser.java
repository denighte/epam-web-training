package by.radchuk.task3.parser;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.model.TextElement;
import by.radchuk.task3.model.TextElementType;
import by.radchuk.task3.validator.TextMatchValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse text from a string.
 */
@Slf4j
public class TextParser {
    //(?:\.{3}|.|\?|!)
    /**
     * Text pattern.
     */
    private static final Pattern TEXT_PATTERN = Pattern.compile("((?: {4}|\t)[\\w,:;\\-−])(?: {4}|\t)");

    public TextElement parse(String data) throws TextException {
        log.info("Parsing text ...");
        TextElement text = new TextElement();
        List<String> childElements = new ArrayList<>();

        Matcher matcher = TEXT_PATTERN.matcher(data);
        TextMatchValidator validator = new TextMatchValidator(0);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            if(!validator.validate(start, end)) {
                log.error("text cannot be parsed!");
                throw new TextException("invalid format of text!");
            }
            childElements.add(matcher.group(1));
        }
        return new TextElement(TextElementType.TEXT, childElements);
    }


}
