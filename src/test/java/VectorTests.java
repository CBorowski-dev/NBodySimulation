import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VectorTests {

    @Test
    public void testAdd() {
        Vector v1 = new Vector(2, 3);
        Vector v2 = new Vector(4, 5);

        v1.add(v2);

        assertEquals(6.0, v1.x, "X component should be 6");
        assertEquals(8.0, v1.y, "Y component should be 8");
    }

    @Test
    public void testMul() {
        Vector v = new Vector(3, 4);
        double timeDelta = 2;

        Vector result = v.mul(timeDelta);

        assertEquals(6.0, result.x, "X component should be 6 after multiplication");
        assertEquals(8.0, result.y, "Y component should be 8 after multiplication");
    }
}
