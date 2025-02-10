import java.util.Objects;

public class Body {
    Coordinate center; // center of mass
    Vector velocity;
    int mass;

    public Body(Coordinate c, Vector v, int mass) {
        this.center = c;
        this.velocity = v;
        this.mass = mass;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Body body = (Body) o;
        return Float.compare(mass, body.mass) == 0 && Objects.equals(center, body.center);
    }

    @Override
    public int hashCode() {
        return Objects.hash(center, mass);
    }
}
