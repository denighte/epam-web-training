package by.radchuk.task4.parser.device;

import by.radchuk.task4.exception.ParseException;
import by.radchuk.task4.model.Connector;
import by.radchuk.task4.model.Device;
import by.radchuk.task4.model.DeviceCritical;
import by.radchuk.task4.model.DeviceType;
import by.radchuk.task4.model.Manufacture;
import by.radchuk.task4.model.Input;
import by.radchuk.task4.model.Price;
import by.radchuk.task4.parser.AbstractTagHandler;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.EmptyStackException;


/**
 * AbstractTagHandler impl for devices xml parsing.
 */
@Slf4j
public class DeviceTagHandler implements AbstractTagHandler {
    /**
     * Devices storage.
     */
    private List<Device> devices;
    /**
     * Current parsing device.
     */
    private Device device;
    /**
     * Tag appearance stack.
     */
    private Stack<Tag> tagStack;

    /**
     * Returns parsed data to parsers.
     * @return parsed devices list.
     */
    @Override
    public Object getData() {
        return devices;
    }

    /**
     * default constructor.
     */
    DeviceTagHandler() {
        devices = new ArrayList<>();
        initDevice();
        tagStack = new Stack<>();
    }

    /**
     * handles open tag.
     * @param tag open tag name.
     * @param attributes tag attributes map.
     * @throws ParseException in case parse errors.
     */
    @Override
    public void onOpen(@NonNull final String tag,
                       @NonNull final Map<String, String> attributes)
                                        throws ParseException {
        log.debug("Started open tag handling, tag name = {}.", tag);
        Tag tagType;
        try {
            tagType = Tag.forName(tag);
        } catch (IllegalStateException exception) {
            throw new ParseException("unknown opening tag!"
                    + " tag name = " + tag, exception);
        }
        log.debug("Handling tag attributes");
        switch (tagType) {
            case DEVICE:
                String id = attributes.get("id");
                String critical = attributes.get("critical");
                String type = attributes.get("type");
                if (id == null || type == null) {
                   throw new ParseException("not enough attributes for tag!"
                           + " tag name = " + tag);
                }
                device.setId(Long.parseLong(id));
                device.setType(DeviceType.fromValue(type));
                if (critical != null) {
                    device.setCritical(DeviceCritical.fromValue(critical));
                }
                break;
            case PRICE:
                String currency = attributes.get("currency");
                if (currency != null) {
                    device.getPrice().setCurrency(currency);
                }
                break;
            default:
                //for checkstyle.
                break;
        }
        tagStack.push(tagType);
        log.debug("Ended open tag handling.");
    }

    /**
     * handles tag text data.
     * @param text text data.
     * @throws ParseException in case parse errors.
     */
    @Override
    public void onText(@NonNull final String text)
                                throws ParseException {
        try {
            tagStack.peek();
        } catch (EmptyStackException exception) {
            throw new ParseException("Unexpected text outside tags!",
                                     exception);
        }
        log.debug("Handling text information of the tag, tag name = {}.",
                  tagStack.peek().getName());
        switch (tagStack.peek()) {
            case NAME:
                device.setName(text);
                break;
            case DESCRIPTION:
                device.setDescription(text);
                break;
            case RATING:
                device.setRating(Double.parseDouble(text));
                break;
            case CONNECTOR:
                device.getInput().setConnector(Connector.fromValue(text));
                break;
            case ENERGY_USAGE:
                device.getInput().setEnergyUsage(text);
                break;
            case PRICE:
                device.getPrice().setValue(Double.parseDouble(text));
                break;
            case COMPANY:
                device.getManufacture().setCompany(text);
                break;
            case ORIGIN:
                device.getManufacture().setOrigin(text);
                break;
            case DATE:
                device.getManufacture().setDate(text);
                break;
            default:
                //for checkstyle.
                break;
        }
        log.debug("Ended tag text information handling.");
    }

    /**
     * Handles close tag.
     * @param tag close tag name.
     * @throws ParseException in case parse errors.
     */
    @Override
    public void onClose(@NonNull final String tag) throws ParseException {
        log.debug("Handling closing tag, tag name = {}.", tag);
        try {
            if (!tagStack.peek().getName().equals(tag)) {
                throw new ParseException("unexpected closing tag!"
                        + " tag name = " + tag);
            }
        } catch (EmptyStackException exception) {
            throw new ParseException("unexpected closing tag!"
                    + " tag name = " + tag, exception);
        }
        switch (tagStack.pop()) {
            case DEVICE:
                devices.add(device);
                initDevice();
                break;
            default:
                //for checkstyle.
                break;
        }
        log.debug("Ended closing tag handling.");
    }

    /**
     * init new temp(parsing) device object.
     */
    private void initDevice() {
        device = new Device();
        device.setManufacture(new Manufacture());
        device.setInput(new Input());
        device.setPrice(new Price());
        device.getPrice().setCurrency("usd");
        device.setCritical(DeviceCritical.FALSE);
    }

    /**
     * xml tags.
     */
    private enum Tag {
        /**
         * <devices> tag.
         */
        DEVICES("devices"),
        /**
         * <device> tag.
         */
        DEVICE("device"),
        /**
         * <name> tag.
         */
        NAME("name"),
        /**
         * <description> tag.
         */
        DESCRIPTION("description"),
        /**
         * <input> tag.
         */
        INPUT("input"),
        /**
         * <connector> tag.
         */
        CONNECTOR("connector"),
        /**
         * <energy-usage> tag.
         */
        ENERGY_USAGE("energy-usage"),
        /**
         * <rating> tag.
         */
        RATING("rating"),
        /**
         * <price> tag.
         */
        PRICE("price"),
        /**
         * <manufacture> tag.
         */
        MANUFACTURE("manufacture"),
        /**
         * <company> tag.
         */
        COMPANY("company"),
        /**
         * <origin> tag.
         */
        ORIGIN("origin"),
        /**
         * <date> tag.
         */
        DATE("date");

        /**
         * tag name.
         */
        @Getter
        private String name;

        /**
         * default constructor.
         * @param tagName tag name.
         */
        Tag(final String tagName) {
            name = tagName;
        }

        /**
         * get Tag instance from tag name.
         * @param tagName tag name string representation.
         * @return Tag instance.
         */
        static Tag forName(final String tagName) {
            for (Tag tag : Tag.values()) {
                if (tag.name.equals(tagName)) {
                    return tag;
                }
            }
            throw new IllegalArgumentException(tagName);
        }
    }
}
