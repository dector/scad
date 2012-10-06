package ua.org.dector.scad.model.nodes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author dector
 */
public class Operational extends Node {
    private LinkedList<Signal> signals;

    public Operational(Signal signal) {
        signals = new LinkedList<Signal>();
        
        addSignal(signal);
    }
    
    public boolean addSignal(Signal signal) {
        return signals.add(signal);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        if (signals.size() == 1) {
            sb.append(signals.getFirst());
        } else {
            sb.append("(");
            
            Signal last = signals.getLast();
            
            for (Signal signal : signals) {
                sb.append(signal);
                
                if (signal != last)
                    sb.append(" ");
            }
            
            sb.append(")");
        }
        
        return sb.toString();
    }
}
