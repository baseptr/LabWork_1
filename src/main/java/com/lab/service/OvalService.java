package com.lab.service;

import com.lab.entity.Oval;

public class OvalService {
    public double calculatePerimeter(Oval oval) {
        double a = Math.abs(oval.getPoint1().getX() - oval.getPoint2().getX()) / 2;
        double b = Math.abs(oval.getPoint1().getY() - oval.getPoint2().getY()) / 2;
        return 2 * Math.PI * Math.sqrt((a * a + b * b) / 2);
    }

    public double calculateArea(Oval oval) {
        double a = Math.abs(oval.getPoint1().getX() - oval.getPoint2().getX()) / 2;
        double b = Math.abs(oval.getPoint1().getY() - oval.getPoint2().getY()) / 2;
        return Math.PI * a * b;
    }

    public boolean isCircle(Oval oval) {
        double a = Math.abs(oval.getPoint1().getX() - oval.getPoint2().getX());
        double b = Math.abs(oval.getPoint1().getY() - oval.getPoint2().getY());
        return a == b;
    }
}
