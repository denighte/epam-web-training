package by.radchuk.task1.repository;

import by.radchuk.task1.entity.GeometryObject;
import by.radchuk.task1.observer.IObserver;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * repository pattern interface.
 * @param <T> geometry figure
 * @param <V> geometry figure special data
 */
public interface FigureRepository<T extends GeometryObject, V>
        extends IObserver {
    /**
     * add object ot repository.
     * @param object object to add.
     */
    void add(T object);

    /**
     * remove object from repository.
     * @param object object to remove.
     */
    void remove(T object);

    /**
     * remove object(s) by condition.
     * @param condition predicate, return true if remove otherwise false.
     */
    void remove(Predicate<T> condition);

    /**
     * find first object matching condition.
     * @param condition predicate, return true to get otherwise false.
     * @return object satisfying the condition.
     */
    T find(Predicate<T> condition);

    /**
     * find all objects matching condition.
     * @param condition predicate, return true to get otherwise false.
     * @return List of object satisfying the condition.
     */
    List<T> findAll(Predicate<T> condition);
    /**
     * returns sorted list of objects.
     * @param comparator comparator interface
     * @return list of objects
     */
    List<T> sortBy(Comparator<T> comparator);
    /**
     * find object by id.
     * @param id id of the object.
     * @return object with such id, null otherwise.
     */
    V getCubeData(int id);
}
