package ua.org.dector.scad.model.nodes;

/**
 * @author dector
 */
public class Signal implements Comparable<Signal> {
    private static int lastId = -1;
    
    private int id;

    public Signal(int id) {
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
