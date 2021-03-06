package ua.org.dector.scad;

import ua.org.dector.scad.model.Document;
import ua.org.dector.scad.model.Item;
import ua.org.dector.scad.model.nodes.*;

import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Save/store document tools
 *
 * @author dector
 */
public class FileManager {
    private static final String EXTENSION           = ".scp";
    private static final String TRANS_MATRIX        = "tMatrix";
    private static final String SIGN_N_COND_MATRIX  = "sNcMatrix";

    /**
     * Store document to file
     *
     * @param document document to store
     * @param file project filename
     * @throws IOException if some shit happend
     */
    public static void store(Document document, String file) throws IOException {
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(file + EXTENSION));
        PrintWriter bufZOUT = new PrintWriter(zout);

        MatrixBuilder builder = new MatrixBuilder(document);
        builder.build();

        ZipEntry transitionMatrixEntry = new ZipEntry(TRANS_MATRIX);
        zout.putNextEntry(transitionMatrixEntry);

        {
            int[][] matrix = builder.getTransMatrix();

            int lastIndex = matrix.length - 1;

            for (int i = 0; i <= lastIndex; i++) {
                for (int j = 0; j <= lastIndex; j++) {
                    bufZOUT.print(matrix[i][j]);
                    if (j != lastIndex)
                        bufZOUT.print(" ");
                }

                bufZOUT.println();
            }
        }

        /*System.out.println("Transitions matrix:");
        for (int[] vector : matrix) {
            System.out.println(Arrays.toString(vector));
        }*/

        bufZOUT.flush();
        zout.closeEntry();

        ZipEntry signalsMatrixEntry = new ZipEntry(SIGN_N_COND_MATRIX);
        zout.putNextEntry(signalsMatrixEntry);

        // Build signals-n-conditions matrix
        {
            String[] titles = builder.getSignNCondTitles();
            int[][] matrix = builder.getSignNCondMatrix();

            for (int i = 0; i < titles.length ; i++) {
                bufZOUT.print(titles[i]);
                if (i != titles.length)
                    bufZOUT.print(" ");
            }
            bufZOUT.println();

            int lastIndex = matrix[0].length - 1;
            
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j <= lastIndex; j++) {
                    bufZOUT.print(matrix[i][j]);
                    if (j != lastIndex)
                        bufZOUT.print(" ");
                }

