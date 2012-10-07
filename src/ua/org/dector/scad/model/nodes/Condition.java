package ua.org.dector.scad.model.nodes;

/**
 * @author dector
 */
public class Condition {
    private static int lastId = -1;

    private int id;

    public Condition(int id) {
        setId(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static int nextId() {
        return ++lastId;
    }

    public static int getLasId() {
        return lastId;
    }

    public String toString() {
        return "X" + id;
    }
}
