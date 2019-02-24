package by.radchuk.task1.repository;

import by.radchuk.task1.entity.GeometryObject;
import by.radchuk.task1.observer.Observer;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * repository pattern interface.
 * @param <T> geometry figure.
 */
public interface FigureRepository<T extends GeometryObject>
        extends Observer {
    /**
     * add object ot repository.
     * @param object object to add.
     */
    void add(T object);

    /**
     * remove object(s) by predicate.
     * @param condition predicate, return true to remove, otherwise false
     */
    void remove(Predicate<T> condition);

    /**
     * remove object by id.
     * @param id id of the object.
     */
    void remove(int id);

    /**
     * find first object matching condition.
     * @param condition predicate, return true to add, otherwise false
     * @return object satisfying the criteria.
     */
    List<T> find(Predicate<T> condition);

    /**
     * find object by id.
     * @param id id of the object.
     * @return object matching id.
     */
    T find(int id);

    /**
     * sorts objects by comparator.
     * @param comparator comparator
     * @return sorted list of objects.
     */
    List<T> sort(Comparator<T> comparator);
}
