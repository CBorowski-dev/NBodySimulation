import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoordinateTests {

    @Test
    public void testDiff() {
        Coordinate c1 = new Coordinate(5, 7);
        Coordinate c2 = new Coordinate(3, 4);

        Vector diff = c1.diff(c2);

        assertEquals(2.0, diff.x, "X component of difference should be 2");
        assertEquals(3.0, diff.y, "Y component of difference should be 3");
    }

    @Test
    public void testInheritance() {
        Coordinate coord = new Coordinate(1, 2);

        // Test if a Coordinate can use Vector's methods
        coord.add(new Vector(3, 4));

        assertEquals(4.0, coord.x, "X component should be 4 after addition");
        assertEquals(6.0, coord.y, "Y component should be 6 after addition");

        Vector result = coord.mul(2);

        assertEquals(8.0, result.x, "X component should be 8 after multiplication");
        assertEquals(12.0, result.y, "Y component should be 12 after multiplication");
    }
}
