package graphs.algos;

import java.util.LinkedList;
import java.util.Queue;
import graphs.graph.Graph;
import graphs.linkedlistgraph.LinkedListGraph;
import graphs.linkedlistgraph.LinkedListVertex;

/**
 *
 * @author Sander Verdonschot <sander.verdonschot at gmail.com>
 */
public class Planar5Orienter {

    /**
     * Pre-condition: g is planar, all edges in g are undirected (maybe not necessary?)
     * Replaces each edge of g by a directed edge such that each vertex has outdegree at most 5.
     * @param g
     * @return
     */
    public static Graph orient(Graph g) {
        LinkedListGraph lg = new LinkedListGraph(g);
        orient(lg);
        return lg.toGraph();
    }

    /**
     * Pre-condition: g is planar, all edges in g are undirected (maybe not necessary?)
     * Replaces each edge of g by a directed edge such that each vertex has outdegree at most 5.
     * @param g
     * @return
     */
    public static void orient(LinkedListGraph g) {
        // Add all vertices with degree 5 or less to the queue
        Queue<LinkedListVertex> q = new LinkedList<LinkedListVertex>();

        for (LinkedListVertex v : g.getVertices()) {
            if (v.getDegree() <= 5) {
                q.add(v);
            }
        }

        while (!q.isEmpty()) {
            LinkedListVertex v = q.remove();

            for (LinkedListVertex vn : v.getNeighbours()) {
                // vn's degree will decrease by 1, so if it's 6 now, we should add it to our queue
                if (vn.getDegree() == 6) {
                    q.add(vn);
                }
            }

            // Remove the incoming edges of the processed vertex
            g.directEdgesOutward(v);
        }
    }
}
