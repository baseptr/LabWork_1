package It.esdc.shapes.service;

import It.esdc.shapes.entity.Oval;
import It.esdc.shapes.entity.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OvalService {
    private static final Logger logger = LoggerFactory.getLogger(OvalService.class);
    private static final double EPSILON = 1E-6; // Погрешность для сравнения

    // Метод для извлечения полуосей (избежание дублирования)
    private double[] getSemiAxes(Oval oval) {
        Point p1 = oval.getPointX();
        Point p2 = oval.getPointY();
        // Используем Math.abs на случай, если p1.x > p2.x или p1.y > p2.y
        double axisXLength = Math.abs(p1.x() - p2.x());
        double axisYLength = Math.abs(p1.y() - p2.y());
        double a = axisXLength / 2.0; // Полуось X
        double b = axisYLength / 2.0; // Полуось Y
        logger.trace("Вычисленные полуоси для овала id={}: a={}, b={}", oval.getId(), a, b);
        return new double[]{a, b};
    }

    public double calculatePerimeter(Oval oval) {
        logger.trace("Вычисленный периметр овала id={}", oval.getId());
        double[] semiAxes = getSemiAxes(oval);
        double a = semiAxes[0];
        double b = semiAxes[1];

        // Используем приближенную формулу Рамануджана (более точная, чем среднее квадратичное)
        // P ≈ π [ 3(a+b) - sqrt((3a+b)(a+3b)) ]
        // Или можно оставить предыдущую формулу: P ≈ 2 * π * sqrt((a^2 + b^2) / 2)
        // Оставим исходную формулу для простоты, как было в задании
        double perimeter = 2 * Math.PI * Math.sqrt((a * a + b * b) / 2.0);

        logger.debug("Вычислений периметр овала id={}: {}", oval.getId(), perimeter);
        return perimeter;
    }

    public double calculateArea(Oval oval) {
        logger.trace("Вычисленная площадь овала id={}", oval.getId());
        double[] semiAxes = getSemiAxes(oval);
        double a = semiAxes[0];
        double b = semiAxes[1];
        double area = Math.PI * a * b;
        logger.debug("Вычисленная площадь овала id={}: {}", oval.getId(), area);
        return area;
    }

    public boolean isCircle(Oval oval) {
        logger.trace("Проверка если овал id={} это круг", oval.getId());
        double[] semiAxes = getSemiAxes(oval);
        double a = semiAxes[0];
        double b = semiAxes[1];
        // Сравниваем полуоси с учетом погрешности
        boolean isCircle = Math.abs(a - b) < EPSILON;
        logger.debug("Овал id={} это круг: {}", oval.getId(), isCircle);
        return isCircle;
    }
}
