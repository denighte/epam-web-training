
package by.radchuk.task4.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for connector.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="connector">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="jack"/>
 *     &lt;enumeration value="PCI"/>
 *     &lt;enumeration value="PCI Express"/>
 *     &lt;enumeration value="socket"/>
 *     &lt;enumeration value="HDMI"/>
 *     &lt;enumeration value="USB"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "connector", namespace = "http://www.w3.org/2001/XMLSchema/dv")
@XmlEnum
public enum Connector {

    @XmlEnumValue("jack")
    JACK("jack"),
    PCI("PCI"),
    @XmlEnumValue("PCI Express")
    PCI_EXPRESS("PCI Express"),
    @XmlEnumValue("socket")
    SOCKET("socket"),
    HDMI("HDMI"),
    USB("USB");
    private final String value;

    Connector(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Connector fromValue(String v) {
        for (Connector c: Connector.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
