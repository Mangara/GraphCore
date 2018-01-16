package graphs;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import graphs.graph.Graph;
import graphs.graph.GraphVertex;

/**
 *
 * @author Sander Verdonschot <sander.verdonschot at gmail.com>
 */
public class RandomTriangulation extends Graph {

    public RandomTriangulation(int n) {
        this(n, new Random(), false);
    }

    public RandomTriangulation(int n, Random rand) {
        this(n, rand, false);
    }

    public RandomTriangulation(int n, Random rand, boolean triangularOuterface) {
        if (triangularOuterface) {
            addVertex(new GraphVertex(-100, -25));
            addVertex(new GraphVertex(50, 200));
            addVertex(new GraphVertex(200, -25));

            for (int i = 0; i < n - 3; i++) {
                addVertex(new GraphVertex(100 * rand.nextDouble(), 100 * rand.nextDouble()));
            }
        } else {
            for (int i = 0; i < n; i++) {
                addVertex(new GraphVertex(100 * rand.nextDouble(), 100 * rand.nextDouble()));
            }
        }

        // Build all vertex pairs
        List<Pair<Integer, Integer>> pairs = new ArrayList<Pair<Integer, Integer>>(n * n / 2);

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                pairs.add(new Pair<Integer, Integer>(i, j));
            }
        }

        Collections.shuffle(pairs, rand);

        // Convert them to edges
        List<Line2D> edges = new ArrayList<Line2D>();

        for (Pair<Integer, Integer> pair : pairs) {
            // Compute the line
            edges.add(new Line2D.Double(getVertices().get(pair.getFirst()).getX(),
                    getVertices().get(pair.getFirst()).getY(),
                    getVertices().get(pair.getSecond()).getX(),
                    getVertices().get(pair.getSecond()).getY()));
        }

        // See which edges we can use without intersecting
        boolean[] used = new boolean[pairs.size()];
        Arrays.fill(used, false);

        for (int i = 0; i < pairs.size(); i++) {
            int v1 = pairs.get(i).getFirst();
            int v2 = pairs.get(i).getSecond();
            Line2D edge = edges.get(i);

            boolean intersects = false;

            // Check if this edge intersects any edges that we added previously (sharing endpoints is ok)
            for (int j = 0; j < i && !intersects; j++) {
                if (used[j] && !(v1 == pairs.get(j).getFirst() || v1 == pairs.get(j).getSecond() || v2 == pairs.get(j).getFirst() || v2 == pairs.get(j).getSecond())) {
                    if (edge.intersectsLine(edges.get(j))) {
                        intersects = true;
                    }
                }
            }

            if (!intersects) {
                used[i] = true;
                addEdge(getVertices().get(v1), getVertices().get(v2));
            }
        }
    }
}
