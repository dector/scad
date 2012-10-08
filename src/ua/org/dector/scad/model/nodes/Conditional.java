package ua.org.dector.scad.model.nodes;

import ua.org.dector.scad.model.nodes.Node;

/**
 * Conditional node with condition
 *
 * @author dector
 */
public class Conditional extends Node {
    private Node nextNegative;

    private Condition condition;

    /**
     * Default constructor with condition
     *
     * @param condition condition
     */
    public Conditional(Condition condition) {
        this.condition = condition;
    }

    /**
     * If cond == 0, goto this node
     *
     * @return false-condition node
     */
    public Node getNextNegative() {
        return nextNegative;
    }

    /**
     * Sets false-condition node
     *
     * @param nextNegative new false-condition node
     */
    public void setNextNegative(Node nextNegative) {
        this.nextNegative = nextNegative;
    }

    /**
     * Returns condition
     *
     * @return condition
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * Sets condition
     *
     * @param condition new condition
     */
    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String toString() {
        return condition.toString();
    }
}
