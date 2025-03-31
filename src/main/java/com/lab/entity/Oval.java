package com.lab.entity;

public class Oval {
    private final long id;
    private final Point point1;
    private final Point point2;

    public Oval(Point p1, Point p2) {
        this.id = System.currentTimeMillis();
        this.point1 = p1;
        this.point2 = p2;
    }

    public long getId() { return id; }
    public Point getPoint1() { return point1; }
    public Point getPoint2() { return point2; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Oval oval = (Oval) o;
        return id == oval.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        return "Oval{" +
                "id=" + id +
                ", point1=" + point1 +
                ", point2=" + point2 +
                '}';
    }
}