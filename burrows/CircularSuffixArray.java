import java.util.Arrays;

public class CircularSuffixArray {
    private final int n; // length of the original string
    private final int[] indices; // array of indices for sorted suffixes

    // Circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("String cannot be null");
        n = s.length();
        indices = new int[n];

        // Create array of circular suffixes
        String[] suffixes = new String[n];
        for (int i = 0; i < n; i++) {
            suffixes[i] = s.substring(i) + s.substring(0, i);
        }

        // Sort suffixes and store the indices
        Integer[] indicesArray = new Integer[n];
        for (int i = 0; i < n; i++) {
            indicesArray[i] = i;
        }

        Arrays.sort(indicesArray, (i, j) -> suffixes[i].compareTo(suffixes[j]));

        for (int i = 0; i < n; i++) {
            indices[i] = indicesArray[i];
        }
    }

    // Length of s
    public int length() {
        return n;
    }

    // Returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= n) throw new IllegalArgumentException("Index out of bounds");
        return indices[i];
    }

    // Unit testing (required)
    public static void main(String[] args) {
        String s = "ABRACADABRA";
        CircularSuffixArray csa = new CircularSuffixArray(s);

        System.out.println("Length: " + csa.length());
        for (int i = 0; i < csa.length(); i++) {
            System.out.println("Index of sorted suffix " + i + ": " + csa.index(i));
        }
    }
}
