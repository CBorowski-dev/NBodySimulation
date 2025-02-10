public class Vector {

    public double x, y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vector c) {
        x += c.x;
        y += c.y;
    }

    public Vector mul(double timeDelta) {
        return new Vector(x * timeDelta, y * timeDelta);
    }

}
