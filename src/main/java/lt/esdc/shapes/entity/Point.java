package lt.esdc.shapes.entity;


public record Point(double x, double y) {


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(point.x)) {
            return false;
        }
        return Double.doubleToLongBits(y) == Double.doubleToLongBits(point.y);
    }

    @Override
    public int hashCode() {
        int result;
        result = Double.hashCode(x);
        result = 31 * result + Double.hashCode(y);
        return result;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Point {x = ").append(x)
            .append(", y = ").append(y)
            .append('}');
        return sb.toString();
    }
}