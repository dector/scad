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
}
