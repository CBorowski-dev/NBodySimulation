import java.util.Objects;

public class Body {
    Coordinate centerOfMass;
    int mass;

    public Body(Coordinate centerOfMass, int mass) {
        this.centerOfMass = centerOfMass;
        this.mass = mass;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Body body = (Body) o;
        return Float.compare(mass, body.mass) == 0 && Objects.equals(centerOfMass, body.centerOfMass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(centerOfMass, mass);
    }
}
