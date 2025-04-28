package com.lab.factory;


import com.lab.entity.Oval;
import com.lab.entity.Point;
import com.lab.exception.InvalidShapeException;
import com.lab.pool.ShapeWarehouse;
import com.lab.validator.OvalValidator;

public class OvalFactory {
    public static Oval createOval(String data) throws InvalidShapeException {
        // Парсинг строки, например: "x1,y1;x2,y2"
        String[] parts = data.split(";");
        if (parts.length != 2) {
            throw new InvalidShapeException("Некорректный формат данных для овала");
        }
        Point p1 = parsePoint(parts[0].trim());
        Point p2 = parsePoint(parts[1].trim());
        Oval oval = new Oval(p1, p2);
        OvalValidator.validate(oval);
        oval.addObserver(ShapeWarehouse.getInstance());
        ShapeWarehouse.getInstance().update(oval, null);
        return oval;

    }

    private static Point parsePoint(String str) throws InvalidShapeException {
        // Реализация парсинга точки через регулярные выражения или Split
        // Например: "1.0,2.0" → x=1.0, y=2.0
        String[] coords = str.split(",");
        if (coords.length != 2) {
            throw new InvalidShapeException("Неккоректный формат точки " + str);
        }
        try {
            double x = Double.parseDouble(coords[0].trim());
            double y = Double.parseDouble(coords[1].trim());
            return new Point(x, y);
        } catch (NumberFormatException e) {
            throw new InvalidShapeException("Некорректное числовое значение" + str, e);
        }
    }
}