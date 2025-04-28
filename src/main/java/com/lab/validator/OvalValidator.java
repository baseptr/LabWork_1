package com.lab.validator;

import com.lab.entity.Oval;
import com.lab.entity.Point;
import com.lab.exception.InvalidShapeException;


public class OvalValidator {
    private static final double EPSILON = 1E-9;
    public static void validate(Oval oval) throws InvalidShapeException {
        if (oval == null) {
            throw new InvalidShapeException("Объект Oval не может быть null");
        }
        Point p1 = oval.getPointX();
        Point p2 = oval.getPointY();

        if (p1 == null || p2 == null) {
            throw new InvalidShapeException("Точки овала не могут быть null");
        }

        // Проверка, что точки не совпадают по обеим координатам
        boolean sameX = Math.abs(p1.x() - p2.x()) < EPSILON;
        boolean sameY = Math.abs(p1.y() - p2.y()) < EPSILON;

        if (sameX && sameY) {
            throw new InvalidShapeException("Точки овала совпадают: " + p1);
        }

        // Проверка, что точки не лежат на одной горизонтальной или вертикальной линии
        // (т.е. образуют прямоугольник с ненулевой площадью)
        if (sameX || sameY) {
            throw new InvalidShapeException("Овал вырожден в линию (точки на одной оси): p1=" + p1 + ", p2=" + p2);
        }

        // Дополнительные проверки, если нужны (например, на максимальные/минимальные координаты)
    }

}