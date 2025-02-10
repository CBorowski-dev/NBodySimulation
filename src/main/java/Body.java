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
        return mass == body.mass && Objects.equals(center, body.center) && Objects.equals(velocity, body.velocity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(center, velocity, mass);
    }
}
