package graphs.embedded;

import graphs.graph.Vertex;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Sander Verdonschot <sander.verdonschot at gmail.com>
 */
public class EmbeddedVertex extends Vertex {

    private HalfEdge dart;

    public EmbeddedVertex(final double x, final double y) {
        super(x, y);
    }

    public HalfEdge getDart() {
        return dart;
    }

    public void setDart(final HalfEdge dart) {
        this.dart = dart;
    }

    /**
     * Returns the degree of this vertex.
     *
     * @return
     */
    public int getDegree() {
        if (dart == null) {
            return 0;
        }

        int degree = 0;
        HalfEdge walkDart = dart;

        do {
            degree++;
            walkDart = walkDart.getTwin().getNext();
        } while (walkDart != dart);

        return degree;
    }

    /**
     * Returns the outgoing darts of this vertex in clockwise order, starting
     * with the dart that is stored with this vertex.
     *
     * @return
     */
    public List<HalfEdge> getEdges() {
        return getEdges(dart);
    }

    /**
     * Returns the outgoing darts of this vertex in clockwise order, starting
     * with the given dart.
     *
     * @param from
     * @return
     */
    public List<HalfEdge> getEdges(HalfEdge from) {
        if (dart == null) {
            return Collections.<HalfEdge>emptyList();
        }

        List<HalfEdge> result = new ArrayList<>();
        HalfEdge walkDart = dart;

        do {
            result.add(walkDart);
            walkDart = walkDart.getTwin().getNext();
        } while (walkDart != dart);

        return result;
    }

    /**
     * Returns the faces around this vertex in clockwise order, starting with
     * the dart stored with this vertex.
     *
     * @return
     */
    public List<Face> getFaces() {
        if (dart == null) {
            return Collections.<Face>emptyList();
        }

        List<Face> result = new ArrayList<>();
        HalfEdge walkDart = dart;

        do {
            result.add(walkDart.getFace());
            walkDart = walkDart.getTwin().getNext();
        } while (walkDart != dart);

        return result;
    }

    /**
     * Returns the neighbours of this vertex in clockwise order, starting with
     * the destination of the dart that is stored with this vertex.
     *
     * @return
     */
    public List<EmbeddedVertex> getNeighbours() {
        if (dart == null) {
            return Collections.<EmbeddedVertex>emptyList();
        }

        List<EmbeddedVertex> result = new ArrayList<>();
        HalfEdge walkDart = dart;

        do {
            result.add(walkDart.getDestination());
            walkDart = walkDart.getTwin().getNext();
        } while (walkDart != dart);

        return result;
    }

    /**
     * Checks whether this vertex is adjacent to the given vertex.
     *
     * @param v
     * @return
     */
    public boolean isAdjacentTo(EmbeddedVertex v) {
        if (dart == null) {
            return false;
        }

        HalfEdge walkDart = dart;

        do {
            if (walkDart.getDestination() == v) {
                return true;
            }

            walkDart = walkDart.getTwin().getNext();
        } while (walkDart != dart);

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EmbeddedVertex other = (EmbeddedVertex) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.getX())) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.getY())) {
            return false;
        }
        return true;
    }
}
