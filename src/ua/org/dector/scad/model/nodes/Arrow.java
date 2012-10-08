package ua.org.dector.scad.model.nodes;

import ua.org.dector.scad.model.Item;

/**
 * Lsa item -> arrow with pare
 *
 * @author dector
 */
public class Arrow extends Item {
    private static int lastId = 0;

    private int id;
    
    private Arrow pair;

    /**
     * Default constructor
     *
     * @param type down/up arrow
     * @param id arrow id
     */
    public Arrow(Type type, int id) {
        super(type, id);

        this.id = id;
    }

    /**
     * Returns id
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns linked arrow
     *
     * @return linked up/down arrow
     */
    public Arrow getPair() {
        return pair;
    }

    /**
     * Returns pair arrow
     *
     * @param pair pair arrow
     */
    public void setPair(Arrow pair) {
        this.pair = pair;
    }

    /**
     * Generates next id
     *
     * @return next id
     */
    public static int nextId() {
        return ++lastId;
    }

    /**
     * Returns lastest setted id
     *
     * @return lastest id
     */
    public static int getLasId() {
        return lastId;
    }

    /**
     * Dec last id
     */
    public static void decLastId() {
        lastId--;
    }
    
    public String toString() {
        switch (getType()) {
            case ARROW_DOWN:
                return "↓" + getId();
            case ARROW_UP:
                return "↑" + getId();
            default:
                return super.toString();
        }
    }
}
