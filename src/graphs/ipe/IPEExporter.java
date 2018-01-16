package graphs.ipe;

import graphs.embedded.EmbeddedGraph;
import graphs.embedded.EmbeddedVertex;
import graphs.embedded.HalfEdge;
import graphs.graph.Edge;
import graphs.graph.Graph;
import graphs.graph.GraphVertex;
import graphs.graph.Vertex;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Sander Verdonschot <sander.verdonschot at gmail.com>
 */
public class IPEExporter {

    private static final String IPE7HEADER
            = // XML stuff
            "<?xml version=\"1.0\"?>\n"
            + "<!DOCTYPE ipe SYSTEM \"ipe.dtd\">\n"
            + // We require IPE version 7
            "<ipe version=\"70010\" creator=\"RectangularCartogram\">\n"
            + // Basic IPE style
            "<ipestyle name=\"basic\">\n"
            + "<symbol name=\"arrow/arc(spx)\">\n"
            + "<path stroke=\"sym-stroke\" fill=\"sym-stroke\" pen=\"sym-pen\">\n"
            + "0 0 m\n"
            + "-1 0.333 l\n"
            + "-1 -0.333 l\n"
            + "h\n"
            + "</path>\n"
            + "</symbol>\n"
            + "<symbol name=\"arrow/farc(spx)\">\n"
            + "<path stroke=\"sym-stroke\" fill=\"white\" pen=\"sym-pen\">\n"
            + "0 0 m\n"
            + "-1 0.333 l\n"
            + "-1 -0.333 l\n"
            + "h\n"
            + "</path>\n"
            + "</symbol>\n"
            + "<symbol name=\"mark/circle(sx)\" transformations=\"translations\">\n"
            + "<path fill=\"sym-stroke\">\n"
            + "0.6 0 0 0.6 0 0 e\n"
            + "0.4 0 0 0.4 0 0 e\n"
            + "</path>\n"
            + "</symbol>\n"
            + "<symbol name=\"mark/disk(sx)\" transformations=\"translations\">\n"
            + "<path fill=\"sym-stroke\">\n"
            + "0.6 0 0 0.6 0 0 e\n"
            + "</path>\n"
            + "</symbol>\n"
            + "<symbol name=\"mark/fdisk(sfx)\" transformations=\"translations\">\n"
            + "<group>\n"
            + "<path fill=\"sym-stroke\" fillrule=\"eofill\">\n"
            + "0.6 0 0 0.6 0 0 e\n"
            + "0.4 0 0 0.4 0 0 e\n"
            + "</path>\n"
            + "<path fill=\"sym-fill\">\n"
            + "0.4 0 0 0.4 0 0 e\n"
            + "</path>\n"
            + "</group>\n"
            + "</symbol>\n"
            + "<symbol name=\"mark/box(sx)\" transformations=\"translations\">\n"
            + "<path fill=\"sym-stroke\" fillrule=\"eofill\">\n"
            + "-0.6 -0.6 m\n"
            + "0.6 -0.6 l\n"
            + "0.6 0.6 l\n"
            + "-0.6 0.6 l\n"
            + "h\n"
            + "-0.4 -0.4 m\n"
            + "0.4 -0.4 l\n"
            + "0.4 0.4 l\n"
            + "-0.4 0.4 l\n"
            + "h\n"
            + "</path>\n"
            + "</symbol>\n"
            + "<symbol name=\"mark/square(sx)\" transformations=\"translations\">\n"
            + "<path fill=\"sym-stroke\">\n"
            + "-0.6 -0.6 m\n"
            + "0.6 -0.6 l\n"
            + "0.6 0.6 l\n"
            + "-0.6 0.6 l\n"
            + "h\n"
            + "</path>\n"
            + "</symbol>\n"
            + "<symbol name=\"mark/fsquare(sfx)\" transformations=\"translations\">\n"
            + "<group>\n"
            + "<path fill=\"sym-stroke\" fillrule=\"eofill\">\n"
            + "-0.6 -0.6 m\n"
            + "0.6 -0.6 l\n"
            + "0.6 0.6 l\n"
            + "-0.6 0.6 l\n"
            + "h\n"
            + "-0.4 -0.4 m\n"
            + "0.4 -0.4 l\n"
            + "0.4 0.4 l\n"
            + "-0.4 0.4 l\n"
            + "h\n"
            + "</path>\n"
            + "<path fill=\"sym-fill\">\n"
            + "-0.4 -0.4 m\n"
            + "0.4 -0.4 l\n"
            + "0.4 0.4 l\n"
            + "-0.4 0.4 l\n"
            + "h\n"
            + "</path>\n"
            + "</group>\n"
            + "</symbol>\n"
            + "<symbol name=\"mark/cross(sx)\" transformations=\"translations\">\n"
            + "<group>\n"
            + "<path fill=\"sym-stroke\">\n"
            + "-0.43 -0.57 m\n"
            + "0.57 0.43 l\n"
            + "0.43 0.57 l\n"
            + "-0.57 -0.43 l\n"
            + "h\n"
            + "</path>\n"
            + "<path fill=\"sym-stroke\">\n"
            + "-0.43 0.57 m\n"
            + "0.57 -0.43 l\n"
            + "0.43 -0.57 l\n"
            + "-0.57 0.43 l\n"
            + "h\n"
            + "</path>\n"
            + "</group>\n"
            + "</symbol>\n"
            + "<symbol name=\"arrow/fnormal(spx)\">\n"
            + "<path stroke=\"sym-stroke\" fill=\"white\" pen=\"sym-pen\">\n"
            + "0 0 m\n"
            + "-1 0.333 l\n"
            + "-1 -0.333 l\n"
            + "h\n"
            + "</path>\n"
            + "</symbol>\n"
            + "<symbol name=\"arrow/pointed(spx)\">\n"
            + "<path stroke=\"sym-stroke\" fill=\"sym-stroke\" pen=\"sym-pen\">\n"
            + "0 0 m\n"
            + "-1 0.333 l\n"
            + "-0.8 0 l\n"
            + "-1 -0.333 l\n"
            + "h\n"
            + "</path>\n"
            + "</symbol>\n"
            + "<symbol name=\"arrow/fpointed(spx)\">\n"
            + "<path stroke=\"sym-stroke\" fill=\"white\" pen=\"sym-pen\">\n"
            + "0 0 m\n"
            + "-1 0.333 l\n"
            + "-0.8 0 l\n"
            + "-1 -0.333 l\n"
            + "h\n"
            + "</path>\n"
            + "</symbol>\n"
            + "<symbol name=\"arrow/linear(spx)\">\n"
            + "<path stroke=\"sym-stroke\" pen=\"sym-pen\">\n"
            + "-1 0.333 m\n"
            + "0 0 l\n"
            + "-1 -0.333 l\n"
            + "</path>\n"
            + "</symbol>\n"
            + "<symbol name=\"arrow/fdouble(spx)\">\n"
            + "<path stroke=\"sym-stroke\" fill=\"white\" pen=\"sym-pen\">\n"
            + "0 0 m\n"
            + "-1 0.333 l\n"
            + "-1 -0.333 l\n"
            + "h\n"
            + "-1 0 m\n"
            + "-2 0.333 l\n"
            + "-2 -0.333 l\n"
            + "h\n"
            + "</path>\n"
            + "</symbol>\n"
            + "<symbol name=\"arrow/double(spx)\">\n"
            + "<path stroke=\"sym-stroke\" fill=\"sym-stroke\" pen=\"sym-pen\">\n"
            + "0 0 m\n"
            + "-1 0.333 l\n"
            + "-1 -0.333 l\n"
            + "h\n"
            + "-1 0 m\n"
            + "-2 0.333 l\n"
            + "-2 -0.333 l\n"
            + "h\n"
            + "</path>\n"
            + "</symbol>\n"
            + "<pen name=\"heavier\" value=\"0.8\"/>\n"
            + "<pen name=\"fat\" value=\"1.2\"/>\n"
            + "<pen name=\"ultrafat\" value=\"2\"/>\n"
            + "<symbolsize name=\"large\" value=\"5\"/>\n"
            + "<symbolsize name=\"small\" value=\"2\"/>\n"
            + "<symbolsize name=\"tiny\" value=\"1.1\"/>\n"
            + "<arrowsize name=\"large\" value=\"10\"/>\n"
            + "<arrowsize name=\"small\" value=\"5\"/>\n"
            + "<arrowsize name=\"tiny\" value=\"3\"/>\n"
            + "<color name=\"red\" value=\"1 0 0\"/>\n"
            + "<color name=\"green\" value=\"0 1 0\"/>\n"
            + "<color name=\"blue\" value=\"0 0 1\"/>\n"
            + "<color name=\"yellow\" value=\"1 1 0\"/>\n"
            + "<color name=\"orange\" value=\"1 0.647 0\"/>\n"
            + "<color name=\"gold\" value=\"1 0.843 0\"/>\n"
            + "<color name=\"purple\" value=\"0.627 0.125 0.941\"/>\n"
            + "<color name=\"gray\" value=\"0.745\"/>\n"
            + "<color name=\"brown\" value=\"0.647 0.165 0.165\"/>\n"
            + "<color name=\"navy\" value=\"0 0 0.502\"/>\n"
            + "<color name=\"pink\" value=\"1 0.753 0.796\"/>\n"
            + "<color name=\"seagreen\" value=\"0.18 0.545 0.341\"/>\n"
            + "<color name=\"turquoise\" value=\"0.251 0.878 0.816\"/>\n"
            + "<color name=\"violet\" value=\"0.933 0.51 0.933\"/>\n"
            + "<color name=\"darkblue\" value=\"0 0 0.545\"/>\n"
            + "<color name=\"darkcyan\" value=\"0 0.545 0.545\"/>\n"
            + "<color name=\"darkgray\" value=\"0.663\"/>\n"
            + "<color name=\"darkgreen\" value=\"0 0.392 0\"/>\n"
            + "<color name=\"darkmagenta\" value=\"0.545 0 0.545\"/>\n"
            + "<color name=\"darkorange\" value=\"1 0.549 0\"/>\n"
            + "<color name=\"darkred\" value=\"0.545 0 0\"/>\n"
            + "<color name=\"lightblue\" value=\"0.678 0.847 0.902\"/>\n"
            + "<color name=\"lightcyan\" value=\"0.878 1 1\"/>\n"
            + "<color name=\"lightgray\" value=\"0.827\"/>\n"
            + "<color name=\"lightgreen\" value=\"0.565 0.933 0.565\"/>\n"
            + "<color name=\"lightyellow\" value=\"1 1 0.878\"/>\n"
            + "<dashstyle name=\"dashed\" value=\"[4] 0\"/>\n"
            + "<dashstyle name=\"dotted\" value=\"[1 3] 0\"/>\n"
            + "<dashstyle name=\"dash dotted\" value=\"[4 2 1 2] 0\"/>\n"
            + "<dashstyle name=\"dash dot dotted\" value=\"[4 2 1 2 1 2] 0\"/>\n"
            + "<textsize name=\"large\" value=\"\\large\"/>\n"
            + "<textsize name=\"small\" value=\"\\small\"/>\n"
            + "<textsize name=\"tiny\" value=\"\\tiny\"/>\n"
            + "<textsize name=\"Large\" value=\"\\Large\"/>\n"
            + "<textsize name=\"LARGE\" value=\"\\LARGE\"/>\n"
            + "<textsize name=\"huge\" value=\"\\huge\"/>\n"
            + "<textsize name=\"Huge\" value=\"\\Huge\"/>\n"
            + "<textsize name=\"footnote\" value=\"\\footnotesize\"/>\n"
            + "<textstyle name=\"center\" begin=\"\\begin{center}\" end=\"\\end{center}\"/>\n"
            + "<textstyle name=\"itemize\" begin=\"\\begin{itemize}\" end=\"\\end{itemize}\"/>\n"
            + "<textstyle name=\"item\" begin=\"\\begin{itemize}\\item{}\" end=\"\\end{itemize}\"/>\n"
            + "<gridsize name=\"4 pts\" value=\"4\"/>\n"
            + "<gridsize name=\"8 pts (~3 mm)\" value=\"8\"/>\n"
            + "<gridsize name=\"16 pts (~6 mm)\" value=\"16\"/>\n"
            + "<gridsize name=\"32 pts (~12 mm)\" value=\"32\"/>\n"
            + "<gridsize name=\"10 pts (~3.5 mm)\" value=\"10\"/>\n"
            + "<gridsize name=\"20 pts (~7 mm)\" value=\"20\"/>\n"
            + "<gridsize name=\"14 pts (~5 mm)\" value=\"14\"/>\n"
            + "<gridsize name=\"28 pts (~10 mm)\" value=\"28\"/>\n"
            + "<gridsize name=\"56 pts (~20 mm)\" value=\"56\"/>\n"
            + "<anglesize name=\"90 deg\" value=\"90\"/>\n"
            + "<anglesize name=\"60 deg\" value=\"60\"/>\n"
            + "<anglesize name=\"45 deg\" value=\"45\"/>\n"
            + "<anglesize name=\"30 deg\" value=\"30\"/>\n"
            + "<anglesize name=\"22.5 deg\" value=\"22.5\"/>\n"
            + "<tiling name=\"falling\" angle=\"-60\" step=\"4\" width=\"1\"/>\n"
            + "<tiling name=\"rising\" angle=\"30\" step=\"4\" width=\"1\"/>\n"
            + "</ipestyle>\n";
    private static final String IPE6HEADER
            = // We require IPE version 6
            "<ipe version=\"60032\" creator=\"RectangularCartogram\">\n"
            + "<info bbox=\"cropbox\"/>\n"
            + "<ipestyle>\n"
            + "</ipestyle>\n";
    private static final String LAYERS
            = // Beginning of the IPE page
            "<page>\n"
            // Layers
            + "<layer name=\"Vertices\"/>\n"
            + "<layer name=\"Edges\"/>\n"
            // The default view showing everything
            + "<view layers=\"Vertices Edges\" active=\"Vertices\"/>\n";
    private static final String PAGE_BREAK
            = "</page>\n";
    private static final String POST_TAGS
            = "</page>\n</ipe>";

