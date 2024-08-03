import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static final int R = 256; // ASCII alphabet size

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] sequence = new char[R];
        for (char i = 0; i < R; i++) {
            sequence[i] = i;
        }

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int index = 0;

            // Find the index of the character c in the sequence
            for (int i = 0; i < R; i++) {
                if (sequence[i] == c) {
                    index = i;
                    break;
                }
            }

            // Write the index to the output
            BinaryStdOut.write((char) index);

            // Move the character to the front of the sequence
            System.arraycopy(sequence, 0, sequence, 1, index);
            sequence[0] = c;
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] sequence = new char[R];
        for (char i = 0; i < R; i++) {
            sequence[i] = i;
        }

        while (!BinaryStdIn.isEmpty()) {
            // Read the index from input
            char index = BinaryStdIn.readChar();

            // Get the character at the index and write it
            char c = sequence[index];
            BinaryStdOut.write(c);

            // Move the character to the front of the sequence
            System.arraycopy(sequence, 0, sequence, 1, index);
            sequence[0] = c;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException(
                    "Usage: java MoveToFront - for encoding, + for decoding");
        }
        if (args[0].equals("-")) {
            encode();
        }
        else if (args[0].equals("+")) {
            decode();
        }
        else {
            throw new IllegalArgumentException("Invalid argument: " + args[0]);
        }
    }
}
