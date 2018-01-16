package graphs.embedded;

import graphs.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import graphs.graph.ClockwiseOrder;
import graphs.graph.Edge;
import graphs.graph.Graph;
import graphs.graph.GraphVertex;
import java.util.List;

public class EmbeddedGraph {

    private Set<EmbeddedVertex> vertices;
    private Set<Face> faces;
    private Set<HalfEdge> darts;
    // Fields related to the original graph this EmbeddedGraph was constructed from
    private Graph originalGraph;
    private HashMap<GraphVertex, EmbeddedVertex> vertexMap;
    private HashMap<Edge, HalfEdge> edgeMap;

    protected EmbeddedGraph() {
        vertices = new HashSet<EmbeddedVertex>();
        faces = new HashSet<Face>();
        darts = new HashSet<HalfEdge>();

        originalGraph = null;
        vertexMap = new HashMap<GraphVertex, EmbeddedVertex>();
        edgeMap = new HashMap<Edge, HalfEdge>();
    }

    public EmbeddedGraph(Graph graph) {
        initializeFromGraph(graph);
    }

    protected void initializeFromGraph(Graph graph) {
        vertices = new HashSet<EmbeddedVertex>();
        faces = new HashSet<Face>();
        darts = new HashSet<HalfEdge>();

        originalGraph = graph;
        vertexMap = new HashMap<GraphVertex, EmbeddedVertex>();
        edgeMap = new HashMap<Edge, HalfEdge>();

        // make vertices
        for (GraphVertex vertex : graph.getVertices()) {
            EmbeddedVertex vtx = new EmbeddedVertex(vertex.getX(), vertex.getY());
            vertices.add(vtx);
            vertexMap.put(vertex, vtx);
        }

        // make darts
        for (Edge edge : graph.getEdges()) {
            EmbeddedVertex emvA = vertexMap.get(edge.getVA());
            EmbeddedVertex emvB = vertexMap.get(edge.getVB());

            HalfEdge d1 = new HalfEdge();
            d1.setOrigin(emvA);
            emvA.setDart(d1);
            darts.add(d1);

            HalfEdge d2 = new HalfEdge();
            d2.setOrigin(emvB);
            emvB.setDart(d2);
            darts.add(d2);

            d2.setTwin(d1);
            d1.setTwin(d2);

            edgeMap.put(edge, d1);
        }

        // For each vertex, find the cyclic order of outgoing darts
        // The next vertex in the cyclic order is the next of your twin
        // So we can compute the next of all incoming darts and the previous of all outgoing darts this way
        for (GraphVertex vertex : graph.getVertices()) {
            // Sort the edges of this vertex in cyclic (clockwise) order
            ArrayList<Edge> edges = new ArrayList<Edge>(vertex.getEdges());
            Collections.sort(edges, new ClockwiseOrder(vertex));

            ArrayList<HalfEdge> outgoingDarts = new ArrayList<HalfEdge>(edges.size());

            for (Edge edge : edges) {
                // Find the outgoing dart that corresponds to this edge
                HalfEdge outgoing = edgeMap.get(edge);

                if (outgoing.getOrigin() != vertexMap.get(vertex)) {
                    outgoing = outgoing.getTwin();
                }

                outgoingDarts.add(outgoing);
            }

            for (int i = 0; i < outgoingDarts.size(); i++) {
                // The next of the twin of this dart is the next dart in clockwise order around the vertex
                HalfEdge dart = outgoingDarts.get(i);
                HalfEdge nextDart = outgoingDarts.get((i + 1) % outgoingDarts.size());

                dart.getTwin().setNext(nextDart);
                nextDart.setPrevious(dart.getTwin());
            }
        }

        // make and set faces
        for (HalfEdge dart : darts) {
            if (dart.getFace() == null) {
                Face face = new Face();

                face.setDart(dart);
                dart.setFace(face);

                HalfEdge walkDart = dart.getNext();
                while (walkDart != dart) {
                    walkDart.setFace(face);
                    walkDart = walkDart.getNext();
                }

                faces.add(face);
            }
        }

        if (!faces.isEmpty()) {
            // Find the outer face.
            // First find the leftmost vertex
            GraphVertex leftMost = Collections.min(graph.getVertices(), GraphVertex.increasingX);

            // The face of the outgoing dart corresponding to the first visible edge is the outer face
            Edge first = Collections.min(leftMost.getEdges(), new ClockwiseOrder(leftMost));
            HalfEdge outgoing = edgeMap.get(first);

            if (outgoing.getOrigin() != vertexMap.get(leftMost)) {
                outgoing = outgoing.getTwin();
            }

            outgoing.getFace().setOuterFace(true);
        }
    }

    public Set<EmbeddedVertex> getVertices() {
        return vertices;
    }

    public void setDarts(final Set<HalfEdge> darts) {
        this.darts = darts;
    }

    public void setFaces(final Set<Face> faces) {
        this.faces = faces;
    }

    public void setVertices(final Set<EmbeddedVertex> vertices) {
        this.vertices = vertices;
    }

    public void addVertex(final EmbeddedVertex v) {
        vertices.add(v);
    }

    public Set<Face> getFaces() {
        return faces;
    }

    public void addFace(final Face f) {
        faces.add(f);
    }

    public Set<HalfEdge> getDarts() {
        return darts;
    }

