/**
 * COMP 3760 Lab 1 test driver
 *
 * Author: Yehor Skudilov
 * Student ID: A01439865
 *
 * Runs one JobAssignmentFinder over every sample data file in a single
 * execution and prints each benefit matrix, the maximum assignment found, and
 * its total value, so the results can be checked against the expected table in
 * the lab handout. Not part of the submission.
 */
public class Main {

    /** Folder holding the sample data files. Change this if the files move. */
    private static final String DATA_FOLDER = "C:\\GitHub\\CST_Semester_3\\COMP 3760 Algorithms\\Assignment 1\\";

    /** Sample data files to run, inside DATA_FOLDER. */
    private static final String[] DATA_FILES = {
        "data0.txt", "data1.txt", "data2.txt", "data3.txt",
        "data4.txt", "data5.txt", "data6.txt", "data7.txt"
    };

    public static void main(String[] args) {
        JobAssignmentFinder finder = new JobAssignmentFinder();
        System.out.println("Input size before any file is read: " + finder.getInputSize());
        System.out.println();

        for (String fileName : DATA_FILES) {
            finder.readDataFile(DATA_FOLDER + fileName);
            if (finder.getInputSize() == -1) {
                System.out.println("Data file: " + fileName + " could not be read from " + DATA_FOLDER);
                System.out.println();
                continue;
            }
            System.out.println("Data file: " + fileName + " (N = " + finder.getInputSize() + ")");
            System.out.print(finder.benefitMatrixToString());
            System.out.println("Max assignment: " + finder.getMaxAssignment());
            System.out.println("Total value:    " + finder.getMaxAssignmentTotalValue());
            System.out.println();
        }
    }
}
