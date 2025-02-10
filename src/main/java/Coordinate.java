public class Coordinate extends Vector {

    public Coordinate(double x, double y) {
        super(x, y);
    }

    public Vector diff(Coordinate c) {
        return new Vector(x - c.x, y - c.y);
    }

}
