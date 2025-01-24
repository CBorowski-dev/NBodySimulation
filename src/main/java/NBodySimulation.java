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
 */
public class NBodySimulation extends Canvas {

    public static final int WIDTH_OF_SPACE = 1000;

    private static final double G = 6.6743e-11;
    private static final int MAX_MASS = 100000;

    private final List<Body> bodies = new ArrayList<>();
    private List<Coordinate> forces = new ArrayList<>();;
    private Quadrant root;
    private int bodyCount = 100;
    private float theta = 0.2F;

    public NBodySimulation(int bodyCount) {
        this.bodyCount = bodyCount;
        generateBodies();
        generateForces();
    }

    public void calculate() {
        int i=0;
        do {
            root = null;
            constructTree();
            root.removeUnusedSubquadrants();
            root.calculateVirtualBodies();
            calculateForces();
            adjustBodyCoordinates();
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
        // int m = 0;
        for (int i=0; i<bodyCount; i++) {
            double x = Math.random() * WIDTH_OF_SPACE;
            double y = Math.random() * WIDTH_OF_SPACE;
            int mass = (int) (Math.random() * MAX_MASS);
            // m += mass;
            bodies.add(new Body(new Coordinate(x, y), mass));
        }
        // System.out.println("Gesamtmasse : " + m);
    }
/**
    private void generateBodies() {
        // int m = 0;
        for (int x=10; x<WIDTH_OF_SPACE; x+=40) {
            for (int y=10; y<WIDTH_OF_SPACE; y+=40) {
                //int mass = (int) (Math.random() * MAX_MASS);
                bodies.add(new Body(new Coordinate(x, y), (x==490 && y==490)?MAX_MASS:MAX_MASS));
            }
        }
        // System.out.println("Gesamtmasse : " + m);
    }*/

    private void generateForces() {
        for (int i=0; i<bodies.size(); i++) {
            forces.add(new Coordinate(0.0, 0.0));
        }
    }

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

    private void calculateForces() {
        for (int i=0; i<bodies.size(); i++) {
            Coordinate f = root.calculateForce(theta, bodies.get(i));
            f.x *= -G;
            f.y *= -G;
            Coordinate fOld = forces.get(i);
            fOld.x /= 2;
            fOld.y /= 2;
            fOld.add(f);
        }
    }

    private void adjustBodyCoordinates() {
        for (int i=0; i<bodies.size(); i++) {
            bodies.get(i).centerOfMass.add(forces.get(i));
        }
    }

    public void paint(Graphics g) {
        for (Body b : bodies) {
            int size = (int) b.mass / 4000;
            g.fillOval((int)b.centerOfMass.x, (int)b.centerOfMass.y, size, size);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("N-Body Simulation");
        NBodySimulation canvas = new NBodySimulation(200);
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