
package by.radchuk.task4.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for price complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="price">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema/dv>priceValue">
 *       &lt;attribute name="currency">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="usd"/>
 *             &lt;enumeration value="eur"/>
 *             &lt;enumeration value="byn"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "price", namespace = "http://www.w3.org/2001/XMLSchema/dv", propOrder = {
    "value"
})
@Data
public class Price {

    /**
     * value property.
     */
    @XmlValue
    private double value;

    /**
     * currency attribute.
     */
    @XmlAttribute(name = "currency")
    private String currency;
}
