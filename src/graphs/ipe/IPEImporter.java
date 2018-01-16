package graphs.ipe;

import graphs.graph.Edge;
import graphs.graph.Graph;
import graphs.graph.GraphVertex;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * @author Wouter Meulemans
 * @author Sander Verdonschot <sander.verdonschot at gmail.com>
 */
public class IPEImporter {

    private String currentLayer = null; // The layer we are currently working in

    public Graph importGraph(File file) throws IOException {
        BufferedReader in = null;
        Graph graph = new Graph();

        try {
            in = new BufferedReader(new FileReader(file));

            String line = in.readLine();

            while (line != null) {
                // Skip the ipestyle
                if (line.contains("<ipestyle")) {
                    while (!line.contains("</ipestyle>")) {
                        line = in.readLine();
                    }
                }

                if (line.contains("layer=")) {
                    setLayer(line);
                }

                if (line.contains("<path")) {
                    // This is a path consisting of one or more edges
                    importEdges(graph, in, getMatrix(line));
                } else if (line.contains("<mark") || (line.contains("<use") && line.contains("name=\"mark"))) {
                    // This is a vertex
                    if (line.contains("pos=")) {
                        // Isolate the substring that contains the position
                        int startIndex = line.indexOf("pos=") + "pos=\"".length();
                        String pos = line.substring(startIndex, line.indexOf('"', startIndex));

                        importVertex(graph, pos, getMatrix(line));
                    } else {
                        // No pos information - origin is default
                        importVertex(graph, "0 0", getMatrix(line));
                    }
                }

                line = in.readLine();
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return graph;
    }

    private void importEdges(Graph graph, BufferedReader in, double[] transform) throws IOException {
        String line = in.readLine();
        GraphVertex prev = null;
        GraphVertex first = null;

        while (!line.contains("</path>")) {
            GraphVertex v;

            if (line.equals("h")) {
                // Return to the start
                v = first;
            } else {
                v = importVertex(graph, line, transform);
            }

            if (prev != null) {
                Edge e = new Edge(prev, v);

                if (prev != v && !graph.getEdges().contains(e)) {
                    graph.addEdge(prev, v);
                }
            } else {
                first = v;
            }

            prev = v;

            line = in.readLine();
        }
    }

    private GraphVertex importVertex(Graph graph, String line, double[] transform) {
        String[] coords = line.split(" ");

        assert coords.length == 2;

        double x = Double.parseDouble(coords[0]);
        double y = Double.parseDouble(coords[1]);

        // Apply the transformation
        double xt = transform[0] * x + transform[2] * y + transform[4];
        double yt = transform[1] * x + transform[3] * y + transform[5];

        GraphVertex v = graph.getVertexAt(xt, yt, 0.01); // Low accuracy due to rounding errors in transformation =(

        if (v == null) {
            v = new GraphVertex(xt, yt);
            graph.addVertex(v);
        }

        return v;
    }

    /**
     * Parses and returns the transformation matrix of this element.
     *
     * The matrix consists of 6 double values: {a, b, c, d, e, f}
     *
     * It has to be read as
     *
     * | a  c  e |
     * | b  d  f |
     * | 0  0  1 |
     *
     * Every point (x, y) is treated as a column vector
     *
     * | x |
     * | y |
     * | 1 |
     *
     * The resulting point is the matrix multiplication of the two:
     *
     * | a  c  e |   | x |   |ax + cy + e|
     * | b  d  f | x | y | = |bx + dy + f|
     * | 0  0  1 |   | 1 |   |     1     |
     *
     * @param line
     * @return
     */
    private double[] getMatrix(String line) {
        double[] transform = new double[]{1, 0, 0, 1, 0, 0}; // Identity matrix

        if (line.contains("matrix=")) {
            // Get the substring that contains the matrix
            int startIndex = line.indexOf("matrix") + "matrix=\"".length();
            String matrix = line.substring(startIndex, line.indexOf('"', startIndex));

            // Split to obtain the 6 double values
            String[] parts = matrix.split(" ");

            assert parts.length == 6;

            for (int i = 0; i < parts.length; i++) {
                transform[i] = Double.parseDouble(parts[i]);
            }
        }

        return transform;
    }

    private void setLayer(String line) {
        // Get the substring that contains the layer
        int startIndex = line.indexOf("layer") + "layer=\"".length();
        currentLayer = line.substring(startIndex, line.indexOf('"', startIndex));
    }
}
