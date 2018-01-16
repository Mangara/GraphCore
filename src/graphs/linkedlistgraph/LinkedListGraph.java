package graphs.linkedlistgraph;

import graphs.Pair;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import graphs.graph.Edge;
import graphs.graph.Graph;
import graphs.graph.GraphVertex;
import java.util.Map;

/**
 * An adjacency-list representation of a graph. If an edge is undirected, its entries in the adjacency lists are linked to allow fast deletion.
 * @author Sander Verdonschot <sander.verdonschot at gmail.com>
 */
public class LinkedListGraph {

    private HashSet<LinkedListVertex> vertices;

    public LinkedListGraph() {
        vertices = new HashSet<LinkedListVertex>();
    }

    public LinkedListGraph(Graph g) {
        vertices = new HashSet<LinkedListVertex>(2 * g.getVertices().size());
        HashMap<GraphVertex, LinkedListVertex> vMap = new HashMap<GraphVertex, LinkedListVertex>(2 * g.getVertices().size());

        for (GraphVertex v : g.getVertices()) {
            LinkedListVertex lv = new LinkedListVertex(v.getX(), v.getY());
            vMap.put(v, lv);
            vertices.add(lv);
        }

        for (Edge e : g.getEdges()) {
            LinkedListVertex a = vMap.get(e.getVA());
            LinkedListVertex b = vMap.get(e.getVB());

            LinkedListVertexEntry ea = a.addNeighbour(b);
            LinkedListVertexEntry eb = b.addNeighbour(a);

            ea.twin = eb;
            eb.twin = ea;
        }
    }

    /**
     * Adds the given vertex to the graph, with no adjacent edges.
     * Runs in O(1) expected time.
     * @param v
     */
    public void addVertex(LinkedListVertex v) {
        vertices.add(v);
    }

    /**
     * Adds an undirected edge between the given pair of vertices. The edge will be present in both adjacency lists.
     * Runs in O(1) time.
     * @param a
     * @param b
     */
    public void addEdge(LinkedListVertex a, LinkedListVertex b) {
        addEdge(a, b, false);
    }

    /**
     * Adds an edge between the given pair of vertices.
     * If the edge is directed, it will be present only in the adjacency list of a, otherwise it will be present in both adjacency lists.
     * Runs in O(1) time.
     * @param a
     * @param b
     */
    public void addEdge(LinkedListVertex a, LinkedListVertex b, boolean directed) {
        if (directed) {
            a.addNeighbour(b);
        } else {
            LinkedListVertexEntry ea = a.addNeighbour(b);
            LinkedListVertexEntry eb = b.addNeighbour(a);

            ea.twin = eb;
            eb.twin = ea;
        }
    }

    /**
     * Returns true if there is an edge between a and b in any direction, false otherwise.
     * Runs in O(degree of a + degree of b) time.
     * @param a
     * @param b
     * @return
     */
    public boolean containsEdge(LinkedListVertex a, LinkedListVertex b) {
        boolean neighbours = a.isAdjacentTo(b);

        if (!neighbours) {
            neighbours = b.isAdjacentTo(a);
        }

        return neighbours;
    }

    /**
     * Returns a collection of all vertices in this graph. Runs in O(1) time.
     * @return
     */
    public Collection<LinkedListVertex> getVertices() {
        return vertices;
    }

    /**
     * Removes the given vertex and all its edges from the graph. Directed incoming edges will not be removed
     * Runs in O(|adjacency list of v|) time.
     * @param v
     */
    public void removeVertex(LinkedListVertex v) {
        // Remove all edges to v
        directEdgesOutward(v);
        vertices.remove(v);
    }

    /**
     * Makes all undirected edges of this vertex outgoing by removing the corresponding entries from the adjacency lists of its neighbours.
     * Runs in O(|adjacency list of v|) time.
     * @param v
     */
    public void directEdgesOutward(LinkedListVertex v) {
        LinkedListVertexEntry e = v.head;

        while (e != null) {
            if (e.twin != null) {
                e.twin.myVertex.degree--;
                e.twin.remove();
            } else {
                System.err.println("Twin was null");
            }

            e = e.next;
        }
    }

    /**
     * Converts this LinkedListGraph into a Graph.
     * Runs in O(n + m) expected time, where n and m are the number of vertices and edges in this LinkedListGraph.
     * @return
     */
    public Graph toGraph() {
        Graph g = new Graph();
        HashMap<LinkedListVertex, GraphVertex> vMap = new HashMap<LinkedListVertex, GraphVertex>(2 * vertices.size());

        for (LinkedListVertex v : vertices) {
            GraphVertex gv = new GraphVertex(v.getX(), v.getY());
            vMap.put(v, gv);
            g.addVertex(gv);
        }

        HashSet<LinkedListVertex> processed = new HashSet<LinkedListVertex>(2 * vertices.size());

        for (LinkedListVertex v : vertices) {
            GraphVertex gv = vMap.get(v);
            LinkedListVertexEntry e = v.head;

            while (e != null) {
                GraphVertex nv = vMap.get(e.neighbour);

                if (e.twin == null) {
                    g.addEdge(gv, nv, true);
                } else {
                    if (!processed.contains(e.neighbour)) {
                        g.addEdge(gv, nv, false);
                    }
                }

                e = e.next;
            }

            processed.add(v);
        }

        return g;
    }

    /**
     * Converts the given Graph into a LinkedListGraph and returns the constructed LinkedListGraph and a mapping of vertices of the original Graph to the LinkedListGraph.\
     * Runs in O(n + m) expected time, where n and m are the number of vertices and edges in the given Graph.
     * @param g
     * @return
     */
    public static Pair<LinkedListGraph, Map<GraphVertex, LinkedListVertex>> fromGraph(Graph g) {
        LinkedListGraph rg = new LinkedListGraph();

        HashMap<GraphVertex, LinkedListVertex> vMap = new HashMap<GraphVertex, LinkedListVertex>(2 * g.getVertices().size());

        for (GraphVertex v : g.getVertices()) {
            LinkedListVertex rv = new LinkedListVertex(v.getX(), v.getY());
            vMap.put(v, rv);
            rg.addVertex(rv);
        }

        for (Edge e : g.getEdges()) {
            LinkedListVertex a = vMap.get(e.getVA());
            LinkedListVertex b = vMap.get(e.getVB());
            rg.addEdge(a, b);
        }

        return new Pair<LinkedListGraph, Map<GraphVertex, LinkedListVertex>>(rg, vMap);
    }
}
