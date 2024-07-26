/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieSET;

import java.util.HashSet;


public class BoggleSolver {


    private final TrieSET dictionarySet;

    private HashSet<String> matches;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {

        if (dictionary == null) throw new IllegalArgumentException("Invalid dictionary");
        matches = new HashSet<>();
        dictionarySet = new TrieSET();
        for (String i : dictionary)
            dictionarySet.add(i);

    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {

        if (board == null) throw new IllegalArgumentException("Invalid board");
        matches = new HashSet<>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                boolean[][] marked = new boolean[board.rows()][board.cols()];
                StringBuilder edgeTo = new StringBuilder(board.cols() * board.rows());
                findWordsDFS(board, marked, new StringBuilder(edgeTo), i, j);
            }
        }
        return matches;
    }

    private void findWordsDFS(BoggleBoard board, boolean[][] marked, StringBuilder edgeTo, int i,
                              int j) {
        marked[i][j] = true;
        // System.out.println("i=" + i + "j=" + j);
        char ch = board.getLetter(i, j);
        if (ch == 'Q') edgeTo.append("QU");
        else edgeTo.append(ch);

        String cw = edgeTo.toString();
        // System.out.println("Current prefix : " + cw);
        // if (cw.contains("AID")) {
        //     System.out.println("Aid Present");
        //     System.exit(0);
        // }

        // if key exists
        if (cw.length() >= 3) if (dictionarySet.contains(cw)) {
            matches.add(cw);
        }
        else {
            // if prefix does not exist halt dfs path
            Iterable<String> it = dictionarySet.keysWithPrefix(cw);
            if (it == null) {
                marked[i][j] = false;
                return;
            }
        }
        for (int k = i - 1; k <= i + 1; k++) {
            for (int m = j - 1; m <= j + 1; m++) {
                if (i == k && j == m) continue;
                // adjacent chars for dfs :
                if (isValid(board, k, m) && !marked[k][m]) {
                    findWordsDFS(board, marked, new StringBuilder(edgeTo), k, m);
                }
            }
        }
        marked[i][j] = false;
    }

    private boolean isValid(BoggleBoard board, int i, int j) {
        return !(i < 0 || j < 0 || i >= board.rows() || j >= board.cols());
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) throw new IllegalArgumentException("Invalid word");
        if (!dictionarySet.contains(word)) return 0;
        int len = word.length();
        if (len < 3) return 0;
        if (len < 5) return 1;
        if (len < 6) return 2;
        if (len < 7) return 3;
        if (len < 8) return 5;
        return 11;

    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}

