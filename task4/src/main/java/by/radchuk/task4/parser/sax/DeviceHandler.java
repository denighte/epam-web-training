package by.radchuk.task4.parser.sax;

import by.radchuk.task4.model.Input;
import by.radchuk.task4.model.Manufacture;
import by.radchuk.task4.model.Connector;
import by.radchuk.task4.model.Device;
import by.radchuk.task4.model.DeviceCritical;
import by.radchuk.task4.model.DeviceType;
import by.radchuk.task4.model.Price;
import by.radchuk.task4.parser.Tag;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


@Slf4j
public class DeviceHandler extends DefaultHandler {
    @Getter
    private List<Device> devices;
    private Device device;
    private Price price;
    private StringBuilder builder;
    private Stack<Tag> tagStack;

    public DeviceHandler() {
        super();
        devices = new ArrayList<>();
        initDevice();
        builder = new StringBuilder();
        tagStack = new Stack<>();
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
            Tag current = Tag.forName(qName);
            switch (current) {
                case DEVICE:
                    if (attributes.getLength() != 3) {
                        throw new IllegalArgumentException("Invalid number of attributes!");
                    }
                    device.setId(Long.parseLong(attributes.getValue("id")));
                    device.setCritical(DeviceCritical.fromValue(attributes.getValue("critical")));
                    device.setType(DeviceType.fromValue(attributes.getValue("type")));
                    break;
                case PRICE:
                    if (attributes.getLength() != 1) {
                        throw new IllegalArgumentException("Invalid number of attributes!");
                    }
                    device.getPrice().setCurrency(attributes.getValue("currency"));
                    break;
            }
            tagStack.push(current);
        } catch (IllegalArgumentException exception) {
            log.debug("Parse error, tag with localName = {} is invalid!", localName);
            log.debug("{}", exception);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (tagStack.pop()) {
            case NAME:
                device.setName(builder.toString().trim());
                break;
            case DESCRIPTION:
                device.setDescription(builder.toString().trim());
                break;
            case RATING:
                device.setRating(Double.parseDouble(builder.toString().trim()));
                break;
            case CONNECTOR:
                device.getInput().setConnector(Connector.fromValue(builder.toString().trim()));
                break;
            case ENERGY_USAGE:
                device.getInput().setEnergyUsage(builder.toString().trim());
                break;
            case PRICE:
                device.getPrice().setValue(Double.parseDouble(builder.toString().trim()));
                break;
            case COMPANY:
                device.getManufacture().setCompany(builder.toString().trim());
                break;
            case ORIGIN:
                device.getManufacture().setOrigin(builder.toString().trim());
                break;
            case DATE:
                device.getManufacture().setDate(builder.toString().trim());
                break;
            case DEVICE:
                devices.add(device);
                initDevice();
                break;
            case DEVICES:
                //ignore
                break;
        }
        builder.setLength(0);
        log.debug("Ended element parsing with localName = {}", localName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        builder.append(new String(ch, start, length));
    }

    private void initDevice() {
        device = new Device();
        device.setManufacture(new Manufacture());
        device.setInput(new Input());
        device.setPrice(new Price());
    }
}
