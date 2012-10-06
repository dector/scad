package ua.org.dector.scad.model.nodes;

import com.sun.org.apache.xerces.internal.impl.dv.xs.YearDV;

/**
 * @author dector
 */
public class Signal {
    private static int lastId = -1;
    
    private int id;

    public Signal(int id) {
        this.id = id;
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
}
