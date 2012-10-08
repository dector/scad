package ua.org.dector.scad.model.nodes;

/**
 * First logical node
 *
 * @author dector
 */
public class BeginNode extends Node {
    /**
     * null, because begin node has no prev
     *
     * @return null
     */
    public Node getPrev() {
        return null;
    }

    /**
     * Do nothing
     *
     * @param prev everything
     */
    public void setPrev(Node prev) {}

    public String toString() {
        return "B";
    }
}
