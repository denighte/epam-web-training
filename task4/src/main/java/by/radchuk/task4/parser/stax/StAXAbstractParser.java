package by.radchuk.task4.parser.stax;

import by.radchuk.task4.exception.ParseException;
import by.radchuk.task4.parser.AbstractParser;
import by.radchuk.task4.parser.AbstractTagHandler;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class StAXAbstractParser implements AbstractParser {
    private XMLInputFactory inputFactory;
    private SchemaFactory schemaFactory;
    private AbstractTagHandler tagHandler;
    public StAXAbstractParser(AbstractTagHandler handler) {
        inputFactory = XMLInputFactory.newInstance();
        inputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
        schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        tagHandler = handler;
    }
    @Override
    public Object parse(final InputStream xml,
                              final InputStream xsd) throws ParseException {
        try {
            XMLStreamReader reader = inputFactory.createXMLStreamReader(xml);
            TagStreamReader deviceReader = new TagStreamReader(reader, tagHandler);
            Schema schema = schemaFactory.newSchema(new StreamSource(xsd));
            Validator validator = schema.newValidator();
            validator.validate(new StAXSource(deviceReader));
            Object result = deviceReader.getData();
            return result;
        } catch (IOException | XMLStreamException | SAXException exception) {
            log.debug("Exception during parsing!, ", exception);
            throw new ParseException(exception);
        }
    }
}
