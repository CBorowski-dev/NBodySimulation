public class Coordinate {

    public double x, y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate diff(Coordinate c) {
        return new Coordinate(x - c.x, y - c.y);
    }

    public void add(Coordinate c) {
        x -= c.x;
        y -= c.y;
    }
}
