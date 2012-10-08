package ua.org.dector.scad.model;

import ua.org.dector.scad.model.nodes.*;

/**
 * Lsa node representation
 *
 * @author dector
 */
public class Item {
    /** Item type */
    public enum Type {BEGIN, X, Y, ARROW_UP, ARROW_DOWN, END }
    
    private Type type;
    private Item next;
    private Item prev;

    private Node node;

    /**
     * New instance
     *
     * @param type item type
     */
    public Item(Type type) {
        this(type, -1);
    }

    /**
     * New instance with id
     *
     * @param type item type
     * @param id init id
     */
    public Item(Type type, int id) {
        this.type = type;

        switch (type) {
            case BEGIN:
                node = new BeginNode();
                break;
            case X: {
                Condition cond = new Condition((id != -1) ? id : Condition.nextId());
                node = new Conditional(cond);
            } break;
            case Y: {
                Signal signal = new Signal((id != -1) ? id : Signal.nextId());
                node = new Operational(signal);
            } break;
            case END:
                node = new EndNode();
                break;
            default: break;
        }
    }

    /**
     * Returns next item
     *
     * @return next item
     */
    public Item getNext() {
        return next;
    }

    /**
     * Sets next item
     *
     * @param next next item
     */
    public void setNext(Item next) {
        this.next = next;
    }

    /**
     * True if next != null
     *
     * @return true if has next
     */
    public boolean hasNext() {
        return next != null;
    }

    /**
     * Return prev item
     *
     * @return prev item
     */
    public Item getPrev() {
        return prev;
    }

    /**
     * Sets prev item
     *
     * @param prev prev item
     */
    public void setPrev(Item prev) {
        this.prev = prev;
    }

    /**
     * Returns hav prev item
     *
     * @return true if prev != null
     */
    public boolean hasPrev() {
        return prev != null;
    }

    /**
     * Returns item type
     *
     * @return item type
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns connected node
     *
     * @return connected node
     */
    public Node getNode() {
        return node;
    }

    /**
     * Sets connected node
     *
     * @param node new node
     */
    public void setNode(Node node) {
        switch (type) {
            case Y:
                if (node.getClass() == Operational.class)
                    this.node = node;
                break;
            default: break;
        }
    }
    
    public String toString() {
        return (node != null) ? node.toString() : "n/a";
    }
}
