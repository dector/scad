package ua.org.dector.scad;

import ua.org.dector.scad.model.Document;
import ua.org.dector.scad.model.Item;
import ua.org.dector.scad.model.nodes.Arrow;
import ua.org.dector.scad.model.nodes.Conditional;
import ua.org.dector.scad.model.nodes.Operational;
import ua.org.dector.scad.model.nodes.Signal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author dector
 */
public class MatrixBuilder {
    private static final int UNCOND_OR_FALSE_TRANSITION     = 1;
    private static final int COND_TRUE_TRANSITION           = -1;
    
    private static final int SIGN_OR_COND_PRESENT           = 1;
    
    private LinkedList<Item> numerableItems;
    private LinkedList<String> signalsAndConditions;
    private Map<Item, Integer> indexes;
    
    private Document doc;

    private int[][] transMatrix;
    private String[] signNCondTitles;
    private int[][] signNCondMatrix;
    
    public MatrixBuilder(Document doc) {
        this.doc = doc;

        numerableItems = new LinkedList<Item>();
        signalsAndConditions = new LinkedList<String>();
        indexes = new HashMap<Item, Integer>();
    }

    public void build() {
        prepare();
        buildTransMatrix();       
        buildSignNCondTitles();
        buildSignNCondMatrix();
    }

    private void prepare() {
        int index = 0;
        Item item = doc.getHead();

        while (item != null) {
            if (isNumerable(item)) {
                numerableItems.add(item);
                indexes.put(item, index++);
                
                switch (item.getType()) {
                    case Y: {
                        Operational node = (Operational) item.getNode();

                        for (Signal signal : node.getSignals())
                            signalsAndConditions.add(signal.toString());
                    } break;
                    case X: {
                        Conditional node = (Conditional) item.getNode();
                        signalsAndConditions.add(node.getCondition().toString());
                    } break;
                    default: break;
                }
            }

            item = item.getNext();
        }
    }
    
    private void buildTransMatrix() {
        transMatrix = new int[numerableItems.size()][numerableItems.size()];

        Iterator<Item> itr = numerableItems.iterator();
        Item item = itr.next();
        Item next;
        int fromIndex;
        int toIndex;

        while (item.getType() != Item.Type.END) {
            // Set 1 - where is unconditional or positive conditional transition
            next = getNextNumerable(item);

            fromIndex = indexes.get(item);
            toIndex = indexes.get(next);

            transMatrix[fromIndex][toIndex] = UNCOND_OR_FALSE_TRANSITION;

            // Set -1 - where is negative conditional transition
            if (item.getType() == Item.Type.X) {
                next = getNextNumerable(item.getNext());

                toIndex = indexes.get(next);

                transMatrix[fromIndex][toIndex] = COND_TRUE_TRANSITION;
            }

            item = itr.next();
        }
    }
    
    private void buildSignNCondTitles() {
        signNCondTitles = new String[signalsAndConditions.size()];
        
        for (int i = 0; i < signNCondTitles.length; i++)
            signNCondTitles[i] = signalsAndConditions.get(i);
    }
    
    private void buildSignNCondMatrix() {
        signNCondMatrix = new int[numerableItems.size() - 2][signalsAndConditions.size()];
        
        Iterator<Item> itr = numerableItems.iterator();
        {
            itr.next(); // Pass begin item
        }
        Item item = itr.next();
        Item.Type type = item.getType();
        int inIndex;
        int whatIndex;
        
        while (type != Item.Type.END) {
            inIndex = indexes.get(item) - 1;
                                      
            switch (item.getType()) {
                case X: {
                    Conditional node = (Conditional) item.getNode();
                    whatIndex = signalsAndConditions.indexOf(node.getCondition().toString());
                    
                    signNCondMatrix[inIndex][whatIndex] = SIGN_OR_COND_PRESENT;
                } break;
                case Y: {
                    Operational node = (Operational) item.getNode();
                    
                    for (Signal signal : node.getSignals()) {
                        whatIndex = signalsAndConditions.indexOf(signal.toString());
                        signNCondMatrix[inIndex][whatIndex] = SIGN_OR_COND_PRESENT;
                    }
                } break;
                default: break;
            }
            
            item = itr.next();
            type = item.getType();
        }
    }

    private static boolean isNumerable(Item item) {
        Item.Type type = item.getType();

        return type == Item.Type.X || type == Item.Type.Y || type == Item.Type.BEGIN || type == Item.Type.END;
    }

    private static Item getNextNumerable(Item item) {
        Item next = item.getNext();
        boolean found = false;

        while (! found) {
            switch (next.getType()) {
                case X:
                case Y:
                case END:
                    found = true;
                    break;
                case ARROW_UP:
                    next = ((Arrow) next).getPair().getNext();
                    break;
                case ARROW_DOWN:
                    next = next.getNext();
                    break;
                default: break;
            }
        }

        return next;
    }

    public int[][] getTransMatrix() {
        return transMatrix;
    }

    public String[] getSignNCondTitles() {
        return signNCondTitles;
    }

    public int[][] getSignNCondMatrix() {
        return signNCondMatrix;
    }
}
