/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import graphs.graph.Graph;
import graphs.graph.GraphVertex;

/**
 *
 * @author Sander Verdonschot <sander.verdonschot at gmail.com>
 */
public class ConvexGraph extends Graph {

    public ConvexGraph(int n) {
        double angle = 2 * Math.PI / n;
        
        for (int i = 0; i < n; i++) {
            double x = 200 + 100 * Math.cos(i * angle);
            double y = 200 + 100 * Math.sin(i * angle);
            
            addVertex(new GraphVertex(x, y));
        }
        
        for (int i = 0; i < n - 1; i++) {
            addEdge(vertices.get(i), vertices.get(i + 1));
        }
        addEdge(vertices.get(n-1), vertices.get(0));
    }
    
    public GraphVertex getVertex(int i) {
        return vertices.get(i);
    }
    
}
