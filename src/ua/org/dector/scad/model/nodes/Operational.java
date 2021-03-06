package ua.org.dector.scad.model.nodes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Operational node model
 *
 * @author dector
 */
public class Operational extends Node {
    private LinkedList<Signal> signals;

    /**
     * Default constructor with some signals
     *
     * @param signals {Yi} signals to init with
     */
    public Operational(Signal... signals) {
        this();
        
        for (Signal signal : signals)
            addSignal(signal);
    }

    private Operational() {
        signals = new LinkedList<Signal>();
    }

    /**
     * Add new signal
     *
     * @param signal added signal
     * @return true if added
     */
    public boolean addSignal(Signal signal) {
        return signals.add(signal);
    }

    /**
     * Count signals inside
     *
     * @return signals count
     */
    public int getSignalsCount() {
        return signals.size();
    }
    
    public Signal getSignal() {
        if (getSignalsCount() > 0)
            return signals.getFirst();
        else
            return null;
    }

    /**
     * Returns all inner signals
     *
     * @return signals as array
     */
    public Signal[] getSignals() {
        Signal[] signals = new Signal[getSignalsCount()];

        this.signals.toArray(signals);

        return signals;
    }
    
    /*public void replaceSignals(Signal... signals) {
        this.signals.clear();

        for (Signal signal : signals) {
            addSignal(signal);
        }
    }*/

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
