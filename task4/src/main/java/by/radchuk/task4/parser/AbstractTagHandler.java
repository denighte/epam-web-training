package by.radchuk.task4.parser;

import by.radchuk.task4.exception.ParseException;
import lombok.NonNull;

import java.util.Map;

/**
 * Tag handler 'push' interface.
 * Provides the API for xml parsing.
 */
public interface AbstractTagHandler extends Storage {
    /**
     * called when open tag was parsed.
     * @param tag open tag name.
     * @param attributes tag attributes map.
     * @throws ParseException raise it on parse errors.
     */
    void onOpen(@NonNull String tag,
                @NonNull Map<String, String> attributes)
                                        throws ParseException;

    /**
     * called when text data was parsed.
     * @param text text data.
     * @throws ParseException raise it on parse errors.
     */
    void onText(@NonNull String text) throws ParseException;

    /**
     * called when close tag wase parsed
     * @param tag close tag name.
     * @throws ParseException raise in on parse errors.
     */
    void onClose(@NonNull String tag) throws ParseException;
}
