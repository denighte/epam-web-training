
package by.radchuk.task4.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for input complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="input">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="connector" type="{http://www.w3.org/2001/XMLSchema/dv}connector"/>
 *         &lt;element name="energy-usage" type="{http://www.w3.org/2001/XMLSchema/dv}energy-usage"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "input", namespace = "http://www.w3.org/2001/XMLSchema/dv", propOrder = {

})
public class Input {

    /**
     * connector property.
     */
    @XmlElement(namespace = "http://www.w3.org/2001/XMLSchema/dv", required = true)
    @XmlSchemaType(name = "string")
    private Connector connector;
    /**
     * energy usage property.
     */
    @XmlElement(name = "energy-usage", namespace = "http://www.w3.org/2001/XMLSchema/dv", required = true)
    private String energyUsage;

}
