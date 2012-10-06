package ua.org.dector.scad.model.nodes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dector
 */
public class Operational extends Node {
    private List<Signal> signals;

    public Operational() {
        signals = new ArrayList<Signal>();
    }
}
