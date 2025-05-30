package lt.esdc.shapes.factory;

import lt.esdc.shapes.entity.AbstractShape;
import lt.esdc.shapes.entity.Point;
import lt.esdc.shapes.exception.InvalidShapeException;

public interface ShapeFactory {
    AbstractShape createShape(Point p1, Point p2) throws InvalidShapeException;
}