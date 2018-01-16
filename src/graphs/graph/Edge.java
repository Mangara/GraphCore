package graphs.graph;

import java.awt.geom.Line2D;

/**
 *
 * @author Sander Verdonschot <sander.verdonschot at gmail.com>
 */
public class Edge {

    private GraphVertex vA, vB;
    private boolean directed;
    private boolean visible;

    public Edge(GraphVertex vA, GraphVertex vB) {
        this(vA, vB, false, true);
    }

    public Edge(GraphVertex vA, GraphVertex vB, boolean directed) {
        this(vA, vB, directed, true);
    }

    public Edge(GraphVertex vA, GraphVertex vB, boolean directed, boolean visible) {
        this.vA = vA;
        this.vB = vB;
        this.directed = directed;
        this.visible = visible;
    }

    public GraphVertex getVA() {
        return vA;
    }

    public void setVA(GraphVertex vA) {
        this.vA = vA;
    }

    public GraphVertex getVB() {
        return vB;
    }

    public void setVB(GraphVertex vB) {
        this.vB = vB;
    }

    public boolean isDirected() {
        return directed;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Returns the length of this edge.
     */
    public double getLength() {
        double dx = vB.getX() - vA.getX();
        double dy = vB.getY() - vA.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public boolean isNear(double x, double y, double precision) {
        double vecX = vB.getX() - vA.getX();
        double vecY = vB.getY() - vA.getY();
        double len = Math.sqrt(vecX * vecX + vecY * vecY);
        double unitX = vecX / len;
        double unitY = vecY / len;
        double offsetX = x - vA.getX();
        double offsetY = y - vA.getY();
        double dot_offset_unit = offsetX * unitX + offsetY * unitY;

        if (0 <= dot_offset_unit && dot_offset_unit <= len) {
            double rotatedUnitX = -unitY;
            double rotatedUnitY = unitX;
            double dot_offset_rotatedUnit = offsetX * rotatedUnitX + offsetY * rotatedUnitY;

            return Math.abs(dot_offset_rotatedUnit) <= precision;
        } else {
            return false;
        }
    }

    /**
     * Returns true if this edge intersects the given edge at any point, false
     * otherwise.
     *
     * @param edge
     * @return
     */
    public boolean intersects(Edge edge) {
        return Line2D.linesIntersect(vA.getX(), vA.getY(), vB.getX(), vB.getY(), edge.getVA().getX(), edge.getVA().getY(), edge.getVB().getX(), edge.getVB().getY());
    }

    /**
     * Returns true if this edge intersects the given edge, but does not share
     * any endpoint.
     *
     * @param edge
     * @return
     */
    public boolean intersectsProperly(Edge edge) {
        if (vA == edge.getVA() || vA == edge.getVB() || vB == edge.getVA() || vB == edge.getVB()) {
            return false;
        } else {
            return Line2D.linesIntersect(vA.getX(), vA.getY(), vB.getX(), vB.getY(), edge.getVA().getX(), edge.getVA().getY(), edge.getVB().getX(), edge.getVB().getY());
        }
    }

    public boolean isIncidentTo(GraphVertex v) {
        return v == vA || v == vB;
    }

    @Override
    public boolean equals(final Object obj) {
        // edges might be undirected:
        // edge a-b == edge b-a
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Edge other = (Edge) obj;
        if (this.vA == other.vA && this.vB == other.vB && this.directed == other.directed) {
            return true;
        }
        if (!directed && !other.directed && this.vA == other.vB && this.vB == other.vA) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        // edges might be undirected:
        // edge a-b == edge b-a
        // we take a lexicographical ordering on vertices to keep this function consistent with equals
        if (directed || this.vA.getX() < this.vB.getX() || (this.vA.getX() == this.vB.getX() && this.vA.getY() < this.vB.getY())) {
            hash = 23 * hash + (this.vA != null ? this.vA.hashCode() : 0);
            hash = 23 * hash + (this.vB != null ? this.vB.hashCode() : 0);
        } else {
            hash = 23 * hash + (this.vB != null ? this.vB.hashCode() : 0);
            hash = 23 * hash + (this.vA != null ? this.vA.hashCode() : 0);
        }
        return hash;
    }

    @Override
    public String toString() {
        return "E[" + vA + ", " + vB + "]";
    }
}
