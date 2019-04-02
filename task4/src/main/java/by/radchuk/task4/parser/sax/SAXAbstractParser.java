package by.radchuk.task4.parser.sax;

import by.radchuk.task4.exception.ParseException;
import by.radchuk.task4.parser.AbstractParser;
import by.radchuk.task4.parser.AbstractTagHandler;
import com.sun.org.apache.xerces.internal.jaxp.JAXPConstants;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class SAXAbstractParser implements AbstractParser {
    private SAXParserFactory factory;
    private TagHandlerAdapter adapter;

    public SAXAbstractParser(AbstractTagHandler handler) {
        log.debug("Creating SAX device parser ...");
        adapter = new TagHandlerAdapter(handler);
        factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        log.debug("SAX device parser created.");
    }

    @Override
    public Object parse(InputStream xml, InputStream xsd) throws ParseException {
        try {
            log.debug("Initializing SAX parser ...");
            SAXParser parser = factory.newSAXParser();
            parser.setProperty(JAXPConstants.JAXP_SCHEMA_LANGUAGE, JAXPConstants.W3C_XML_SCHEMA);
            parser.setProperty(JAXPConstants.JAXP_SCHEMA_SOURCE, xsd);
            log.debug("SAX parser initialized, starting document parsing ...");
            parser.parse(xml, adapter);
            Object result = adapter.getData();
            log.debug("document parsed successfully!");
            return result;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.debug("exception during parsing! ", e);
            throw new ParseException(e);
        }
    }
}
