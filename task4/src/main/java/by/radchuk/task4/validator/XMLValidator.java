package by.radchuk.task4.validator;

import lombok.Getter;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;

public class XMLValidator {
    @Getter
    private String message = "OK";
    private SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    public boolean validateAgainstXSD(final InputStream xml,
                                      InputStream xsd) {
        try {
            Schema schema = factory.newSchema(new StreamSource(xsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xml));
        } catch (SAXException | IOException exception) {
            message = exception.getMessage();
            return false;
        }
        return true;
    }
}
