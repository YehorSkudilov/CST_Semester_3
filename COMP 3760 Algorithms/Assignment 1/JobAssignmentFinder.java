import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * COMP 3760 Assignment 1
 *
 * Author: Yehor Skudilov
 * Student ID: A01439865
 *
 * Solves the "job assignment" problem by brute force. A benefit matrix of size
 * NxN is loaded from a data file, where row X, column Y is the benefit of
 * assigning person X to job Y. Every possible assignment (every permutation of
 * the jobs 0..N-1) is generated and totalled, and the assignment with the
 * largest total benefit is kept as the answer.
 */
public class JobAssignmentFinder {

    /** Value returned by getInputSize() when no data file has been loaded. */
    private static final int NO_INPUT = -1;

    /** The currently loaded NxN benefit matrix, or null if nothing is loaded. */
    private int[][] benefitMatrix;

    /** The best assignment found for the current matrix, or null if not yet computed. */
    private ArrayList<Integer> maxAssignment;

    /** The total benefit of maxAssignment. */
    private int maxAssignmentTotalValue;

    /**
     * Loads a new benefit matrix from the given file, replacing any matrix and
     * results from a previous call. The first line of the file holds N, followed
     * by N lines of N space-separated integers. If the file cannot be opened,
     * the finder is left with no matrix loaded.
     *
     * @param fileName full path of the data file to read
     */
    public void readDataFile(String fileName) {
        benefitMatrix = null;
        maxAssignment = null;
        maxAssignmentTotalValue = 0;

        try (Scanner scanner = new Scanner(new File(fileName))) {
            int size = scanner.nextInt();
            int[][] matrix = new int[size][size];

            for (int person = 0; person < size; person++) {
                for (int job = 0; job < size; job++) {
                    matrix[person][job] = scanner.nextInt();
                }
            }

            benefitMatrix = matrix;
        } catch (FileNotFoundException e) {
        }
    }

    /**
     * Returns N, the size of the currently loaded NxN benefit matrix, or -1 if
     * no data file has been loaded.
     *
     * @return the matrix size N, or -1
     */
    public int getInputSize() {
        if (benefitMatrix == null) {
            return NO_INPUT;
        }
        return benefitMatrix.length;
    }

    /**
     * Returns the currently loaded benefit matrix as a plain NxN array.
     *
     * @return the benefit matrix
     */
    public int[][] getBenefitMatrix() {
        return benefitMatrix;
    }

    /**
     * Builds a readable table of the current benefit matrix, with people as
     * rows and jobs as columns, each column right-aligned to the widest value.
     *
     * @return a string representation of the benefit matrix
     */
    public String benefitMatrixToString() {
        if (benefitMatrix == null) {
            return "(no benefit matrix loaded)";
        }

        int size = benefitMatrix.length;

        // Find the widest number so every column lines up
        int width = String.valueOf(size - 1).length() + 1;
        for (int[] row : benefitMatrix) {
            for (int value : row) {
                width = Math.max(width, String.valueOf(value).length());
            }
        }
        String cell = " %" + width + "s";
        String label = "%-" + (String.valueOf(size - 1).length() + 2) + "s|";

        StringBuilder builder = new StringBuilder();

        // Header row of job numbers
        builder.append(String.format(label, ""));
        for (int job = 0; job < size; job++) {
            builder.append(String.format(cell, "J" + job));
        }
        builder.append(System.lineSeparator());

        // Divider under the header
        int headerLength = builder.length() - System.lineSeparator().length();
        builder.append("-".repeat(headerLength)).append(System.lineSeparator());

        // One row per person
        for (int person = 0; person < size; person++) {
            builder.append(String.format(label, "P" + person));
            for (int job = 0; job < size; job++) {
                builder.append(String.format(cell, benefitMatrix[person][job]));
            }
            builder.append(System.lineSeparator());
        }

        return builder.toString();
    }

    /**
     * Returns the job assignment with the maximum total benefit. Position i of
     * the list holds the job given to person i.
     *
     * @return a permutation of 0..N-1 representing the best assignment
     */
    public ArrayList<Integer> getMaxAssignment() {
        findMaxAssignment();
        return new ArrayList<Integer>(maxAssignment);
    }

