package ua.org.dector.scad.model.nodes;

import com.sun.org.apache.xerces.internal.impl.dv.xs.YearDV;

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
}
