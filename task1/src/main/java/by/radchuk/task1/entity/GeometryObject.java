package by.radchuk.task1.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base class for all geometry objects.
 */
@Data
@NoArgsConstructor
public abstract class GeometryObject {
    /**
     * GeometryObject constructor.
     * @param objectName object name
     */
    public GeometryObject(final String objectName) {
        this.id = 0;
        this.name = objectName;
    }
    /**
     * unique id of the object.
     */
    private int id;
    /**
     * name of the object.
     */
    private String name;
}
