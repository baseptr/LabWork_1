package com.lab.validator;

import com.lab.entity.Oval;
import com.lab.entity.Point;
import com.lab.exception.InvalidShapeException;

public class OvalValidator {
    public static void validate(Oval oval) throws InvalidShapeException {
        Point p1 = oval.getPoint1();
        Point p2 = oval.getPoint2();
        // Проверка, что прямоугольник не вырожден
        if (p1.getX() == p2.getX() || p1.getY() == p2.getY()) {
            throw new InvalidShapeException("Точки формируют вырожденный прямоугольник");
        }
    }
}