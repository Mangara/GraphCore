package graphs.graph;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sander Verdonschot <sander.verdonschot at gmail.com>
 */
public class Graph {

    protected List<GraphVertex> vertices;
    protected List<Edge> edges;

    public Graph() {
        vertices = new ArrayList<GraphVertex>();
        edges = new ArrayList<Edge>();
    }

    /**
     * Creates a new graph that is a copy of the given graph.
     *
     * @param graph
     */
    public Graph(Graph graph) {
        vertices = new ArrayList<GraphVertex>(graph.getVertices());
        edges = new ArrayList<Edge>(graph.getEdges());
    }

    public List<GraphVertex> getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addVertex(GraphVertex v) {
        vertices.add(v);
    }

    /**
     * Returns true if there is an edge between vA and vB, irrespective of
     * direction, false otherwise.
     *
     * @param vA
     * @param vB
     * @return
     */
    public boolean containsEdge(GraphVertex vA, GraphVertex vB) {
        return getEdgeBetween(vA, vB) != null;
    }

    /**
     * Returns the edge between vA and vB if one exists, irrespective of
     * direction, or null otherwise.
     *
     * @param vA
     * @param vB
     * @return
     */
    public Edge getEdgeBetween(GraphVertex vA, GraphVertex vB) {
        if (vA.getDegree() < vB.getDegree()) {
            return vA.getEdgeTo(vB);
        } else {
            return vB.getEdgeTo(vA);
        }
    }

    /**
     * Adds and returns the undirected edge between vA and vB. If vA and vB are
     * the same vertex, null is returned.
     *
     * @param vA
     * @param vB
     * @return
     */
    public Edge addEdge(GraphVertex vA, GraphVertex vB) {
        return addEdge(vA, vB, false);
    }

    /**
     * Adds and returns the edge between vA and vB. If directed is true, this
     * edge is directed from vA to vB. If vA and vB are the same vertex, no edge
     * is added and null is returned.
     *
     * @param vA
     * @param vB
     * @param directed
     * @return
     */
    public Edge addEdge(GraphVertex vA, GraphVertex vB, boolean directed) {
        if (vA != vB) {
            Edge e = new Edge(vA, vB, directed);
            vA.addEdge(e);
            vB.addEdge(e);
            edges.add(e);
            return e;
        } else {
            return null;
        }
    }

    public GraphVertex getVertexAt(double x, double y, double precision) {
        for (GraphVertex v : vertices) {
            if (v.isNear(x, y, precision)) {
                return v;
            }
        }
        return null;
    }

    public Edge getEdgeAt(double x, double y, double precision) {
        for (Edge e : edges) {
            if (e.isNear(x, y, precision)) {
                return e;
            }
        }
        return null;
    }

    public void removeVertex(GraphVertex v) {
        for (Edge e : v.getEdges()) {
            if (e.getVA() != v) {
                e.getVA().removeEdge(e);
            }

            if (e.getVB() != v) {
                e.getVB().removeEdge(e);
            }

            edges.remove(e);
        }

        vertices.remove(v);
    }

    public void removeEdge(Edge e) {
        edges.remove(e);
        e.getVA().removeEdge(e);
        e.getVB().removeEdge(e);
    }

    /**
     * Removes all edges from the graph
     */
    public void clearEdges() {
        for (Edge e : edges) {
            e.getVA().removeEdge(e);
            e.getVB().removeEdge(e);
        }

        edges.clear();
    }

    /**
     * Removes all edges and vertices from the graph
     */
    public void clear() {
        edges.clear();
        vertices.clear();
    }
}
