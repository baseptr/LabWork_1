package com.lab.factory;


import com.lab.entity.Oval;
import com.lab.entity.Point;
import com.lab.exception.InvalidShapeException;

public class OvalFactory {
    public static Oval createOval(String data) throws InvalidShapeException {
        // Парсинг строки, например: "x1,y1;x2,y2"
        String[] parts = data.split(";");
        if (parts.length != 2) {
            throw new InvalidShapeException("Некорректный формат данных для овала");
        }
        Point p1 = parsePoint(parts[0]);
        Point p2 = parsePoint(parts[1]);
        return new Oval(p1, p2);
    }

    private static Point parsePoint(String str) {
        // Реализация парсинга точки через регулярные выражения или Split
        // Например: "1.0,2.0" → x=1.0, y=2.0
        String[] coords = str.split(",");
        double x = Double.parseDouble(coords[0]);
        double y = Double.parseDouble(coords[1]);
        return new Point(x, y);
    }
}