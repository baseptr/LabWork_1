package lt.esdc.shapes.service;

import lt.esdc.shapes.entity.Oval;
import lt.esdc.shapes.entity.Point;

public class OvalService {

    private static final double EPSILON = 1e-9;

    public double[] getSemiAxes(Oval oval) {
        Point p1 = oval.getPointX();
        Point p2 = oval.getPointY();

        double semiMajorAxisA = Math.abs(p1.x() - p2.x()) / 2.0;
        double semiMinorAxisB = Math.abs(p1.y() - p2.y()) / 2.0;

        if (semiMajorAxisA < semiMinorAxisB) {
            double temp = semiMajorAxisA;
            semiMajorAxisA = semiMinorAxisB;
            semiMinorAxisB = temp;
        }
        return new double[]{semiMajorAxisA, semiMinorAxisB};
    }

    public double calculateArea(Oval oval) {
        double[] semiAxes = getSemiAxes(oval);
        double a = semiAxes[0];
        double b = semiAxes[1];
        return Math.PI * a * b;
    }

    public double calculatePerimeter(Oval oval) {
        double[] semiAxes = getSemiAxes(oval);
        double a = semiAxes[0];
        double b = semiAxes[1];

        if (Math.abs(a) < EPSILON && Math.abs(b) < EPSILON) return 0.0;
        if (Math.abs(a - b) < EPSILON) {
            return 2 * Math.PI * a;
        }

        double term1 = 3 * (a + b);
        double term2 = Math.sqrt((3 * a + b) * (a + 3 * b));
        return Math.PI * (term1 - term2);
    }

    public boolean isCircle(Oval oval) {
        double[] semiAxes = getSemiAxes(oval);
        double a = semiAxes[0];
        double b = semiAxes[1];
        return Math.abs(a - b) < EPSILON;
    }


}