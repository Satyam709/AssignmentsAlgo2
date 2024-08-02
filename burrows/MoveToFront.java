/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdIn;

public class MoveToFront {

    private static class Node {
        private Node next;
        private char ch;

        Node(char ch) {
            this.setCh(ch);
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public char getCh() {
            return ch;
        }

        public void setCh(char ch) {
            this.ch = ch;
        }
    }

    private static Node head;
    private static Node tail;

    private static void add(char ch) {
        Node newNode = new Node(ch);
        newNode.setNext(null);
        if (tail != null)
            tail.setNext(newNode);
        else
            head = newNode;
        tail = newNode;
    }

    private static int removeAddFirst(char ch) {
        if (head == null) throw new IllegalArgumentException("empty list");

        Node tmp = head;

        int index = 0;

        if (tmp.getCh() == ch)
            return index;
        while (tmp.getNext() != null) {
            index++;
            if (tmp.getNext().getCh() == ch) break;
            tmp = tmp.getNext();
        }
        if (tmp.getNext() != null) {
            tmp.setNext(tmp.getNext().getNext());
            Node newNode = new Node(ch);
            newNode.setNext(head);
            head = newNode;
        }
        return index;
    }

    private static void printList() {
        Node h = head;
        while (h != null) {
            System.out.print(h.getCh() + " ");
            h = h.getNext();
        }
        System.out.println();
    }

    private static void init() {
        // for (int i = 0; i < 256; i++)
        //     add((char) i);
        for (int i = 'A'; i < 'G'; i++) {
            add((char) i);
        }
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        init();
        String s = StdIn.readString();

        for (char ch : s.toCharArray()) {
            int index = removeAddFirst(ch);
            // printList();
            // System.out.println(index);
            BinaryStdOut.write(index, 8);
        }
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {

    }

    public static void main(String[] args) {
        encode();
    }
}