    private static class Transformation {

        double factor = 1;
        double xTranslate = 0;
        double yTranslate = 0;

        double transformX(double x) {
            return x * factor + xTranslate;
        }

        double transformY(double y) {
            return y * factor + yTranslate;
        }
    }

    public static void exportGraph(File file, Graph graph) throws IOException {
        exportGraph(file, graph, false);
    }
    
    public static void exportGraph(File file, Graph graph, boolean useIPE6) throws IOException {
        exportGraphs(file, Collections.singleton(graph), useIPE6);
    }

    public static void exportGraphs(File file, Collection<? extends Graph> graphs, boolean useIPE6) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            if (useIPE6) {
                out.write(IPE6HEADER);
            } else {
                out.write(IPE7HEADER);
            }

            boolean first = true;

            for (Graph graph : graphs) {
                if (first) {
                    first = false;
                } else {
                    out.write(PAGE_BREAK);
                }

                out.write(LAYERS);

                Transformation t = computeScaleFactors(graph.getVertices());

                for (Edge e : graph.getEdges()) {
                    exportEdge(out, t, e.getVA().getX(), e.getVA().getY(), e.getVB().getX(), e.getVB().getY(), "Edges", Color.black, 1);
                }

                // Vertices last, so they appear on top
                for (GraphVertex v : graph.getVertices()) {
                    exportVertex(out, t, v.getX(), v.getY(), "Vertices", Color.blue, useIPE6);
                }
            }

