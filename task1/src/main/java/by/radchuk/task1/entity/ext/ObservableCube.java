package by.radchuk.task1.entity.ext;

import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.entity.Point;
import by.radchuk.task1.observer.IObservable;
import by.radchuk.task1.observer.IObserver;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Cube extension.
 * Implements a geometry Cube model in three-dimensional space.
 * Can be stored in a repository-observer.
 * Notifies observer every time when happens any changes to the data
 * @author Dmitry Radchuk
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class ObservableCube extends CheckedCube
        implements IObservable, IObserver {
    /**
     * observer reference.
     */
    @Setter
    @Getter
    private IObserver observer;

    /**
     * ObservableCube decorator constructor.
     * @param cube base class.
     */
    public ObservableCube(final Cube cube) {
        super(cube);
    }

    /**
     * center point setter override to register observable point.
     * @param point
     */
    @Override
    public void setCenterPoint(final Point point) {
        super.setCenterPoint(point);
    }
    /**
     * notifies observer that the data has changed.
     */
    @Override
    public void notifyObserver() {
        log.debug(
                "Notifying observer of the Cube with id={}"
                        + " new Point id={}",
                this.getId(),
                this.getCenterPoint().getId());
        observer.update(this.getId());
    }
    /**
     * call every time when the data changes.
     */
    @Override
    public void update(final int id) {
        log.debug(
                "Cube with id={} has been changed",
                this.getId());
        this.notifyObserver();
    }
    /**
     * sets new edge length and notifies the observer.
     * @param length new edge length of the cube.
     */
    @Override
    public void setEdgeLength(final double length) {
        super.setEdgeLength(length);
        log.debug(
                "Cube with id={} edge length has been changed,"
                        + " new edge={}",
                this.getId(),
                length);
        this.notifyObserver();
    }
}

