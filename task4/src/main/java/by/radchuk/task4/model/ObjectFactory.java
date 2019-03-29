
package by.radchuk.task4.model;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the by.radchuk.task4.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: by.radchuk.task4.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Device }
     * 
     */
    public Device createDevices() {
        return new Device();
    }

    /**
     * Create an instance of {@link Device }
     * 
     */
    public Device createDevicesDevice() {
        return new Device();
    }

    /**
     * Create an instance of {@link Manufacture }
     * 
     */
    public Manufacture createManufacture() {
        return new Manufacture();
    }

    /**
     * Create an instance of {@link Input }
     * 
     */
    public Input createInput() {
        return new Input();
    }

    /**
     * Create an instance of {@link Price }
     * 
     */
    public Price createPrice() {
        return new Price();
    }

}
