package It.esdc.shapes.entity;

import java.util.Observable;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class Oval extends Observable {
    private final long id;
    private Point pointX;
    private Point pointY;

    public Oval(Point p1, Point p2) {
        this.id = UUID.randomUUID().getLeastSignificantBits();
        this.pointX = p1;
        this.pointY = p2;
    }

    public long getId() {
        return id;
    }

    public Point getPointX() {
        return pointX;
    }

    public Point getPointY() {
        return pointY;
    }

    public void setPointX(Point pointX) {
        this.pointX = pointX;
        this.setChanged();
        this.notifyObservers();
    }

    public void setPointY(Point pointY) {
        this.pointY = pointY;
        this.setChanged();
        this.notifyObservers();
    }


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
                ", point1=" + pointX +
                ", point2=" + pointY +
                '}';
    }

    @Override
    public void addObserver(java.util.Observer o) {
        super.addObserver(o);
    }

    @Override
    public void deleteObserver(java.util.Observer o) {
        super.deleteObserver(o);
    }
}