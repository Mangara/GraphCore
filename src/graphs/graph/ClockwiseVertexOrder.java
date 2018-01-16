package graphs.graph;

import java.util.Comparator;

/**
 *
 * @author Sander Verdonschot <sander.verdonschot at gmail.com>
 */
public class ClockwiseVertexOrder implements Comparator<GraphVertex> {

    private GraphVertex center;

    public ClockwiseVertexOrder(GraphVertex center) {
        this.center = center;
    }

    public int compare(GraphVertex v1, GraphVertex v2) {
        return Double.compare(getAngle(v1), getAngle(v2));
    }

    private double getAngle(GraphVertex v) {
        double vx = v.getX() - center.getX();
        double vy = v.getY() - center.getY();

        double angle = Math.acos(vy / Math.sqrt(vx * vx + vy * vy));

        if (vx > 0) {
            return angle;
        } else {
            return -angle;
        }
    }
}
