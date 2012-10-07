package ua.org.dector.scad.model.nodes;

import ua.org.dector.scad.model.Item;

/**
 * @author dector
 */
public class Arrow extends Item {
    private static int lastId = -1;

    private int id;
    
    private Arrow pair;

    public Arrow(Type type, int id) {
        super(type, id);

        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Arrow getPair() {
        return pair;
    }

    public void setPair(Arrow pair) {
        this.pair = pair;
    }

    public static int nextId() {
        return ++lastId;
    }

    public static int getLasId() {
        return lastId;
    }

    public static void decLastId() {
        lastId--;
    }
    
    public String toString() {
        switch (getType()) {
            case ARROW_DOWN:
                return "V" + getId();
            case ARROW_UP:
                return "^" + getId();
            default:
                return super.toString();
        }
    }
}
