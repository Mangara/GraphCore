package graphs.graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class GraphReader {

    public static Graph readGraph(Path file) throws IOException {
        Graph graph = new Graph();

        try (BufferedReader in = Files.newBufferedReader(file, Charset.forName("UTF-8"))) {
            // Read all vertices
            ArrayList<GraphVertex> vertices = readVertices(in);

            // Add them all to our graph
            for (GraphVertex v : vertices) {
                graph.addVertex(v);
            }

            // Read and add all edges
            readEdges(in, graph, vertices);
        }

        return graph;
    }

    private static ArrayList<GraphVertex> readVertices(BufferedReader in) throws IOException {
        // Skip until the first occurrence of "Vertices"
        String line = in.readLine();

        while (line != null && !"Vertices".equals(line)) {
            line = in.readLine();
        }

        if (line == null) {
            throw new IOException("Incorrect file format: No line \"Vertices\" found.");
        }

        // Read the number of vertices
        line = in.readLine();
        int nVertices = Integer.parseInt(line);

        // Create our own temporary list of all vertices
        ArrayList<GraphVertex> vertices = new ArrayList<GraphVertex>(nVertices);

        // Read all vertices
        int i = 0;

        while (i < nVertices) {
            line = in.readLine().trim();

            // Skip blank lines
            if (line.length() > 0) {
                GraphVertex v = parseVertex(line);

                if (v != null) {
                    vertices.add(v);
                    i++;
                }
            }
        }

        return vertices;
    }

    private static void readEdges(BufferedReader in, Graph graph, ArrayList<GraphVertex> vertices) throws IOException {
        // Skip until the first occurrence of "Edges"
        String line = in.readLine();

        while (line != null && !"Edges".equals(line)) {
            line = in.readLine();
        }

        if (line == null) {
            throw new IOException("Incorrect file format: No line \"Edges\" found.");
        }

        // Read the number of edges
        line = in.readLine();
        int nEdges = Integer.parseInt(line);

        // Read all edges
        int i = 0;

        while (i < nEdges) {
            line = in.readLine();
            
            if (line == null) {
                throw new IOException("Incorrect file format: Fewer edges than specified.");
            }
            
            line = line.trim();

            // Skip blank lines
            if (line.length() > 0) {
                // Add the edge to the graph
                String[] parts = line.split(" ");

                int v1 = Integer.parseInt(parts[0]);
                int v2 = Integer.parseInt(parts[1]);

                graph.addEdge(vertices.get(v1), vertices.get(v2));
                i++;
            }
        }
    }

    private static GraphVertex parseVertex(String s) {
        String[] parts = s.split(" ");

        double x = java.lang.Double.parseDouble(parts[0]);
        double y = java.lang.Double.parseDouble(parts[1]);

        return new GraphVertex(x, y);
    }
}
