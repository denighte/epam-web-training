package by.radchuk.task4.parser.dom;

import by.radchuk.task4.exception.ParseException;
import by.radchuk.task4.parser.AbstractParser;
import by.radchuk.task4.parser.AbstractTagHandler;
import com.sun.org.apache.xerces.internal.jaxp.JAXPConstants;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;


@Slf4j
public class DOMAbstractParser implements AbstractParser {
    private DocumentBuilderFactory factory;
    private TagHandlerAdapter adapter;
    public DOMAbstractParser(final AbstractTagHandler handler) {
        log.debug("Creating DOM device parser ...");
        factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        factory.setAttribute(JAXPConstants.JAXP_SCHEMA_LANGUAGE, JAXPConstants.W3C_XML_SCHEMA);
        adapter = new TagHandlerAdapter(handler);
        log.debug("DOM device parser created.");
    }

    @Override
    public Object parse(final InputStream xml,
                        final InputStream xsd) throws ParseException {
        try {
            log.debug("initializing DOM builder ...");
            factory.setAttribute(JAXPConstants.JAXP_SCHEMA_SOURCE, xsd);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new StrictErrorHandler());
            log.debug("DOM builder initialized, parsing xml into document ...");
            Document document = builder.parse(xml);
            log.debug("document parsed, collecting information ...");
            adapter.handle(document);
            Object result = adapter.getData();
            log.debug("information successfully collected!");
            return result;
        } catch (IOException | ParserConfigurationException | SAXException exception) {
            log.debug("Exception during parsing!, ", exception);
            throw new ParseException(exception);
        }
    }

}
