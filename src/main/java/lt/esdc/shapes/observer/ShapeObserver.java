package lt.esdc.shapes.observer;

import lt.esdc.shapes.entity.AbstractShape;

public interface ShapeObserver {
    void update(AbstractShape shape);
}