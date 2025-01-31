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
public class NBodySimulation extends JPanel {

    public static final int WIDTH_OF_SPACE = 1000;
    private static final double G = 6.6743e-11;
    private static final int MAX_MASS = 10_000_000;
    private static final double DELTA_TIME = 0.1;
    private static final float THETA = 0.8F;

    private final List<Body> bodies = new ArrayList<>();
    private Quadrant root;
    private int bodyCount = 100;

    private int coreCount;
    private int bodiesPerCore;

    private boolean[] threadSync;

    /**
     *
     * @param bodyCount
     */
    public NBodySimulation(int bodyCount) {
        this.bodyCount = bodyCount;
        generateBodies();
        coreCount = Runtime.getRuntime().availableProcessors();
        bodiesPerCore = bodyCount / coreCount;
        threadSync = new boolean[coreCount];
    }

    /**
     *
     */
    public void calculate() {
        int i=0;
        do {
            root = new Quadrant(new Coordinate(0, 0), WIDTH_OF_SPACE);
            constructTree();
            root.removeUnusedSubquadrants();
            root.calculateVirtualBodies();
            // calculatePosition();
            for (int x=0; x<coreCount; x++) {
                int x_final = x;
                threadSync[x_final] = false;
                Thread.startVirtualThread(() -> {
                    calculatePosition(x_final * bodiesPerCore, bodiesPerCore);
                    threadSync[x_final] = true;
                });
            }
            boolean wait;
            do {
                wait = false;
                for (int j=0; j<threadSync.length; j++) {
                    if (!threadSync[j]) {
                        wait = true;
                        break;
                    }
                }
            } while (wait);
            if (i == 5) {
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
            Coordinate velocity = new Coordinate((Math.random()-0.5)/60, (Math.random()-0.5)/60);
            int mass = (int) (Math.random() * MAX_MASS);
            bodies.add(new Body(centerOfMass, velocity, mass));
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
        for (Body b : bodies) root.addBody(b);
    }

    /**
     *
     */
    private void calculatePosition(int start, int length) {
        //for (Body b : bodies) {
        for (int t=start; t<start+length; t++) {
            Body b = bodies.get(t);
            Coordinate acc = root.calculateForce(THETA, b);
            acc.x *= -G;
            acc.y *= -G;
            Coordinate vel = b.velocity;

            // calculate new center of mass: r(n+1) = r(n) + v(n)*delta_t
            b.center.x += vel.x * DELTA_TIME;
            if (b.center.x<0) b.center.x = WIDTH_OF_SPACE + b.center.x;
            if (b.center.x>WIDTH_OF_SPACE) b.center.x = b.center.x - WIDTH_OF_SPACE;
            b.center.y += vel.y * DELTA_TIME;
            if (b.center.y<0) b.center.y = WIDTH_OF_SPACE + b.center.y;
            if (b.center.y>WIDTH_OF_SPACE) b.center.y = b.center.y - WIDTH_OF_SPACE;

            // calculate new velocity: v(n+1) = v(n) + a(n)*delta_t
            vel.add(acc.mul(DELTA_TIME));
        }
    }

    /**
     *
     * @param g
     */
    public void paintComponent(Graphics g) {
        g.clearRect(0,0, WIDTH_OF_SPACE, WIDTH_OF_SPACE);
        for (Body b : bodies) {
            int size = (int) b.mass / 800_000;
            g.fillOval((int)b.center.x, (int)b.center.y, size, size);
            // g.drawOval((int)b.center.x, (int)b.center.y, size, size);
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("N-Body Simulation");
        NBodySimulation panel = new NBodySimulation(256);
        frame.add(panel);
        frame.setSize(WIDTH_OF_SPACE, WIDTH_OF_SPACE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setVisible(true);
        panel.calculate();
    }
}