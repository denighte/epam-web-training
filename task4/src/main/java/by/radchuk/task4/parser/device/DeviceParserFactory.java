package by.radchuk.task4.parser.device;

import by.radchuk.task4.parser.AbstractParser;
import by.radchuk.task4.parser.dom.DOMAbstractParser;
import by.radchuk.task4.parser.sax.SAXAbstractParser;
import by.radchuk.task4.parser.stax.StAXAbstractParser;

/**
 * Device Parsers factory class.
 * Returns Parser instance depending on string name.
 */
public final class DeviceParserFactory {
    private static final DeviceParserFactory INSTANCE = new DeviceParserFactory();
    public static DeviceParserFactory getInstance() {
        return INSTANCE;
    }
    public AbstractParser createParser(final String name) {
        switch (ParserType.valueOf(name)) {
            case DOM:
                return new DOMAbstractParser(new DeviceTagHandler());
            case SAX:
                return new SAXAbstractParser(new DeviceTagHandler());
            case StAX:
                return new StAXAbstractParser(new DeviceTagHandler());
            default:
                throw new IllegalArgumentException();
        }
    }
    private enum ParserType {
        DOM,
        SAX,
        StAX
    }
}
