package by.radchuk.task4.parser;

import by.radchuk.task4.exception.ParseException;
import java.io.InputStream;

/**
 * Base interface for parser classes.
 */
public interface AbstractParser {
    /**
     * Parses xml to list of the entity objects.
     * @param xml xml stream.
     * @param xsd xsd scheme stream to validate the xml.
     * @throws ParseException in case parse errors.
     * @return List of entity objects, parsed from given xml.
     */
    Object parse(InputStream xml, InputStream xsd) throws ParseException;
}
