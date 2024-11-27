/**
 * Edgar Quinones
 *
 * n : 1000, Avg. speedup: 17X
 * n : 2000, Avg. speedup: 14X
 * n : 3000, Avg. speedup: 31X
 * n : 4000, Avg. speedup: 48X
 * n : 5000, Avg. speedup: 81X
 * n : 6000, Avg. speedup: 109X
 * n : 7000, Avg. speedup: 145X
 * n : 8000, Avg. speedup: 193X
 * n : 9000, Avg. speedup: 252X
 * n : 10000, Avg. speedup: 323X
 */

package p2;

import java.io.*;
import java.util.Scanner;

public class SparseMatrix2D extends AbstractSparseMatrix2D {

    // The constructor; we set up the pink and blue nodes first in this constructor
    // Then we read the file and create the gray nodes one by one.
    protected SparseMatrix2D(String inputFileName) throws FileNotFoundException {
        // Do not forget to update the sizeOfMatrix variable once you read it from the file
        // Using the setEntryMethod, you need to populate the matrix; insert the entry immediately when you read it
        // Do not hold the entire file content in any array or data structure
        // protected MatrixNode[] rows, cols; inside the AbstractSparseMatrix2D holds the sparse matrix

        Scanner in = new Scanner(new File(inputFileName));
        sizeOfMatrix = Integer.parseInt(in.nextLine());

        rows = new MatrixNode[sizeOfMatrix];
        cols = new MatrixNode[sizeOfMatrix];
        // row[0] = null OR EMPTY Matrix Node
        rows[0] = new MatrixNode(2,3);

        for (int i = 0; i < sizeOfMatrix; i++) {
            rows[i] = new MatrixNode(i, 0);
            cols[i] = new MatrixNode(0, i);
        }

        while (in.hasNextLine()) {
            int row = in.nextInt();
            int col = in.nextInt();
            int entry = Integer.parseInt(in.nextLine().trim());

            setEntry(row, col, entry);
        }
        in.close();
    }

    // Please do not touch this method
    public void writeTheFourTraversalsToFile(String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter( fileName );

        fileWriter.write(printRowMajorLtoR());
        fileWriter.write(printRowMajorRtoL());
        fileWriter.write(printColMajorTtoB());
        fileWriter.write(printColMajorBtoT());

        fileWriter.close();
    }

    // Please do not touch this method; This method adds to matrices and sends the result to a file
    // Runs in O(n^3) time, where n is the size of the matrix
    public static void addMatricesCubic(SparseMatrix2D A, SparseMatrix2D B, String outputFile) throws IOException {
        if(A.sizeOfMatrix != B.sizeOfMatrix)
            throw new IllegalArgumentException("The input matrices must have a same size!");

        FileWriter output = new FileWriter(outputFile);
        output.write(A.sizeOfMatrix + "\n");

        // Runs in O(n^3) time
        for(int i = 0; i < A.sizeOfMatrix; i++)
            for(int j = 0; j < A. sizeOfMatrix; j++) {
                Integer Aij = A.getEntry(i, j); // takes O(n) tioutput.write(A.sizeOfMatrix + "\n");me
                Integer Bij = B.getEntry(i, j); // takes O(n) time
                if(Aij == null)  Aij = 0;
                if(Bij == null)  Bij = 0;
                if( Aij + Bij != 0) output.write(i + " " + j + " " + (Aij + Bij) + "\n");
            }
        output.close();
    }

    // Complete this method; This method should run in O(n^2) time
    // Do not declare new arrays/ArrayLists or any other data structure inside this method!
    public static void addMatricesQuadratic(SparseMatrix2D A, SparseMatrix2D B, String outputFile) throws IOException {
        if (A.sizeOfMatrix != B.sizeOfMatrix)
            throw new IllegalArgumentException("The input matrices must have a same size!");

        FileWriter output = new FileWriter(outputFile);

        output.write(A.sizeOfMatrix + "\n");

        for (int i = 0; i < A.sizeOfMatrix; i++)
        {
            MatrixNode currentA = A.rows[i].right;
            MatrixNode currentB = B.rows[i].right;

            while (currentA != null || currentB != null) {
                if (currentA != null && currentB == null) {
                    output.write(currentA.row + " " + currentA.col + " " + (currentA.entry) + "\n");
                    currentA = currentA.right;
                }
                else if (currentA == null && currentB != null) {
                    output.write(currentB.row + " " + currentB.col + " " + (currentB.entry) + "\n");
                    currentB = currentB.right;
                }
                else if(currentA.col == currentB.col) {
                    output.write(currentA.row + " " + currentA.col + " " + (currentA.entry + currentB.entry) + "\n");
                    currentA = currentA.right;
                    currentB = currentB.right;
                }
                else if (currentA.col < currentB.col) {
                    output.write(currentA.row + " " + currentA.col + " " + (currentA.entry) + "\n");
                    currentA = currentA.right;
                }
                else if (currentA.col > currentB.col){
                    output.write(currentB.row + " " + currentB.col + " " + (currentB.entry) + "\n");
                    currentB = currentB.right;
                }

            }
        }

        output.close();
    }


    // Complete this method; do not declare new arrays/ArrayLists or any other
    // data structure inside this method!
    private void setEntry(Integer i, Integer j, Integer entry) {
        if (i > sizeOfMatrix - 1 || j > sizeOfMatrix - 1)
            throw new IllegalArgumentException("At least one index is out of bound!");

        MatrixNode newNode = new MatrixNode(i, j, entry); // insert this node properly into the matrix

        // your code goes right after this...

        // if there is no node to the right of the head node, add the new node there
        if (rows[i].right == null) {
            rows[i].right = newNode;
            newNode.left = rows[i];
        } else { // else, keep going right until you found a node with a row less than the greater than the current node, if that dosent exist, put node at the end
            MatrixNode currentNode = rows[i].right;
            while (currentNode != null) {
                if (newNode.col < currentNode.col) {
                    MatrixNode leftNode = currentNode.left;
                    leftNode.right = newNode;
                    newNode.left = leftNode;
                    currentNode.left = newNode;
                    newNode.right = currentNode;
                    break;
                }
                else{
                    if (currentNode.right == null) {
                        currentNode.right = newNode;
                        newNode.left = currentNode;
                        break;
                    }
                    else {
                        currentNode = currentNode.right;
                    }
                }
            }
        }

        // if there is no node to the right of the head node, add the new node there
        if (cols[j].down == null) {
            cols[j].down = newNode;
            newNode.up = cols[j];
        } else { // else, keep going right until you found a node with a row less than the greater than the current node, if that dosent exist, put node at the end
            MatrixNode currentNode = cols[j].down;
            while (currentNode != null) {
                if (newNode.row < currentNode.row) {
                    MatrixNode aboveNode = currentNode.up;
                    aboveNode.down = newNode;
                    newNode.up = aboveNode;
                    currentNode.up = newNode;
                    newNode.down = currentNode;
                    break;
                }
                else{
                    if (currentNode.down == null) {
                        currentNode.down = newNode;
                        newNode.up = currentNode;
                        break;
                    }
                    else {
                        currentNode = currentNode.down;
                    }
                }
            }
        }

    }
}
