package by.radchuk.task4.parser.dom;

import lombok.extern.slf4j.Slf4j;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

@Slf4j
public class StrictErrorHandler implements ErrorHandler {
    @Override
    public void warning(final SAXParseException exception) throws SAXException {
        log.debug("Got DOM creation warning, message = {}", exception.getMessage());
        //ignore
    }

    @Override
    public void error(final SAXParseException exception) throws SAXException {
        log.debug("Got error during DOM creation.", exception);
        throw new SAXException(exception);
    }

    @Override
    public void fatalError(final SAXParseException exception) throws SAXException {
        log.debug("Got error during DOM creation.", exception);
        throw new SAXException(exception);
    }
}
