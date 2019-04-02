package by.radchuk.task4.parser.sax;

import by.radchuk.task4.exception.ParseException;
import by.radchuk.task4.parser.AbstractTagHandler;
import by.radchuk.task4.parser.Storage;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Slf4j
public class TagHandlerAdapter extends DefaultHandler implements Storage {
    private AbstractTagHandler tagHandler;
    private StringBuilder builder;

    TagHandlerAdapter(final AbstractTagHandler handler) {
        super();
        builder = new StringBuilder();
        tagHandler = handler;
    }

    @Override
    public Object getData() {
        return tagHandler.getData();
    }

    @Override
    public void startDocument() throws SAXException {
        log.debug("Started document parsing.");
    }

    @Override
    public void endDocument() throws SAXException {
        log.debug("Ended document parsing.");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        log.debug("Started element parsing with localName = {}", localName);
        try {
            tagHandler.onOpen(qName, IntStream.range(0, attributes.getLength()).boxed().collect(Collectors.toMap(attributes::getQName, attributes::getValue)));
        } catch (ParseException exception) {
            log.debug("Parse error for open tag with qName = {}!", qName);
            log.debug("{}", exception);
            throw new SAXException(exception);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            tagHandler.onText(builder.toString().trim());
            tagHandler.onClose(qName);
        } catch (ParseException exception) {
            log.debug("Parse error for close tag with qName = {}!", qName);
            log.debug("{}", exception);
            throw new SAXException(exception);
        }
        builder.setLength(0);
        log.debug("Ended element parsing with qName = {}", qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        builder.append(new String(ch, start, length));
    }
}