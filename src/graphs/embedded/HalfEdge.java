package graphs.embedded;

/**
 *
 * @author Sander Verdonschot <sander.verdonschot at gmail.com>
 */
public class HalfEdge {

    private Face face;
    private HalfEdge twin;
    private HalfEdge next;
    private HalfEdge previous;
    private EmbeddedVertex origin;

    public Face getFace() {
        return face;
    }

    public void setFace(Face face) {
        this.face = face;
    }

    public HalfEdge getNext() {
        return next;
    }

    public void setNext(HalfEdge next) {
        this.next = next;
    }

    public EmbeddedVertex getOrigin() {
        return origin;
    }

    public void setOrigin(EmbeddedVertex origin) {
        this.origin = origin;
    }

    public HalfEdge getPrevious() {
        return previous;
    }

    public void setPrevious(HalfEdge previous) {
        this.previous = previous;
    }

    public HalfEdge getTwin() {
        return twin;
    }

    public void setTwin(HalfEdge twin) {
        this.twin = twin;
    }

    public EmbeddedVertex getDestination() {
        return twin.getOrigin();
    }

    public double getLength() {
        double dx = getDestination().getX() - origin.getX();
        double dy = getDestination().getY() - origin.getY();

        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return origin + " -> " + getDestination();
    }
}
