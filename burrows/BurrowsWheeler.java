import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    private static final int EXTENDED_ASCII = 256; // Define global constant for extended ASCII

    // Apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int length = csa.length();

        int first = -1;

        for (int i = 0; i < length; i++) {
            int index = csa.index(i);

            // Find the original first row
            if (index == 0) {
                first = i;
                break;
            }
        }

        // Output the first index of the sorted suffix array
        BinaryStdOut.write(first);

        // Write the BWT string
        for (int i = 0; i < length; i++) {
            int index = csa.index(i);
            // Last character of the suffix starting at index
            char lastChar = s.charAt((index + length - 1) % length);
            BinaryStdOut.write(lastChar);
        }

        BinaryStdOut.close();
    }

    // Apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        // Read the first row index
        int first = BinaryStdIn.readInt();
        // Read the BWT transformed string
        String bwt = BinaryStdIn.readString();
        int n = bwt.length();

        int[] next = new int[n];
        int[] count = new int[EXTENDED_ASCII + 1]; // count array for counting sort
        char[] sortedBWT = new char[n];

        // Count the occurrences of each character
        for (int i = 0; i < n; i++) {
            count[bwt.charAt(i) + 1]++;
        }

        // Compute the cumulative counts
        for (int r = 0; r < EXTENDED_ASCII; r++) {
            count[r + 1] += count[r];
        }

        // Use the cumulative counts to sort the characters in bwt[]
        for (int i = 0; i < n; i++) {
            char c = bwt.charAt(i);
            int pos = count[c]++;
            sortedBWT[pos] = c;
            next[pos] = i;
        }

        // Reconstruct the original string
        for (int i = 0, current = first; i < n; i++) {
            BinaryStdOut.write(sortedBWT[current]);
            current = next[current];
        }

        BinaryStdOut.close();
    }

    // If args[0] is "-", apply Burrows-Wheeler transform
    // If args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException(
                    "Usage: java BurrowsWheeler - for transform, + for inverse transform");
        }
        if (args[0].equals("-")) {
            transform();
        }
        else if (args[0].equals("+")) {
            inverseTransform();
        }
        else {
            throw new IllegalArgumentException("Invalid argument: " + args[0]);
        }
    }
}
