package ua.org.dector.scad;

import ua.org.dector.scad.model.Document;

import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author dector
 */
public class FileManager {
    private static final String EXTENSION           = ".scp";
    private static final String TRANS_MATRIX        = "tMatrix";
    private static final String SIGN_N_COND_MATRIX  = "sNcMatrix";
    
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

    public static Document restore(String file) throws IOException, ParseException {
        ZipInputStream zin = new ZipInputStream(new FileInputStream(file + EXTENSION));
        
        Document doc = new Document();

        int[][] transitionsMatrix;
        String[] signAndCondTitles;
        int[][] signAndCondMatrix;
        
        // Read matrixes
        zin.getNextEntry();
        
        transitionsMatrix = parseTransitionsMatrix(new Scanner(zin));

        System.out.println("Transitions matrix:");
        for (int[] vector : transitionsMatrix) {
            System.out.println(Arrays.toString(vector));
        }

        zin.closeEntry();
        zin.getNextEntry();

        Scanner secondScanner = new Scanner(zin);
        
        signAndCondTitles = parseSignAndCondTitles(secondScanner);

        System.out.println("Sign & cond titles:");
        System.out.println(Arrays.toString(signAndCondTitles));

        signAndCondMatrix = parseSignAndCondMatrix(secondScanner);

        System.out.println("Sign & cond matrix:");
        for (int[] vector : signAndCondMatrix) {
            System.out.println(Arrays.toString(vector));
        }

        zin.closeEntry();
        zin.close();
                
        // Setup all nodes
        // Insert conditional transitions and other arrows 

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
}
