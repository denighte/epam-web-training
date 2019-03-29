
package by.radchuk.task4.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema/dv}name"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema/dv}description"/>
 *         &lt;element name="input" type="{http://www.w3.org/2001/XMLSchema/dv}input"/>
 *         &lt;element name="rating" type="{http://www.w3.org/2001/XMLSchema/dv}rating"/>
 *         &lt;element name="price" type="{http://www.w3.org/2001/XMLSchema/dv}price"/>
 *         &lt;element name="manufacture" type="{http://www.w3.org/2001/XMLSchema/dv}manufacture"/>
 *       &lt;/all>
 *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema/dv}device-type" />
 *       &lt;attribute name="critical" type="{http://www.w3.org/2001/XMLSchema/dv}device-critical" default="false" />
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema/dv}device-id" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
public class Device {

    @XmlElement(namespace = "http://www.w3.org/2001/XMLSchema/dv", required = true)
    private String name;
    @XmlElement(namespace = "http://www.w3.org/2001/XMLSchema/dv", required = true)
    private String description;
    @XmlElement(namespace = "http://www.w3.org/2001/XMLSchema/dv", required = true)
    private Input input;
    @XmlElement(namespace = "http://www.w3.org/2001/XMLSchema/dv", required = true)
    private double rating;
    @XmlElement(namespace = "http://www.w3.org/2001/XMLSchema/dv", required = true)
    private Price price;
    @XmlElement(namespace = "http://www.w3.org/2001/XMLSchema/dv", required = true)
    private Manufacture manufacture;
    @XmlAttribute(name = "type", required = true)
    private DeviceType type;
    @XmlAttribute(name = "critical")
    private DeviceCritical critical;
    @XmlAttribute(name = "id", required = true)
    private long id;
}