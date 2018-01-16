package graphs.embedded;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Sander Verdonschot <sander.verdonschot at gmail.com>
 */
public class Face {

    private HalfEdge dart;
    private boolean outerFace;

    public HalfEdge getDart() {
        return dart;
    }

    public void setDart(final HalfEdge dart) {
        this.dart = dart;
    }

    /**
     * True if this is the outer face, false otherwise.
     * @return
     */
    public boolean isOuterFace() {
        return outerFace;
    }

    public void setOuterFace(boolean outerFace) {
        this.outerFace = outerFace;
    }

    /**
     * Returns the size (number of vertices) of this face.
     *
     * @return
     */
    public int getSize() {
        if (dart == null) {
            return 0;
        }

        int count = 0;
        HalfEdge walkDart = dart;

        do {
            count++;
            walkDart = walkDart.getNext();
        } while (walkDart != dart);

        return count;
    }

    /**
     * Returns a list of the vertices of this face, in counter-clockwise order,
     * starting from the origin of the dart associated with this face.
     *
     * @return
     */
    public List<EmbeddedVertex> getVertices() {
        if (dart == null) {
            return Collections.<EmbeddedVertex>emptyList();
        }

        List<EmbeddedVertex> result = new ArrayList<>();
        HalfEdge walkDart = dart;

        do {
            result.add(walkDart.getOrigin());
            walkDart = walkDart.getNext();
        } while (walkDart != dart);

        return result;
    }

    /**
     * Returns a list of the darts of this face, in counter-clockwise order,
     * starting from the dart associated with this face.
     *
     * @return
     */
    public List<HalfEdge> getEdges() {
        if (dart == null) {
            return Collections.<HalfEdge>emptyList();
        }

        List<HalfEdge> darts = new ArrayList<>();
        HalfEdge walkDart = dart;

        do {
            darts.add(walkDart);
            walkDart = walkDart.getNext();
        } while (walkDart != dart);

        return darts;
    }
}
