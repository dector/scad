package ua.org.dector.scad.model.nodes;

/**
 * Some logical condition
 *
 * @author dector
 */
public class Condition {
    private static int lastId = -1;

    private int id;

    /**
     * Default constructor with id
     *
     * @param id cond id
     */
    public Condition(int id) {
        setId(id);
    }

    /**
     * Sets condition id
     *
     * @param id cond id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * REturns id
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Generates next ordered id
     *
     * @return next id
     */
    public static int nextId() {
        return ++lastId;
    }

    /**
     * Returns lastest setted id
     *
     * @return lastest setted
     */
    public static int getLasId() {
        return lastId;
    }

    public String toString() {
        return "X" + id;
    }
}
