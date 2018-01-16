package graphs.graph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GraphWriter {

    public static void writeGraph(File file, Graph graph) throws IOException {
        BufferedWriter out = null;

        try {
            out = new BufferedWriter(new FileWriter(file));

            List<GraphVertex> vertices = graph.getVertices();
            List<Edge> edges = graph.getEdges();

            out.write("Vertices");
            out.newLine();

            out.write(Integer.toString(vertices.size()));
            out.newLine();

            for (GraphVertex vertex : vertices) {
                out.write(vertex.getX() + " " + vertex.getY());
                out.newLine();
            }

            out.newLine();
            out.write("Edges");
            out.newLine();

            out.write(edges.size());
            out.newLine();

            for (Edge edge : edges) {
                // print the indices of the endpoints of this edge
                out.write(Integer.toString(vertices.indexOf(edge.getVA())));
                out.write(" ");
                out.write(Integer.toString(vertices.indexOf(edge.getVB())));
                out.newLine();
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
