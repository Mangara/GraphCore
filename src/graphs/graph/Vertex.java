package graphs.graph;

import java.util.Comparator;

/**
 *
 * @author Sander Verdonschot <sander.verdonschot at gmail.com>
 */
public class Vertex {

    protected double x, y;

    public Vertex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public static final Comparator<Vertex> increasingX = (Vertex v1, Vertex v2) -> {
        int compX = java.lang.Double.compare(v1.getX(), v2.getX());

        if (compX != 0) {
            return compX;
        } else {
            return java.lang.Double.compare(v1.getY(), v2.getY());
        }
    };

    public static final Comparator<Vertex> increasingY = (Vertex v1, Vertex v2) -> {
        int compY = java.lang.Double.compare(v1.getY(), v2.getY());

        if (compY != 0) {
            return compY;
        } else {
            return java.lang.Double.compare(v1.getX(), v2.getX());
        }
    };

    @Override
    public String toString() {
        return "V[" + x + ", " + y + "]";
    }

}
