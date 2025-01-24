import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * See
 * http://arborjs.org/docs/barnes-hut
 * https://github.com/DrA1ex/JS_ParticleSystem
 * https://physics.stackexchange.com/questions/317239/initializing-positions-of-n-body-simulations/317230#317230
 */
public class NBodySimulation extends Canvas {

    public static final int WIDTH_OF_SPACE = 1000;
    private static final double G = 6.6743e-11;
    private static final int MAX_MASS = 100000;
    private static final double DELTA_TIME = 0.5;

    private final List<Body> bodies = new ArrayList<>();
    private Quadrant root;
    private int bodyCount = 100;
    private float theta = 0.1F;

    public NBodySimulation(int bodyCount) {
        this.bodyCount = bodyCount;
        generateBodies();
        // initDynamicData();
    }

    public void calculate() {
        int i=0;
        do {
            root = null;
            constructTree();
            root.removeUnusedSubquadrants();
            root.calculateVirtualBodies();
            calculatePosition();
            //System.out.println(i++);
            if (i == 100) {
                repaint();
                i=0;
            }
            i++;
        } while (true);
    }

    /**
     * Generate bodies
     */
    private void generateBodies() {
        for (int i=0; i<bodyCount; i++) {
            Coordinate centerOfMass = new Coordinate(Math.random() * WIDTH_OF_SPACE, Math.random() * WIDTH_OF_SPACE);
            Coordinate velocity = new Coordinate((Math.random()-0.5)/100, (Math.random()-0.5)/100);
            int mass = (int) (Math.random() * MAX_MASS);
            bodies.add(new Body(centerOfMass, new Coordinate(0, 0), mass));
        }
    }

    /*
    private void generateBodies() {
        for (int x=10; x<WIDTH_OF_SPACE; x+=40) {
            for (int y=10; y<WIDTH_OF_SPACE; y+=40) {
                Coordinate velocity = new Coordinate(0, 0);
                int mass = (int) (Math.random() * MAX_MASS);
                bodies.add(new Body(new Coordinate(x, y), velocity, mass));
            }
        }
    }*/

    /**
     * Construct tree with bodies
     */
    private void constructTree() {
        for (Body b : bodies) {
            if (root == null) {
                root = new Quadrant(new Coordinate(0, 0), WIDTH_OF_SPACE);
            }
            root.addBody(b);
        }
    }

    private void calculatePosition() {
        for (Body b : bodies) {
            Coordinate acc = root.calculateForce(theta, b);
            acc.x *= -G;
            acc.y *= -G;
            Coordinate vel = b.velocity;

            // calculate new center of mass: r(n+1) = r(n) + v(n)*delta_t
            b.center.x += vel.x * DELTA_TIME;
            b.center.y += vel.y * DELTA_TIME;

            // calculate new velocity: v(n+1) = v(n) + a(n)*delta_t
            vel.add(acc.mul(DELTA_TIME));
        }
    }

    public void paint(Graphics g) {
        for (Body b : bodies) {
            int size = (int) b.mass / 4000;
            //g.fillOval((int)b.center.x, (int)b.center.y, size, size);
            g.drawOval((int)b.center.x, (int)b.center.y, size, size);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("N-Body Simulation");
        NBodySimulation canvas = new NBodySimulation(20);
        canvas.setSize(WIDTH_OF_SPACE, WIDTH_OF_SPACE);
        frame.add(canvas);
        frame.pack();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setVisible(true);
        canvas.calculate();
    }
}