    /**
     * Returns the total benefit of the maximum job assignment for the currently
     * loaded benefit matrix.
     *
     * @return the maximum total benefit
     */
    public int getMaxAssignmentTotalValue() {
        findMaxAssignment();
        return maxAssignmentTotalValue;
    }

    /**
     * Returns the benefit of assigning the given person to the given job.
     *
     * @param person the person, 0..N-1
     * @param job    the job, 0..N-1
     * @return benefitMatrix[person][job]
     */
    public int getBenefit(int person, int job) {
        return benefitMatrix[person][job];
    }

    /**
     * Runs the brute force search if it has not already been run for the
     * current matrix: generates every permutation of the jobs, totals the
     * benefit of each, and remembers the first one with the largest total.
     */
    private void findMaxAssignment() {
        if (maxAssignment != null) {
            return;
        }

        ArrayList<ArrayList<Integer>> allAssignments = getPermutations(benefitMatrix.length);

        ArrayList<Integer> bestAssignment = null;
        int bestTotal = Integer.MIN_VALUE;

        for (ArrayList<Integer> assignment : allAssignments) {
            int total = getAssignmentTotalValue(assignment);
            if (total > bestTotal) {
                bestTotal = total;
                bestAssignment = assignment;
            }
        }

        maxAssignment = bestAssignment;
        maxAssignmentTotalValue = bestTotal;
    }

    /**
     * Adds up the benefit of every person's job in the given assignment.
     *
     * @param assignment position i holds the job given to person i
     * @return the total benefit of the assignment
     */
    private int getAssignmentTotalValue(ArrayList<Integer> assignment) {
        int total = 0;
        for (int person = 0; person < assignment.size(); person++) {
            total += benefitMatrix[person][assignment.get(person)];
        }
        return total;
    }

    /**
     * Recursive decrease-and-conquer algorithm to generate a list of all
     * permutations of the numbers 0..N-1. This follows the "decrease by 1" pattern
     * of decrease and conquer algorithms.
     *
     * This method returns an ArrayList of ArrayLists. One permutation is an
     * ArrayList containing 0,1,2,...,N-1 in some order. The final result is an
     * ArrayList containing N! of those permutations.
     *
     * @param N
     * @return
     */
    @SuppressWarnings("unchecked")
    private ArrayList<ArrayList<Integer>> getPermutations(int N) {
        ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();

        /**
         * This isn't a "base case", it's a "null case". This function does not call
         * itself with an argument of zero, but we can't prevent another caller from
         * doing so. It's a weird result, though. The list of permutations has one
         * permutation, but the one permutation is empty (0 elements).
         */
        if (N == 0) {
            ArrayList<Integer> emptyList = new ArrayList<Integer>();
            results.add(emptyList);

        } else if (N == 1) {
            /**
             * Now THIS is the base case. Create an ArrayList with a single integer, and add
             * it to the results list.
             */
            ArrayList<Integer> singleton = new ArrayList<Integer>();
            singleton.add(0);
            results.add(singleton);

        } else {
            /**
             * And: the main part. First a recursive call (this is a decrease and conquer
             * algorithm) to get all the permutations of length N-1.
             */
            ArrayList<ArrayList<Integer>> smallList = getPermutations(N - 1);

            /**
             * Iterate over the list of smaller permutations and insert the value 'N-1' into
             * every permutation in every possible position, adding each new permutation to
             * the big list of permutations.
             */
            for (ArrayList<Integer> perm : smallList) {

                /**
                 * Add 'N-1' -- the biggest number in the new permutation -- at each of the
                 * positions from 0..N-1.
                 */
                for (int i = 0; i < perm.size(); i++) {
                    ArrayList<Integer> newPerm = (ArrayList<Integer>) perm.clone();
                    newPerm.add(i, N - 1);
                    results.add(newPerm);
                }

                /**
                 * Add 'N-1' at the end (i.e. at position "size").
                 */
                ArrayList<Integer> newPerm = (ArrayList<Integer>) perm.clone();
                newPerm.add(N - 1);
                results.add(newPerm);

            }

        }

        /**
         * Nothing left to do except:
         */
        return results;
    }
}
