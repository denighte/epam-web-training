package by.radchuk.task4.parser;

import by.radchuk.task4.exception.ParseException;

import java.io.InputStream;
import java.util.List;

/**
 * Base interface for parser classes.
 */
public interface AbstractParser {
    /**
     * Parses xml to list of the entity objects.
     * @param xml xml stream.
     * @param xsd xsd scheme stream to validate the xml.
     * @return List of entity objects, parsed from given xml.
     */
    List<Object> parse(InputStream xml, InputStream xsd) throws ParseException;
}
