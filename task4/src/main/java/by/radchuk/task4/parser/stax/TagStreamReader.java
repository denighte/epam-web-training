package by.radchuk.task4.parser.stax;

import by.radchuk.task4.parser.AbstractTagHandler;
import by.radchuk.task4.parser.Storage;
import lombok.extern.slf4j.Slf4j;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class TagStreamReader extends StreamReaderDelegate implements Storage {
    private AbstractTagHandler tagHandler;
    public TagStreamReader(final XMLStreamReader reader, final AbstractTagHandler handler) {
        super(reader);
        tagHandler = handler;
    }

    @Override
    public Object getData() {
        return tagHandler.getData();
    }

    @Override
    public int next() throws XMLStreamException {
        int event = getEventType();
        try {
            switch (event) {
                case START_DOCUMENT:
                    log.debug("Started xml parsing.");
                    break;
                case START_ELEMENT:
                    log.debug("Start Element: {}", getLocalName());
                    tagHandler.onOpen(getLocalName(),
                            IntStream.range(0, getAttributeCount())
                                     .boxed()
                                     .collect(Collectors
                                             .toMap(i -> getAttributeName(i).getLocalPart(), this::getAttributeValue)));
                    break;
                case CHARACTERS:
                    if (isWhiteSpace()) {
                        break;
                    }
                    log.debug("Text: {}", getText());
                    tagHandler.onText(getText());
                    break;
                case END_ELEMENT:
                    tagHandler.onClose(getName().getLocalPart());
                    log.debug("End Element", getName());
                    break;
                case END_DOCUMENT:
                    log.debug("Ended document parsing.");
                    break;
            }
        } catch (Exception exception) {
            log.debug("Error during parsing!, {}", exception);
            //ignoring the error, leaving it for validator.
        }
        return super.next();
    }

}
