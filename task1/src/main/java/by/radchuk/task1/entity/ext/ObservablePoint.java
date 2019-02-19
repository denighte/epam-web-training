package by.radchuk.task1.entity.ext;

import by.radchuk.task1.entity.Point;
import by.radchuk.task1.observer.IObservable;
import by.radchuk.task1.observer.IObserver;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Point extension.
 * Implements a geometry Point model in three-dimensional space.
 * Can be stored in a repository-observer.
 * Notifies observer every time when happens any changes to the data
 * @author Dmitry Radchuk
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class ObservablePoint extends Point implements IObservable {
    /**
     * observer reference.
     */
    @Setter
    @Getter
    private IObserver observer;
    /**
     * ObservablePoint decorator constuctor.
     * @param point base class.
     */
    public ObservablePoint(final Point point) {
        super(point);
    }
    /**
     * set the new x axis coordinate and notifies the observer.
     * @param newX new x coordinate
     */
    @Override
    public void setX(final double newX) {
        super.setX(newX);
        log.debug(
                "Point with id={} has been changed by new value x={}",
                this.getId(),
                this.getX());
        this.notifyObserver();
    }
    /**
     * set the new y axis coordinate and notifies the observer.
     * @param newY new y coordinate
     */
    @Override
    public void setY(final double newY) {
        super.setY(newY);
        log.debug(
                "Point with id={} has been changed by new value y={}",
                this.getId(),
                this.getY());
        this.notifyObserver();
    }
    /**
     * set the new z axis coordinate and notifies the observer.
     * @param newZ new z coordinate
     */
    @Override
    public void setZ(final double newZ) {
        super.setZ(newZ);
        log.debug(
                "Point with id={} has been changed by new value z={}",
                this.getId(),
                this.getZ());
        this.notifyObserver();
    }
    /**
     * notifies observer that the data has changed.
     */
    @Override
    public void notifyObserver() {
        log.debug("Notifying observer of the Point with id={}", this.getId());
        this.observer.update(this.getId());
    }
}