                bufZOUT.println();
            }
        }

        bufZOUT.flush();
        zout.closeEntry();

        zout.close();
    }

    /**
     * Load document from file
     *
     * @param file project filename
     * @return loaded document
     * @throws IOException if some I/O shit happend
     * @throws ParseException if project file was corrupted
     */
    public static Document restore(String file) throws IOException, ParseException {
        ZipInputStream zin = new ZipInputStream(new FileInputStream(file + EXTENSION));
        
        int[][] transitionsMatrix;
        String[] signAndCondTitles;
        int[][] signAndCondMatrix;
        
        // Read matrixes
        zin.getNextEntry();
        
        transitionsMatrix = parseTransitionsMatrix(new Scanner(zin));

        zin.closeEntry();
        zin.getNextEntry();

        Scanner secondScanner = new Scanner(zin);
        
        signAndCondTitles = parseSignAndCondTitles(secondScanner);
        signAndCondMatrix = parseSignAndCondMatrix(secondScanner);

        zin.closeEntry();
        zin.close();
                
        Document doc = new Document();
        
        buildDocument(doc, transitionsMatrix, signAndCondTitles, signAndCondMatrix);

        return doc;
    }
    
    private static int[][] parseTransitionsMatrix(Scanner in) throws ParseException {
        LinkedList<LinkedList<String>> lines = new LinkedList<LinkedList<String>>();
        LinkedList<String> line;

        while (in.hasNextLine()) {
            line = new LinkedList<String>();

            line.addAll(Arrays.asList(in.nextLine().split(" ")));

            lines.add(line);
        }

        int[][] matrix = new int[lines.size()][];
        int preferedSize = lines.getFirst().size();

        for (int i = 0; i < lines.size(); i++) {
            line = lines.get(i);

            if (line.size() != preferedSize)
                throw new ParseException("File is corrupted at line " + i, i);

            matrix[i] = new int[preferedSize];
            for (int j = 0; j < preferedSize; j++) {
                try{
                    matrix[i][j] = Integer.valueOf(line.get(j));
                } catch (NumberFormatException e) {
                    throw new ParseException("Not a number at " + i + ":" + j, i);
                }
            }
        }

        return matrix;
    }

    private static String[] parseSignAndCondTitles(Scanner in) throws ParseException {
        LinkedList<String> line = new LinkedList<String>();

        if (in.hasNextLine())
            line.addAll(Arrays.asList(in.nextLine().split(" ")));

        String[] titles = new String[line.size()];
        line.toArray(titles);

        return titles;
    }

    private static int[][] parseSignAndCondMatrix(Scanner in) throws ParseException {
        LinkedList<LinkedList<String>> lines = new LinkedList<LinkedList<String>>();
        LinkedList<String> line;

        while (in.hasNextLine()) {
            line = new LinkedList<String>();

            line.addAll(Arrays.asList(in.nextLine().split(" ")));

            lines.add(line);
        }

        int[][] matrix = new int[lines.size()][];
        int preferedSize = lines.getFirst().size();

        for (int i = 0; i < lines.size(); i++) {
            line = lines.get(i);

            if (line.size() != preferedSize)
                throw new ParseException("File is corrupted at line " + i, i);

            matrix[i] = new int[preferedSize];
            for (int j = 0; j < preferedSize; j++) {
                try{
                    matrix[i][j] = Integer.valueOf(line.get(j));
                } catch (NumberFormatException e) {
                    throw new ParseException("Not a number at " + i + ":" + j, i);
                }
            }
        }

        return matrix;
    }
    
    private static void buildDocument(Document doc, int[][] transitionsMatrix,
                                      String[] signAndCondTitles, int[][] signAndCondMatrix) throws ParseException {
        // Setup all nodes
        int nodeIndex = 1;
        String[] nodeEntries;
        String beg;
        int entryId;

        Signal[] signals = null;
        Condition condition = null;
        
        Item item = doc.getHead();
        
        Map<Integer, Item> indexedItems = new HashMap<Integer, Item>();

        indexedItems.put(0, doc.getHead());
        indexedItems.put(transitionsMatrix.length - 1, doc.getHead().getNext());
        
        while (nodeIndex <= transitionsMatrix.length - 2) {
            nodeEntries = findNodeEntries(nodeIndex, signAndCondTitles, signAndCondMatrix);

            beg = nodeEntries[0].substring(0, 1);
            for (int i = 1; i < nodeEntries.length; i++)
                if (! nodeEntries[i].startsWith(beg))
                    throw new ParseException("Mixing signals and conditions in one node", -1);

            // Check node type
            
            boolean operational;
            if (beg.equals("X")) {
                if (nodeEntries.length > 1)
                    throw new ParseException("More than one condition in conditional node", -1);
                
                operational = false;
            } else if (beg.equals("Y")) {
                operational = true;
                
                signals = new Signal[nodeEntries.length];
            } else
                throw new ParseException("WTF in signals and conditions table?", -1);
            
            // Create item & node
            
            for (int i = 0; i < nodeEntries.length; i++) {
                entryId = Integer.valueOf(nodeEntries[i].substring(1));
                
                if (operational) {
                    signals[i] = new Signal(entryId); 
                } else {
                    condition = new Condition(entryId);
                }
            }
            
            Item newItem;
            Node node;
            
            if (operational) {
                node = new Operational(signals);
                newItem = new Item(Item.Type.Y, 0);
            } else {
                node = new Conditional(condition);
                newItem = new Item(Item.Type.X, 0);
            }

            newItem.setNode(node);
            
            Item nextItem = item.getNext();
            
            item.setNext(newItem);
            newItem.setPrev(item);

            newItem.setNext(nextItem);
            nextItem.setPrev(newItem);

            indexedItems.put(nodeIndex, newItem);
                    
            item = newItem;
            nodeIndex++;

            /*System.out.println(newItem);*/
        }

        // Insert conditional transitions and other arrows

        // Check transitions matrix
        int transCount;

        for (int[] transitionVector : transitionsMatrix) {
            transCount = 0;

            for (int transition : transitionVector) {
                if (transition != 0) transCount++;
            }

            if (transCount > 2) {
                throw new ParseException("Transitions matrix is corrupted", -1);
            }
        }
        
        // Setup transitions
        Item currItem;
        Item otherItem = null;
        boolean done;
        int trans;
        int arrowId = 1;
        
        for (int from = 0; from < transitionsMatrix.length - 1; from++) {
            currItem = indexedItems.get(from);
            done = false;
            
            for (int to = 0; (to < transitionsMatrix.length) && (! done); to++) {
                trans = transitionsMatrix[from][to];
                
                Item.Type type = currItem.getType();
                
                if ((trans == 1 && type == Item.Type.Y && from != to - 1)
                        || (trans == 1 && type == Item.Type.X)) {
                    otherItem = indexedItems.get(to);

                    Item currNextItem = currItem.getNext();
                    Item otherPrevItem = otherItem.getPrev();
                    
                    Arrow upArrow = new Arrow(Item.Type.ARROW_UP, arrowId);
                    Arrow downArrow = new Arrow(Item.Type.ARROW_DOWN, arrowId);
                    upArrow.setPair(downArrow);
                    downArrow.setPair(upArrow);

                    arrowId++;

                    done = true;

                    currItem.setNext(upArrow);
                    upArrow.setPrev(currItem);

                    upArrow.setNext(currNextItem);
                    currNextItem.setPrev(upArrow);

                    otherPrevItem.setNext(downArrow);
                    downArrow.setPrev(otherPrevItem);

                    downArrow.setNext(otherItem);
                    otherItem.setPrev(downArrow);
                }
            }
        }
    }
    
    private static String[] findNodeEntries(int nodeIndex, String[] signAndCondTitles, int[][] signAndCondMatrix) {
        List<String> entries = new LinkedList<String>();

        nodeIndex--;
        
        for (int i = 0; i < signAndCondMatrix[nodeIndex].length; i++)
            if (signAndCondMatrix[nodeIndex][i] == 1)
                entries.add(signAndCondTitles[i]);
        
        String[] entriesArray = new String[entries.size()];

        entries.toArray(entriesArray);

        return entriesArray;
    }
}
