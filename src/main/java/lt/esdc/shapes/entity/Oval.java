package lt.esdc.shapes.entity;


public class Oval extends AbstractShape {
    private Point pointX;
    private Point pointY;

    public Oval(long id, Point pointX, Point pointY) {
        super(id);
        this.pointX = pointX;
        this.pointY = pointY;
    }

    public Point getPointX() {
        return pointX;
    }

    public void setPointX(Point pointX) {
        boolean changed = false;
        if (this.pointX == null) {
            if (pointX != null) {
                changed = true;
            }
        } else {
            if (!this.pointX.equals(pointX)) {
                changed = true;
            }
        }

        if (changed) {
            this.pointX = pointX;
            notifyObservers();
        }
    }

    public Point getPointY() {
        return pointY;
    }

    public void setPointY(Point pointY) {
        boolean changed = false;
        if (this.pointY == null) {
            if (pointY != null) {
                changed = true;
            }
        } else {
            if (!this.pointY.equals(pointY)) {
                changed = true;
            }
        }

        if (changed) {
            this.pointY = pointY;
            notifyObservers();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Oval oval = (Oval) o;

        if (this.id != oval.id) {
            return false;
        }
        if (this.pointX == null) {
            if (oval.pointX != null) {
                return false;
            }
        } else {
            if (!this.pointX.equals(oval.pointX)) {
                return false;
            }
        }

        if (this.pointY == null) {
            return oval.pointY == null;
        } else {
            return this.pointY.equals(oval.pointY);
        }
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(id);
        result = 31 * result + (pointX != null ? pointX.hashCode() : 0);
        result = 31 * result + (pointY != null ? pointY.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Oval {id = ").append(id);
        sb.append(", point1 = ").append(pointX); // Renamed for clarity if they are corners
        sb.append(", point2 = ").append(pointY);
        sb.append('}');
        return sb.toString();
    }
}