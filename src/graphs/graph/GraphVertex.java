package graphs.graph;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sander Verdonschot <sander.verdonschot at gmail.com>
 */
public class GraphVertex extends Vertex {

    private ArrayList<Edge> edges;
    private boolean visible;

    public GraphVertex(double x, double y) {
        this(x, y, true);
    }

    public GraphVertex(double x, double y, boolean visible) {
        super(x, y);
        this.edges = new ArrayList<Edge>();
        this.visible = visible;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addEdge(Edge e) {
        edges.add(e);
    }

    public void removeEdge(Edge e) {
        edges.remove(e);
    }

    /**
     * Returns true if there is an edge between this vertex and the given
     * vertex, irrespective of direction, false otherwise.
     *
     * @param v
     * @return
     */
    public boolean isAdjacentTo(GraphVertex v) {
        return getEdgeTo(v) != null;
    }

    /**
     * Returns the edge to the given vertex if it is a neighbour of this vertex,
     * or null otherwise.
     *
     * @param v
     * @return
     */
    public Edge getEdgeTo(GraphVertex v) {
        for (Edge e : edges) {
            GraphVertex neighbour = (e.getVA() == this ? e.getVB() : e.getVA());

            if (neighbour == v) {
                return e;
            }
        }

        return null;
    }

    /**
     * Returns a list of all vertices that have an edge to this vertex,
     * irrespective of direction.
     *
     * @return
     */
    public List<GraphVertex> getNeighbours() {
        List<GraphVertex> neighbours = new ArrayList<GraphVertex>(edges.size());

        for (Edge e : edges) {
            neighbours.add(e.getVA() == this ? e.getVB() : e.getVA());
        }

        return neighbours;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getDegree() {
        return edges.size();
    }

    public boolean isNear(double x, double y, double precision) {
        double dX = x - this.x;
        double dY = y - this.y;
        return (dX * dX + dY * dY <= precision * precision);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GraphVertex other = (GraphVertex) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }

    /*
     * Removed, because when storing information related to a vertex in a hash map, the information would be lost when the vertex was moved.
     @Override
     public int hashCode() {
     int hash = 3;
     hash = 41 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
     hash = 41 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
     return hash;
     }*/
    @Override
    public String toString() {
        return String.format("V[%.3f, %.3f]", x, y);
    }
}
