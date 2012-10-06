package ua.org.dector.scad.model.nodes;

import ua.org.dector.scad.model.nodes.Node;

/**
 * @author dector
 */
public class Conditional extends Node {
    private Node nextNegative;

    private Condition condition;

    public Conditional(Condition condition) {
        this.condition = condition;
    }

    public Node getNextNegative() {
        return nextNegative;
    }

    public void setNextNegative(Node nextNegative) {
        this.nextNegative = nextNegative;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String toString() {
        return condition.toString();
    }
}
