package ua.org.dector.scad.model.nodes;

/**
 * Yi signal inside Operational node
 *
 * @author dector
 */
public class Signal implements Comparable<Signal> {
    private static int lastId = -1;
    
    private int id;

    /**
     * Default constructor
     *
     * @param id signal id
     */
    public Signal(int id) {
        setId(id);
    }

    /**
     * Sets id for signal
     *
     * @param id new id value
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns id
     *
     * @return id value
     */
    public int getId() {
        return id;
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
     * Returns last id
     *
     * @return last setted id
     */
    public static int getLasId() {
        return lastId;
    }
    
    public String toString() {
        return "Y" + id;
    }

    public int compareTo(Signal signal) {
        int thisId = getId();
        int otherId = signal.getId();
        
        if (thisId > otherId)
            return 1;
        else if (thisId < otherId)
            return -1;
        else
            return 0;
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (this.getClass() != other.getClass())
            return false;

        Signal otherObj = (Signal) other;
        return this.getId() == otherObj.getId();
    }

    public int hashCode() {
        return 76 + 133 * id;
    }
}
