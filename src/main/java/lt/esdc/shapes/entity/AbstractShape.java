package lt.esdc.shapes.entity;

import lt.esdc.shapes.observer.ShapeObserver;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractShape {
    protected final long id;
    private final List<ShapeObserver> observers = new ArrayList<>();

    protected AbstractShape(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void addObserver(ShapeObserver observer) {
        if (observer != null) {
            this.observers.add(observer);
        }
    }

    public void removeObserver(ShapeObserver observer) {
        if (observer != null) {
            this.observers.remove(observer);
        }
    }

    protected void notifyObservers() {
        for (ShapeObserver observer : this.observers) {
            observer.update(this);
        }
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}
