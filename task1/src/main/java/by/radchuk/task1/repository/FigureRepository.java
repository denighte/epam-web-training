package by.radchuk.task1.repository;

import by.radchuk.task1.criteria.Criteria;
import by.radchuk.task1.entity.GeometryObject;
import by.radchuk.task1.observer.Observer;
import java.util.List;

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
     * remove object(s) by criteria.
     * @param criteria
     * @see Criteria
     */
    void remove(Criteria<T> criteria);

    /**
     * find first object matching condition.
     * @param criteria
     * @return object satisfying the criteria.
     * @see Criteria
     */
    List<T> find(Criteria<T> criteria);
}
