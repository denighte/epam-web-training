
package by.radchuk.task4.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for manufacture complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="manufacture">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="company" type="{http://www.w3.org/2001/XMLSchema/dv}company"/>
 *         &lt;element name="origin" type="{http://www.w3.org/2001/XMLSchema/dv}origin"/>
 *         &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema/dv}date"/>
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
@XmlType(name = "manufacture", namespace = "http://www.w3.org/2001/XMLSchema/dv", propOrder = {

})
public class Manufacture {

    /**
     * company property.
     */
    @XmlElement(namespace = "http://www.w3.org/2001/XMLSchema/dv", required = true)
    private String company;
    /**
     * origin property.
     */
    @XmlElement(namespace = "http://www.w3.org/2001/XMLSchema/dv", required = true)
    private String origin;
    /**
     * date property.
     */
    @XmlElement(namespace = "http://www.w3.org/2001/XMLSchema/dv", required = true)
    @XmlSchemaType(name = "date")
    private String date;
}
