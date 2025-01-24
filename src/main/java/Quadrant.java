import java.util.ArrayList;
import java.util.List;

public class Quadrant {

    Coordinate coordinate; // Coordinate of quadrant with dimensions [x+width, y+width]
    int width;
    boolean isSingleBodyQuadrant = true;
    Body body; // real or virtual body
    List<Quadrant> subQuadrants = new ArrayList<>();

    public Quadrant(Coordinate coordinate, int widthOfQuadrant) {
        this.coordinate = coordinate;
        this.width = widthOfQuadrant;
    }

    public void addBody(Body b) {
        double b_x = b.center.x;
        double b_y = b.center.y;
        if (!(coordinate.x <= b_x && coordinate.x+width > b_x &&
                coordinate.y <= b_y && coordinate.y+width > b_y)) {
            // b is not in this quadrant
            return;
        }

        // b is in this quadrant
        if (body == null) {
            // first body --> just add
            this.body = b;
            isSingleBodyQuadrant = true;
            return;
        }

        if (isSingleBodyQuadrant) {
            // no subquadrants so far
            isSingleBodyQuadrant = false;
            int widthHalf = width / 2;
            subQuadrants.add(new Quadrant(coordinate, widthHalf));
            subQuadrants.add(new Quadrant(new Coordinate(coordinate.x+widthHalf, coordinate.y), widthHalf));
            subQuadrants.add(new Quadrant(new Coordinate(coordinate.x, coordinate.y+widthHalf), widthHalf));
            subQuadrants.add(new Quadrant(new Coordinate(coordinate.x+widthHalf, coordinate.y+widthHalf), widthHalf));
            for (Quadrant q : subQuadrants) {
                q.addBody(body);
                q.addBody(b);
            }
        } else {
            // subquadrants available
            for (Quadrant q : subQuadrants) {
                q.addBody(b);
            }
        }
    }

    public void removeUnusedSubquadrants() {
        for (int i=0; i<subQuadrants.size();) {
            Quadrant q = subQuadrants.get(i);
            if (q.isEmpty()) {
                subQuadrants.remove(i);
            } else {
                q.removeUnusedSubquadrants();
                i++;
            }
        }
    }

    private boolean isEmpty() {
        return body == null;
    }

    public Body calculateVirtualBodies() {
        // m = m1 + m2
        // x = (x1*m1 + x2*m2) / m
        // y = (y1*m1 + y2*m2) / m

        if (!isSingleBodyQuadrant) {
            int m = 0;
            float x = 0;
            float y = 0;
            for (Quadrant q : subQuadrants) {
                Body b = q.calculateVirtualBodies();
                m += b.mass;
                x += b.center.x * b.mass;
                y += b.center.y * b.mass;
            }
            x /= m;
            y /= m;
            body = new Body(new Coordinate(x, y), new Coordinate(0,0), m);
        }
        return body;
    }

    public Coordinate calculateForce(float theta, Body b) {
        Coordinate vec = b.center.diff(body.center);
        double vecLength = Math.sqrt(vec.x*vec.x + vec.y*vec.y);
        if ((isSingleBodyQuadrant && !b.equals(body))
            || (((double) width) / vecLength) < theta) {
            // real body or b is far away
            vec.x /= vecLength;
            vec.y /= vecLength;
            double vecLength2 = vecLength * vecLength;
            vec.x = vec.x / vecLength2 * body.mass;
            vec.y = vec.y / vecLength2 * body.mass;
        } else {
            vec = new Coordinate(0, 0);
            for (Quadrant q : subQuadrants) {
                vec.add(q.calculateForce(theta, b));
            }
        }
        return vec;
    }
}
