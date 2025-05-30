package lt.esdc.shapes.factory;

import lt.esdc.shapes.entity.Oval;
import lt.esdc.shapes.entity.Point;
import lt.esdc.shapes.exception.InvalidShapeException;
import java.util.UUID;

public class OvalFactory implements ShapeFactory {
    private final double EPSILON = 1e-9;

    @Override
    public Oval createShape(Point p1, Point p2) throws InvalidShapeException {
        if (Math.abs(p1.x() - p2.x()) < EPSILON || Math.abs(p1.y() - p2.y()) < EPSILON) {
            throw new InvalidShapeException(
                    "Points doesn't forms correct limited rectangle for Oval: " + p1 + ", " + p2);
        }

        long id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        return new Oval(id, p1, p2);
    }
}