package by.radchuk.task4.parser.sax;

import by.radchuk.task4.exception.ParseException;
import by.radchuk.task4.model.Device;
import by.radchuk.task4.parser.AbstractParser;
import com.sun.org.apache.xerces.internal.jaxp.JAXPConstants;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class SAXDeviceParser implements AbstractParser {
    private SAXParserFactory factory;
    private DeviceHandler handler;

    public SAXDeviceParser() {
        log.debug("Creating SAX device parser ...");
        handler = new DeviceHandler();
        factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        log.debug("SAX device parser created.");
    }

    @Override
    public List<Object> parse(InputStream xml, InputStream xsd) throws ParseException {
        try {
            log.debug("Initializing SAX parser ...");
            SAXParser parser = factory.newSAXParser();
            parser.setProperty(JAXPConstants.JAXP_SCHEMA_LANGUAGE, JAXPConstants.W3C_XML_SCHEMA);
            parser.setProperty(JAXPConstants.JAXP_SCHEMA_SOURCE, xsd);
            log.debug("SAX parser initialized, starting document parsing ...");
            parser.parse(xml, handler);
            @SuppressWarnings("unchecked")
            List<Object> result = (List)handler.getDevices();
            log.debug("document parsed successfully!");
            return result;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.debug("xml/xsd parsing failed! ", e);
            throw new ParseException(e);
        }
    }
}
