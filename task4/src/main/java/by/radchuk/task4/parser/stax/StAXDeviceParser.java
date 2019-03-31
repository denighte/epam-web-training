package by.radchuk.task4.parser.stax;

import by.radchuk.task4.exception.ParseException;
import by.radchuk.task4.parser.AbstractParser;
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
import java.util.List;

@Slf4j
public class StAXDeviceParser implements AbstractParser {
    private XMLInputFactory inputFactory;
    private SchemaFactory schemaFactory;
    public StAXDeviceParser() {
        inputFactory = XMLInputFactory.newInstance();
        inputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
        schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    }
    @Override
    public List<Object> parse(final InputStream xml,
                              final InputStream xsd) throws ParseException {
        try {
            XMLStreamReader reader = inputFactory.createXMLStreamReader(xml);
            DeviceStreamReader deviceReader = new DeviceStreamReader(reader);
            Schema schema = schemaFactory.newSchema(new StreamSource(xsd));
            Validator validator = schema.newValidator();
            validator.validate(new StAXSource(deviceReader));
            @SuppressWarnings("unchecked")
            List<Object> result = (List) deviceReader.getDevices();
            return result;
        } catch (IOException | XMLStreamException | SAXException exception) {
            log.debug("Exception during parsing!, ", exception);
            throw new ParseException(exception);
        }
    }
}
