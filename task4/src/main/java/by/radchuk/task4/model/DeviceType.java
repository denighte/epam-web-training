
package by.radchuk.task4.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for device-type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="device-type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="audio"/>
 *     &lt;enumeration value="video"/>
 *     &lt;enumeration value="input"/>
 *     &lt;enumeration value="cpu"/>
 *     &lt;enumeration value="motherboard"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "device-type", namespace = "http://www.w3.org/2001/XMLSchema/dv")
@XmlEnum
public enum DeviceType {

    @XmlEnumValue("audio")
    AUDIO("audio"),
    @XmlEnumValue("video")
    VIDEO("video"),
    @XmlEnumValue("input")
    INPUT("input"),
    @XmlEnumValue("cpu")
    CPU("cpu"),
    @XmlEnumValue("motherboard")
    MOTHERBOARD("motherboard");
    private final String value;

    DeviceType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DeviceType fromValue(String v) {
        for (DeviceType c: DeviceType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
