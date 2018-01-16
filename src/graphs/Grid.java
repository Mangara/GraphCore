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
public class Grid extends Graph {

    private final int gridSize;
    private GraphVertex[][] grid;

    public Grid(int gridSize) {
        this.gridSize = gridSize;
        grid = new GraphVertex[gridSize][gridSize];

        // Create all vertices and edges
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = new GraphVertex(i, j);
                addVertex(grid[i][j]);

                // connect left
                if (i > 0) {
                    addEdge(grid[i][j], grid[i - 1][j]);
                }

                // connect down
                if (j > 0) {
                    addEdge(grid[i][j], grid[i][j - 1]);
                }
            }
        }
    }

    public GraphVertex getVertex(int i, int j) {
        return grid[i][j];
    }

    public int getGridSize() {
        return gridSize;
    }
}
