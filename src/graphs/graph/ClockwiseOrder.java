package graphs.graph;

import java.util.Comparator;

/**
 *
 * @author Sander Verdonschot <sander.verdonschot at gmail.com>
 */
public class ClockwiseOrder implements Comparator<Edge> {

    private GraphVertex center;

    public ClockwiseOrder(GraphVertex center) {
        this.center = center;
    }

    public int compare(Edge e1, Edge e2) {
        // compare the angles of e1 and e2
        return Double.compare(getAngle(e1), getAngle(e2));
    }

    private double getAngle(Edge e) {
        GraphVertex dest = (e.getVA() == center ? e.getVB() : e.getVA());

        double vx = dest.getX() - center.getX();
        double vy = dest.getY() - center.getY();

        double angle = Math.acos(vy / Math.sqrt(vx * vx + vy * vy));

        if (vx > 0) {
            return angle;
        } else {
            return -angle;
        }
    }
}