    public void addDart(final HalfEdge d) {
        darts.add(d);
    }

    public HashMap<Edge, HalfEdge> getEdgeMap() {
        return edgeMap;
    }

    public Graph getOriginalGraph() {
        return originalGraph;
    }

    public HashMap<GraphVertex, EmbeddedVertex> getVertexMap() {
        return vertexMap;
    }

    public Graph toGraph() {
        Graph result = new Graph();

        HashMap<EmbeddedVertex, GraphVertex> vMap = new HashMap<EmbeddedVertex, GraphVertex>(vertices.size() * 2); // Double the required size to keep hash access fast

        for (EmbeddedVertex v : vertices) {
            GraphVertex vertex = new GraphVertex(v.getX(), v.getY());

            vMap.put(v, vertex);
            result.addVertex(vertex);
        }

        for (HalfEdge dart : darts) {
            GraphVertex vA = vMap.get(dart.getOrigin());
            GraphVertex vB = vMap.get(dart.getDestination());

            if (!result.containsEdge(vA, vB)) {
                result.addEdge(vA, vB);
            }
        }

        return result;
    }

    public Pair<Graph, HashMap<Face, GraphVertex>> getDualGraph() {
        Graph dual = new Graph();

        // Add a vertex for each face and remember the mapping
        HashMap<Face, GraphVertex> faceMap = new HashMap<Face, GraphVertex>(2 * faces.size()); // Double the capacity to make the hashing more efficient

        for (Face face : faces) {
            GraphVertex v = centerOfMass(face);
            faceMap.put(face, v);
            dual.addVertex(v);
        }

        // Add an edge for each adjacency between faces
        for (HalfEdge e : darts) {
            GraphVertex v1 = faceMap.get(e.getFace());
            GraphVertex v2 = faceMap.get(e.getTwin().getFace());

            dual.addEdge(v1, v2);
        }

        return new Pair<Graph, HashMap<Face, GraphVertex>>(dual, faceMap);
    }

    private GraphVertex centerOfMass(Face face) {
        List<EmbeddedVertex> faceVertices = face.getVertices();

        if (faceVertices != null && faceVertices.size() > 0) {
            double area = 0;
            double cx = 0;
            double cy = 0;

            for (int i = 0; i < faceVertices.size(); i++) {
                EmbeddedVertex v = faceVertices.get(i);
                EmbeddedVertex next = faceVertices.get((i + 1) % faceVertices.size());

                double areaIncrement = v.getX() * next.getY() - next.getX() * v.getY();

                area += areaIncrement;
                cx += (v.getX() + next.getX()) * areaIncrement;
                cy += (v.getY() + next.getY()) * areaIncrement;
            }

            area /= 2;
            cx /= 6 * area;
            cy /= 6 * area;

            return new GraphVertex(cx, cy);
        } else {
            return new GraphVertex(0, 0);
        }
    }

    public boolean verifyDCEL() {
        //System.out.println("Starting verification");
        // for all darts, the twin of the twin must be the dart itself
        // and the previous of the next must be itself
        for (HalfEdge e : darts) {
            HalfEdge t = e.getTwin();
            if (t == null) {
                System.out.println("----> No twin");
                return false;
            } else if (t.getTwin() != e) {
                System.out.println("----> Twins twin is not edge");
                return false;
            } else if (t == e) {
                System.out.println("----> edge is its own twin");
                return false;
            }

            HalfEdge n = e.getNext();
            if (n == null) {
                System.out.println("----> No next");
                return false;
            } else if (n.getPrevious() != e) {
                System.out.println("----> Nexts previous is not edge");
                return false;
            } else if (n == e) {
                System.out.println("----> edge is its own next");
                return false;
            }

            HalfEdge p = e.getPrevious();
            if (p == null) {
                System.out.println("----> No previous");
                return false;
            } else if (p.getNext() != e) {
                System.out.println("----> Previous' next is not edge");
                return false;
            } else if (p == e) {
                System.out.println("----> edge is its own previous");
                return false;
            }

            if (!e.getOrigin().getEdges().contains(e)) {
                System.out.println("----> origins edges do not contain edge");
                return false;
            }
        }

        // for each vertex, the source of its edge must be the vertex itself
        // as for all other edges around
        for (EmbeddedVertex v : vertices) {
            HalfEdge e = v.getDart();

            if (e == null) {
                System.out.println("----> No edge for vertex");
                return false;
            } else {
                HalfEdge d = e;

                do {
                    if (d.getOrigin() == null) {
                        System.out.println("----> No origin");
                        return false;
                    } else if (d.getOrigin() != v) {
                        System.out.println("----> Incorrect origin");
                        return false;
                    }

                    d = d.getTwin().getNext();
                } while (e != d);
            }
        }

        // for each face, the face of its edge must be the face itself
        // as for all other edge around the face
        for (Face f : faces) {
            HalfEdge e = f.getDart();
            if (e == null) {
                System.out.println("----> No edge for face");
                return false;
            } else {
                HalfEdge d = e;
                do {
                    if (d.getFace() != f) {
                        System.out.println("----> Incorrect face");
                        return false;
                    }
                    d = d.getNext();
                } while (e != d);
            }
        }

        //System.out.println("Verification succesful");
        return true;
    }
}
