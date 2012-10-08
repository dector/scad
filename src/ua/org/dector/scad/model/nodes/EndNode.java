package ua.org.dector.scad.model.nodes;

/**
 * End logical node
 *
 * @author dector
 */
public class EndNode extends Node {
    /**
     * Do nothing
     *
     * @param next some value
     */
    public void setNext(Node next) {}

    /**
     * null, because end node is the lastest
     *
     * @return null
     */
    public Node getNext() {
        return null;
    }

    public String toString() {
        return "E";
    }
}
