package graphs.linkedlistgraph;

import java.util.ArrayList;
import java.util.List;

/**
 * A vertex of a LinkedListGraph.
 */
public class LinkedListVertex {

    private double x, y; // The coordinates of this vertex
    private LinkedListVertexEntry head; // Pointer to the first element in the double-linked adjacency list
    private int degree; // The degree of this vertex

    /**
     * Creates a new LinkedListVertex with the given coordinates. Runs in O(1)
     * time.
     *
     * @param x
     * @param y
     */
    public LinkedListVertex(double x, double y) {
        this.x = x;
        this.y = y;
        head = null;
        degree = 0;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * Returns the first element in the double-linked adjacency list of this
     * vertex. Runs in O(1) time.
     *
     * @return
     */
    LinkedListVertexEntry getHead() {
        return head;
    }

    /**
     * Returns the degree of this vertex. Runs in O(1) time.
     *
     * @return
     */
    public int getDegree() {
        return degree;
    }

    /**
     * Returns true if this vertex has an outgoing or undirected edge to the
     * given vertex, false otherwise. Runs in O(degree) time.
     *
     * @param v
     * @return
     */
    public boolean isAdjacentTo(LinkedListVertex v) {
        LinkedListVertexEntry e = head;

        while (e != null && e.neighbour != v) {
            e = e.next;
        }

        if (e == null) {
            return false;
        } else {
            assert e.neighbour == v;
            return true;
        }
    }

    /**
     * Returns a collection of all neighbours of this vertex. Runs in O(degree)
     * time.
     *
     * @return
     */
    public List<LinkedListVertex> getNeighbours() {
        ArrayList<LinkedListVertex> ns = new ArrayList<>(degree);

        LinkedListVertexEntry e = head;

        while (e != null) {
            ns.add(e.neighbour);
            e = e.next;
        }

        return ns;
    }

    /**
     * Adds a neighbour to the head of the neighbour list and returns the new
     * entry. Runs in O(1) time.
     *
     * @param v
     * @return
     */
    LinkedListVertexEntry addNeighbour(LinkedListVertex v) {
        LinkedListVertexEntry e = new LinkedListVertexEntry();

        // Initialize the new entry's fields
        e.myVertex = this;
        e.neighbour = v;
        e.next = head;
        e.prev = null;

        // Add the new entry to the head of the list
        if (head != null) {
            head.prev = e;
        }

        head = e;

        // Increment degree
        degree++;

        return e;
    }

    /**
     * Makes all undirected edges of this vertex outgoing by removing the
     * corresponding entries from the adjacency lists of its neighbours. Runs in
     * O(degree) time.
     */
    public void directEdgesOutward() {
        LinkedListVertexEntry e = head;

        while (e != null) {
            if (e.twin != null) {
                e.twin.myVertex.degree--;
                e.twin.remove();
            } else {
                System.err.println("Twin was null");
            }

            e = e.next;
        }
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    class LinkedListVertexEntry {

        LinkedListVertexEntry prev, next;
        LinkedListVertexEntry twin;
        LinkedListVertex myVertex, neighbour;

        public void remove() {
            if (prev == null) {
                myVertex.head = next;
            } else {
                prev.next = next;
            }

            if (next != null) {
                next.prev = prev;
            }
        }
    }
}
