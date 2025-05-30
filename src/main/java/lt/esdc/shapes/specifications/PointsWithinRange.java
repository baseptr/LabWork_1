package lt.esdc.shapes.specifications;

import lt.esdc.shapes.entity.Oval;
import lt.esdc.shapes.entity.Point;

public class PointsWithinRange implements Specification<Oval> {
    private final double minX, maxX, minY, maxY;

    public PointsWithinRange(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    private boolean isPointInRange(Point p) {
        if (p == null) return false;
        return p.x() >= minX && p.x() <= maxX && p.y() >= minY && p.y() <= maxY;
    }

    @Override
    public boolean isSatisfiedBy(Oval oval) {
        if (oval == null) return false;
        return isPointInRange(oval.getPointX()) && isPointInRange(oval.getPointY());
    }
}