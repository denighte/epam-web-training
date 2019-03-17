package by.radchuk.task3.parser;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.model.TextElement;
import by.radchuk.task3.model.TextElementType;
import by.radchuk.task3.validator.TextContinuityValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Parse text from a string.
 */
@Slf4j
public class TextParser implements AbstractParser{
    /**
     * parser in the chain.
     */
    private static final AbstractParser NEXT_PARSER = new ParagraphParser();
    /**
     * Paragraph pattern.
     */
    //"(?: {4}|\t)(.+?)(?: {4}|\t)"
    private static final Pattern PARAGRAPH_PATTERN
            = Pattern.compile("(?<=\t)(.*?)(?=\\.)");
            //= Pattern.compile("\t([\\w',.;:()><|^&~]+?)\t");

    public TextElement parse(String data) throws TextException {
        log.info("Parsing text ...");
        List<TextElement> childrenElements = new ArrayList<>();

        List<String> paragraphs = Arrays.stream(
                data.split("\t| {4}"))
                    .filter(paragraph -> !paragraph.equals(""))
                    .collect(Collectors.toList()
                    );
        for (String paragraph : paragraphs) {
            childrenElements.add(NEXT_PARSER.parse(paragraph));
        }
        log.info("Text parsed, creating text instance ...");
        return new TextElement(TextElementType.TEXT, childrenElements);
    }


}
