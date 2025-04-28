package com.lab.validator;

import com.lab.entity.Oval;
import com.lab.entity.Point;
import com.lab.exception.InvalidShapeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OvalValidator {
    //погрешность для сравнения double
    private static final double EPSILON = 1E-6;
    private static final Logger logger = LoggerFactory.getLogger(OvalValidator.class);
    public static void validate(Oval oval) throws InvalidShapeException {
        logger.trace("Валидация овала id={}", oval != null ? oval.getId() : "null");
        if (oval == null) {
            throw new InvalidShapeException("Объект овал не может быть null");
        }

        Point p1 = oval.getPointX();
        Point p2 = oval.getPointY();

        if (p1 == null || p2 == null) {
            throw new InvalidShapeException("Точки овала не могут быть null. Овал id=" + oval.getId());
        }

        logger.trace("Валидация точек: p1={}, p2={}", p1, p2);

        // Math.abs для корректной работы с отрицательными координатами
        boolean sameX = Math.abs(p1.x() - p2.x()) < EPSILON;
        boolean sameY = Math.abs(p1.y() - p2.y()) < EPSILON;

        // Проверка на совпадение точек
        if (sameX && sameY) {
            String message = "Точки овала идентичны: " + p1 + ". Овал id=" + oval.getId();
            logger.warn(message);
            throw new InvalidShapeException(message);
        }

        // Проверка на вырожденность в линию
        if (sameX || sameY) {
            String message = "Овал дегенеративный (точки одинаковы " + (sameX ? "вертикаль" : "горизонталь") + " линия): p1=" + p1 + ", p2=" + p2 + ". Овал id=" + oval.getId();
            logger.warn(message);
            throw new InvalidShapeException(message);
        }
        logger.trace("Овал id={} геометрия корректна.", oval.getId());
    }

}