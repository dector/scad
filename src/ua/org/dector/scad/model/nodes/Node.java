package ua.org.dector.scad.model.nodes;

/**
 * Composite graph structure representation
 *
 * @author dector
 */
public abstract class Node {
    private Node next;
    private Node prev;

    /**
     * Returns next node
     *
     * @return next
     */
    public Node getNext() {
        return next;
    }

    /**
     * Sets next node
     *
     * @param next next node to set
     */
    public void setNext(Node next) {
        this.next = next;
    }

    /**
     * Returns prev node
     *
     * @return prev
     */
    public Node getPrev() {
        return prev;
    }

    /**
     * Sets prev node
     *
     * @param prev prev node to set
     */
    public void setPrev(Node prev) {
        this.prev = prev;
    }

    /**
     * String representation
     *
     * @return string representation
     */
    public abstract String toString();
}
