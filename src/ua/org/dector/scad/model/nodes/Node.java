package ua.org.dector.scad.model.nodes;

/**
 * @author dector
 */
public abstract class Node {
    private Node next;
    private Node prev;

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
    
    public abstract String toString();
}
