package by.radchuk.task4.parser.stax;

import by.radchuk.task4.model.Connector;
import by.radchuk.task4.model.Manufacture;
import by.radchuk.task4.model.Input;
import by.radchuk.task4.model.Price;
import by.radchuk.task4.model.Device;
import by.radchuk.task4.model.DeviceCritical;
import by.radchuk.task4.model.DeviceType;
import by.radchuk.task4.parser.Tag;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Slf4j
public class DeviceStreamReader extends StreamReaderDelegate {
    Stack<Tag> stack = new Stack<>();
    @Getter
    private List<Device> devices;
    private Device device;
    public DeviceStreamReader(final XMLStreamReader reader) {
        super(reader);
        devices = new ArrayList<>();
        initDevice();
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
                    stack.push(Tag.forName(getLocalName()));
                    log.debug("Start Element: {}", getLocalName());
                    switch (stack.peek()) {
                        case DEVICE:
                            for (int i = 0; i < getAttributeCount(); ++i) {
                                String attribute = getAttributeName(i).getLocalPart();
                                switch (attribute) {
                                    case "id":
                                        device.setId(Long.parseLong(getAttributeValue(i)));
                                        break;
                                    case "critical":
                                        device.setCritical(DeviceCritical.fromValue(getAttributeValue(i)));
                                        break;
                                    case "type":
                                        device.setType(DeviceType.fromValue(getAttributeValue(i)));
                                        break;
                                }
                            }
                            break;
                        case PRICE:
                            for (int i = 0; i < getAttributeCount(); ++i) {
                                String attribute = getAttributeName(i).getLocalPart();
                                switch (attribute) {
                                    case "currency":
                                        device.getPrice().setCurrency(getAttributeValue(i));
                                        break;
                                }
                            }
                    }
                    break;
                case CHARACTERS:
                    if (isWhiteSpace()) {
                        break;
                    }
                    log.debug("Text: {}", getText());
                    switch (stack.pop()) {
                        case NAME:
                            device.setName(getText());
                            break;
                        case DESCRIPTION:
                            device.setDescription(getText());
                            break;
                        case RATING:
                            device.setRating(Double.parseDouble(getText()));
                            break;
                        case CONNECTOR:
                            device.getInput().setConnector(Connector.fromValue(getText()));
                            break;
                        case ENERGY_USAGE:
                            device.getInput().setEnergyUsage(getText());
                            break;
                        case PRICE:
                            device.getPrice().setValue(Double.parseDouble(getText()));
                            break;
                        case COMPANY:
                            device.getManufacture().setCompany(getText());
                            break;
                        case ORIGIN:
                            device.getManufacture().setOrigin(getText());
                            break;
                        case DATE:
                            device.getManufacture().setDate(getText());
                            break;
                        case DEVICE:
                            //ignore
                            break;
                        case DEVICES:
                            //ignore
                            break;
                    }
                    break;
                case END_ELEMENT:
                    switch (Tag.forName(getName().getLocalPart())) {
                        case DEVICE:
                            devices.add(device);
                            initDevice();
                            break;
                    }
                    log.debug("End Element", getName());
                    break;
                case END_DOCUMENT:
                    log.debug("Ended document parsing.");
                    break;
            }
        } catch (Exception ignore) {
            log.debug("Error during parsing!, {}", ignore);
            //ignoring the error, leaving it for validator.
        }
        return super.next();
    }

    private void initDevice() {
        device = new Device();
        device.setManufacture(new Manufacture());
        device.setInput(new Input());
        device.setPrice(new Price());
        device.getPrice().setCurrency("usd");
        device.setCritical(DeviceCritical.FALSE);
    }
}
