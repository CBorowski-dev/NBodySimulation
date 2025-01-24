public class Coordinate {

    public double x, y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(Coordinate c) {
        x += c.x;
        y += c.y;
    }

    public Coordinate diff(Coordinate c) {
        return new Coordinate(x - c.x, y - c.y);
    }

    public Coordinate mul(double timeDelta) {
        return new Coordinate(x * timeDelta, y * timeDelta);
    }
}
