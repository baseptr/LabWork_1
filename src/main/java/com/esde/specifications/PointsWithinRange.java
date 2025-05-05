package com.esde.specifications;

import com.esde.entity.Oval;
import com.esde.entity.Point;

public class PointsWithinRange implements Specification<Oval> {
    @Override
    public boolean isSatisfiedBy(Oval oval) {
        Point p1 = oval.getPointX();
        Point p2 = oval.getPointY();
        return (p1.x() >= 0 && p1.x() <= 10) &&
                (p1.y() >= 0 && p1.y() <= 10) &&
                (p2.x() >= 0 && p2.x() <= 10) &&
                (p2.y() >= 0 && p2.y() <= 10);
    }
}