            out.write(POST_TAGS);
        }
    }

    public static void exportEmbeddedGraph(File file, EmbeddedGraph graph, boolean useIPE6) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {

            if (useIPE6) {
                out.write(IPE6HEADER);
            } else {
                out.write(IPE7HEADER);
            }

            out.write(LAYERS);

            Transformation t = computeScaleFactors(graph.getVertices());

            // Keep track of which edges we already exported
            HashSet<HalfEdge> drawnHalfEdges = new HashSet<>(graph.getDarts().size());

            for (HalfEdge e : graph.getDarts()) {
                if (!drawnHalfEdges.contains(e)) {
                    exportEdge(out, t, e.getOrigin().getX(), e.getOrigin().getY(), e.getDestination().getX(), e.getDestination().getY(), "Edges", Color.black, 1);

                    drawnHalfEdges.add(e);
                    drawnHalfEdges.add(e.getTwin());
                }
            }

            // Vertices last, so they appear on top
            for (EmbeddedVertex v : graph.getVertices()) {
                exportVertex(out, t, v.getX(), v.getY(), "Vertices", Color.blue, useIPE6);
            }

            out.write(POST_TAGS);
        }
    }

    private static void exportVertex(BufferedWriter out, Transformation t, double x, double y, String layer, Color fillColour, boolean useIPE6) throws IOException {
        float[] c = fillColour.getRGBColorComponents(null);
        if (useIPE6) {
            out.write(String.format("<mark layer=\"%s\" type=\"1\" pos=\"%f %f\" size=\"large\" stroke=\"black\" fill=\"%f %f %f\"/>\n", layer, t.transformX(x), t.transformY(y), c[0], c[1], c[2]));
        } else {
            out.write(String.format("<use layer=\"%s\" name=\"mark/fdisk(sfx)\" pos=\"%f %f\" size=\"large\" stroke=\"black\" fill=\"%f %f %f\"/>\n", layer, t.transformX(x), t.transformY(y), c[0], c[1], c[2]));
        }
    }

    private static void exportEdge(BufferedWriter out, Transformation t, double x1, double y1, double x2, double y2, String layer, Color colour, double thickness) throws IOException {
        float[] c = colour.getRGBColorComponents(null);
        out.write(String.format("<path layer=\"%s\" pen=\"%f\" stroke=\"%f %f %f\">\n", layer, thickness, c[0], c[1], c[2]));
        out.write(t.transformX(x1) + " " + t.transformY(y1) + " m\n");
        out.write(t.transformX(x2) + " " + t.transformY(y2) + " l\n");
        out.write("</path>\n");
    }

    private static final double TARGET_MARGINS = 20;
    private static final double TARGET_WIDTH = 595; // Width of an IPE page
    private static final double TARGET_HEIGHT = 842; // Height of an IPE page

    private static Transformation computeScaleFactors(Collection<? extends Vertex> vertices) {
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (Vertex v : vertices) {
            minX = Math.min(v.getX(), minX);
            maxX = Math.max(v.getX(), maxX);
            minY = Math.min(v.getY(), minY);
            maxY = Math.max(v.getY(), maxY);
        }

        // Compute scale factor
        Transformation result = new Transformation();

        double xFactor = (maxX <= minX ? Double.POSITIVE_INFINITY : (TARGET_WIDTH - 2 * TARGET_MARGINS) / (maxX - minX));
        double yFactor = (maxY <= minY ? Double.POSITIVE_INFINITY : (TARGET_HEIGHT - 2 * TARGET_MARGINS) / (maxY - minY));

        if (xFactor < yFactor) {
            result.factor = xFactor;

            result.xTranslate = (maxX <= minX ? 0 : TARGET_MARGINS - result.factor * minX);
            result.yTranslate = (maxY <= minY ? 0 : (TARGET_HEIGHT - result.factor * (maxY - minY)) / 2);
        } else {
            result.factor = yFactor;

            result.xTranslate = (maxX <= minX ? 0 : (TARGET_WIDTH - result.factor * (maxX - minX)) / 2);
            result.yTranslate = (maxY <= minY ? 0 : TARGET_MARGINS - result.factor * minY);
        }
        
        //System.out.println("minX: " + minX);
        //System.out.println("F: " + result.factor + " xT: " + result.xTranslate + " yT: " + result.yTranslate);

        return result;
    }

    private final BufferedWriter outWriter;
    private final List<String> layers;
    private final boolean ipe6mode;
    private Transformation scaleFactors = new Transformation();
    private String currentLayer;
    private static final Color DEFAULT_FILL_COLOUR = Color.blue;
    private static final Color DEFAULT_EDGE_COLOUR = Color.black;
    private static final double DEFAULT_EDGE_THICKNESS = 1;

    public static IPEExporter startInteractiveMode(Path outputFile, List<String> layers) throws IOException {
        return startInteractiveMode(outputFile, layers, false);
    }

    public static IPEExporter startInteractiveMode(Path outputFile, List<String> layers, boolean useIpe6) throws IOException {
        if (layers == null || layers.isEmpty()) {
            throw new IllegalArgumentException("At least one layer required.");
        }

        return new IPEExporter(Files.newBufferedWriter(outputFile), layers, useIpe6);
    }

    public void stopInteractiveMode() throws IOException {
        outWriter.write(POST_TAGS);
        outWriter.close();
    }

    public void scaleDrawing(Collection<? extends Vertex> vertices) {
        scaleFactors = computeScaleFactors(vertices);
    }

    public void setLayer(String layer) {
        if (!layers.contains(layer)) {
            throw new IllegalArgumentException("Layer does not exist.");
        }

        currentLayer = layer;
    }

    public void newPage() throws IOException {
        outWriter.write(PAGE_BREAK);
        writeLayers();
    }

    public void drawVertex(GraphVertex v, String layer, Color fillColour) throws IOException {
        exportVertex(outWriter, scaleFactors, v.getX(), v.getY(), layer, fillColour, ipe6mode);
    }

    public void drawVertex(GraphVertex v, String layer) throws IOException {
        drawVertex(v, layer, DEFAULT_FILL_COLOUR);
    }

    public void drawVertex(GraphVertex v, Color fillColour) throws IOException {
        drawVertex(v, this.currentLayer, fillColour);
    }

    public void drawVertex(GraphVertex v) throws IOException {
        drawVertex(v, this.currentLayer, DEFAULT_FILL_COLOUR);
    }

    public void drawEdge(Edge e, String layer, Color colour, double thickness) throws IOException {
        exportEdge(outWriter, scaleFactors, e.getVA().getX(), e.getVA().getY(), e.getVB().getX(), e.getVB().getY(), layer, colour, thickness);
    }

    public void drawEdge(Edge e, String layer, Color colour) throws IOException {
        drawEdge(e, layer, colour, DEFAULT_EDGE_THICKNESS);
    }

    public void drawEdge(Edge e, String layer, double thickness) throws IOException {
        drawEdge(e, layer, DEFAULT_EDGE_COLOUR, thickness);
    }

    public void drawEdge(Edge e, String layer) throws IOException {
        drawEdge(e, layer, DEFAULT_EDGE_COLOUR, DEFAULT_EDGE_THICKNESS);
    }

    public void drawEdge(Edge e, Color colour, double thickness) throws IOException {
        drawEdge(e, currentLayer, colour, thickness);
    }

    public void drawEdge(Edge e, Color colour) throws IOException {
        drawEdge(e, currentLayer, colour, DEFAULT_EDGE_THICKNESS);
    }

    public void drawEdge(Edge e, double thickness) throws IOException {
        drawEdge(e, currentLayer, DEFAULT_EDGE_COLOUR, thickness);
    }

    public void drawEdge(Edge e) throws IOException {
        drawEdge(e, currentLayer, DEFAULT_EDGE_COLOUR, DEFAULT_EDGE_THICKNESS);
    }

    private IPEExporter(BufferedWriter outWriter, List<String> layers, boolean ipe6mode) throws IOException {
        this.outWriter = outWriter;
        this.layers = layers;
        currentLayer = layers.get(0);
        this.ipe6mode = ipe6mode;

        if (ipe6mode) {
            outWriter.write(IPE6HEADER);
        } else {
            outWriter.write(IPE7HEADER);
        }

        writeLayers();
    }

    private void writeLayers() throws IOException {
        // Beginning of the IPE page
        outWriter.write("<page>\n");

        // Each layer
        for (String layer : layers) {
            outWriter.write(String.format("<layer name=\"%s\"/>\n", layer));
        }

        // The default view showing all layers
        boolean first = true;
        outWriter.write("<view layers=\"");
        for (String layer : layers) {
            if (!first) {
                outWriter.write(" ");
            }
            first = false;

            outWriter.write(layer);
        }
        // The active layer
        outWriter.write(String.format("\" active=\"%s\"/>\n", layers.get(0)));
    }
}
