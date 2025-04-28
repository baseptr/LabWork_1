package com.lab.service;

import com.lab.entity.Oval;
import com.lab.entity.Point;

public class OvalService {
    public double calculatePerimeter(Oval oval) {
        Point p1 = oval.getPointX();
        Point p2 = oval.getPointY();
        double dx = Math.abs(p1.x() - p2.x());
        double dy = Math.abs(p1.y() - p2.y());
        double a = dx / 2;
        double b = dy / 2;
        return 2 * Math.PI * Math.sqrt((a * a + b * b) / 2);
    }

    public double calculateArea(Oval oval) {
        Point p1 = oval.getPointX();
        Point p2 = oval.getPointY();
        double dx = Math.abs(p1.x() - p2.x());
        double dy = Math.abs(p1.y() - p2.y());
        double a = dx / 2;
        double b = dy / 2;
        return Math.PI * a * b;
    }

    public boolean isCircle(Oval oval) {
        Point p1 = oval.getPointX();
        Point p2 = oval.getPointY();
        double a = Math.abs(p1.x() - p2.x());
        double b = Math.abs(p1.y() - p2.y());
        return a == b;
    }
}
