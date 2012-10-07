package ua.org.dector.scad;

import ua.org.dector.scad.model.Document;
import ua.org.dector.scad.model.Item;
import ua.org.dector.scad.model.nodes.Arrow;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author dector
 */
public class FileManager {
    private static final String EXTENSION       = ".scp";
    private static final String TRANS_MATRIX    = "tMatrix";
    private static final String SIGN_MATRIX     = "sMatrix";
    
    private static final int UNCOND_OR_FALSE_TRANSITION     = 1;
    private static final int COND_TRUE_TRANSITION           = -1;

    public static void store(Document document, String file) throws IOException {
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(file + EXTENSION));
        PrintWriter bufZOUT = new PrintWriter(zout);

        ZipEntry transitionMatrixEntry = new ZipEntry(TRANS_MATRIX);
        zout.putNextEntry(transitionMatrixEntry);

        int[][] matrix = getTransitionMatrix(document);

        int lastIndex = matrix.length - 1;

        for (int i = 0; i <= lastIndex; i++) {
            for (int j = 0; j <= lastIndex; j++) {
                bufZOUT.print(matrix[i][j]);
                if (j != lastIndex)
                    bufZOUT.print(" ");
            }

            bufZOUT.println();
        }

        /*System.out.println("Transitions matrix:");
        for (int[] vector : matrix) {
            System.out.println(Arrays.toString(vector));
        }*/

        bufZOUT.flush();
        zout.closeEntry();

        ZipEntry signalsMatrixEntry = new ZipEntry(SIGN_MATRIX);
        zout.putNextEntry(signalsMatrixEntry);

        //

        zout.closeEntry();

        zout.close();
    }

    private static int[][] getTransitionMatrix(Document doc) {
        LinkedList<Item> numerableItems = new LinkedList<Item>();
        Map<Item, Integer> indexes = new HashMap<Item, Integer>();

        // Numerate B, Y, X, E nodes
        {
            int index = 0;
            Item item = doc.getHead();
            
            while (item != null) {
                if (isNumeratable(item)) {
                    numerableItems.add(item);
                    indexes.put(item, index++);
                }
                
                item = item.getNext();
            }
        }

        // Build matrix NxN
        int[][] matrix = new int[numerableItems.size()][numerableItems.size()];

        
        {
            Iterator<Item> itr = numerableItems.iterator();
            Item item = itr.next();
            Item next;
            int fromIndex;
            int toIndex;
            
            while (item.getType() != Item.Type.END) {
                // Set 1 - where is unconditional or positive conditional transition
                next = getNextItem(item);

                fromIndex = indexes.get(item);
                toIndex = indexes.get(next);
                
                matrix[fromIndex][toIndex] = UNCOND_OR_FALSE_TRANSITION;

                // Set -1 - where is negative conditional transition
                if (item.getType() == Item.Type.X) {
                    next = getNextItem(item.getNext());
                    
                    toIndex = indexes.get(next);
                    
                    matrix[fromIndex][toIndex] = COND_TRUE_TRANSITION;
                }
                
                item = itr.next();
            }
        }                       

        return matrix;
    }
    
    private static boolean isNumeratable(Item item) {
        Item.Type type = item.getType();
        
        return type == Item.Type.X || type == Item.Type.Y || type == Item.Type.BEGIN || type == Item.Type.END;
    }
    
    private static Item getNextItem(Item item) {
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
}
