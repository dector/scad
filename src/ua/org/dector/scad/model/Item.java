package ua.org.dector.scad.model;

import ua.org.dector.scad.model.nodes.*;

/**
 * @author dector
 */
public class Item {
    public enum Type { START, X, Y, ARROW_UP, ARROW_DOWN, END }
    
    private Type type;
    private Item next;
    private Item prev;

    private Node node;

    public Item(Type type) {
        this.type = type;

        switch (type) {
            case START:
                node = new BeginNode(); break;
            case X:
                node = new Conditional(new Condition(Condition.nextId())); break;
            case Y:
                node = new Operational(new Signal(Signal.nextId())); break;
            case END:
                node = new EndNode(); break;
            default: break;
        }
    }
    
    public Item getNext() {
        return next;
    }

    public void setNext(Item next) {
        this.next = next;
    }

    public boolean hasNext() {
        return next != null;
    }
    
    public Item getPrev() {
        return prev;
    }

    public void setPrev(Item prev) {
        this.prev = prev;
    }

    public boolean hasPrev() {
        return prev != null;
    }

    public Type getType() {
        return type;
    }

    public Node getNode() {
        return node;
    }
    
    public String toString() {
        String s;
        
        switch (type) {
            case ARROW_DOWN:
                s = "V"; break;
            case ARROW_UP:
                s = "^"; break;
            default:
                s = node.toString();
        }
        
        return s;
    }
}
