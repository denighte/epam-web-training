package by.radchuk.task4.parser.stax;

import by.radchuk.task4.exception.ParseException;
import by.radchuk.task4.model.Device;
import by.radchuk.task4.parser.AbstractParser;
import by.radchuk.task4.parser.device.DeviceParserFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StAXAbstractParserTest {
    private AbstractParser parser;
    private InputStream xml;
    private InputStream xsd;

    @Parameters({"xmlTestFilePath", "xsdTestFilePath"})
    @BeforeClass
    void setUp(String xmlPath, String xsdPath) throws IOException {
        parser = DeviceParserFactory.getInstance().createParser("StAX");
        xml = Files.newInputStream(Paths.get(xmlPath));
        xsd = Files.newInputStream(Paths.get(xsdPath));
    }

    @Parameters({"xmlParseResultPath"})
    @Test
    void test(String resultPath) throws ParseException, IOException {
        @SuppressWarnings("unchecked") List<Device> actual = (List<Device>) parser.parse(xml, xsd);
        List<String> expected = new BufferedReader(
                new InputStreamReader(
                        Files.newInputStream(
                                Paths.get(resultPath)))).lines().collect(Collectors.toList());
        if(expected.size() != actual.size()) {
            Assert.fail();
        }
        IntStream.range(0, actual.size()).boxed().forEach(i -> actual.get(i).toString().equals(expected.get(i)));
        Assert.assertTrue(true);
    }
}
