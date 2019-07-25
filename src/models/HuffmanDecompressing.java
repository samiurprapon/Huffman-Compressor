package models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.PriorityQueue;

public class HuffmanDecompressing {
    private static PriorityQueue<TREE> priorityQueue = new PriorityQueue<>();
    private static int[] frequencies = new int[300];
    private static String[] strings = new String[300];
    private static String[] intToBinary = new String[300];
    private static String largeString;
    private static String temp;
    private static int extraBits;

    // CODE MULTIPLE OF 8
    private static int putIt;
    private static int numberOfFrequency;

    static class TREE implements Comparable<TREE> {
        TREE leftChild;
        TREE rightChild;
        String deb;
        int Bite;
        int frequency;

        public int compareTo(TREE tree) {
            if (this.frequency < tree.frequency) {
                return -1;
            } else if (this.frequency > tree.frequency) {
                return 1;
            }

            return 0;
        }
    }

    private static TREE root;

    private static void initializeZipping() {
        if (root != null)
            byteToInteger(root);
        for (int i = 0; i < 300; i++)
            frequencies[i] = 0;
        for (int i = 0; i < 300; i++)
            strings[i] = "";

        priorityQueue.clear();
        largeString = "";
        temp = "";
        extraBits = 0;
        putIt = 0; //
        numberOfFrequency = 0;
    }


    private static void byteToInteger(TREE tree) {

        if (tree.leftChild == null && tree.rightChild == null) {
            tree = null;
            return;
        }
        if (tree.leftChild != null)
            byteToInteger(tree.leftChild);
        if (tree.rightChild != null)
            byteToInteger(tree.rightChild);
    }

    private static void depthFirstSearch(TREE tree, String st) {
        tree.deb = st;
        if ((tree.leftChild == null) && (tree.rightChild == null)) {
            strings[tree.Bite] = st;
            return;
        }
        if (tree.leftChild != null)
            depthFirstSearch(tree.leftChild, st + "0");
        if (tree.rightChild != null)
            depthFirstSearch(tree.rightChild, st + "1");
    }


    private static void makeNode() {

        numberOfFrequency = 0;

        for (int i = 0; i < 300; i++) {
            if (frequencies[i] != 0) {

                TREE Temp = new TREE();
                Temp.Bite = i;
                Temp.frequency = frequencies[i];
                Temp.leftChild = null;
                Temp.rightChild = null;
                priorityQueue.add(Temp);
                numberOfFrequency++;
            }

        }
        TREE temp1, temp2;

        if (numberOfFrequency == 0) {
            return;
        } else if (numberOfFrequency == 1) {
            for (int i = 0; i < 300; i++)
                if (frequencies[i] != 0) {
                    strings[i] = "0";
                    break;
                }
            return;
        }

        // will there b a problem if the file is empty
        // a bug is found if there is only one character
        while (priorityQueue.size() != 1) {
            TREE temp = new TREE();
            temp1 = priorityQueue.poll();
            temp2 = priorityQueue.poll();

            temp.leftChild = temp1;
            temp.rightChild = temp2;
            temp.frequency = temp1.frequency + temp2.frequency;
            priorityQueue.add(temp);
        }
        root = priorityQueue.poll();
    }


    private static void readFrequency(String codes) {

        File filei = new File(codes);
        int fey, i;
        Byte baital;
        try {
            FileInputStream file_input = new FileInputStream(filei);
            DataInputStream data_in = new DataInputStream(file_input);
            numberOfFrequency = data_in.readInt();

            for (i = 0; i < numberOfFrequency; i++) {
                baital = data_in.readByte();
                fey = data_in.readInt();
                frequencies[byteToInteger(baital)] = fey;
            }
            data_in.close();
            file_input.close();
        } catch (IOException e) {
            System.out.println("IO exception = " + e);
        }

        makeNode(); // makeing corresponding nodes
        if (numberOfFrequency > 1)
            depthFirstSearch(root, "");

        for (i = 0; i < 256; i++) {
            if (strings[i] == null)
                strings[i] = "";
        }
        filei = null;
    }

    private static void createBinary() {
        int i, j;
        String t;
        for (i = 0; i < 256; i++) {
            intToBinary[i] = "";
            j = i;
            while (j != 0) {
                if (j % 2 == 1)
                    intToBinary[i] += "1";
                else
                    intToBinary[i] += "0";
                j /= 2;
            }
            t = "";
            for (j = intToBinary[i].length() - 1; j >= 0; j--) {
                t += intToBinary[i].charAt(j);
            }
            intToBinary[i] = t;
        }
        intToBinary[0] = "0";
    }

    private static int got() {
        int i;

        for (i = 0; i < 256; i++) {
            if (strings[i].compareTo(temp) == 0) {
                putIt = i;
                return 1;
            }
        }
        return 0;

    }

    private static int byteToInteger(Byte b) {
        int ret = b;
        if (ret < 0) {
            ret = ~b;
            ret = ret + 1;
            ret = ret ^ 255;
            ret += 1;
        }
        return ret;
    }

    private static String makeEightDigitString(String b) {
        String ret = "";
        int i;
        int len = b.length();
        for (i = 0; i < (8 - len); i++)
            ret += "0";
        ret += b;
        return ret;
    }

    private static void readBinary(String zip, String unz) {
        File f1 = null, f2 = null;
        int ok, bt;
        Byte b;
        int j, i;
        largeString = "";
        f1 = new File(zip);
        f2 = new File(unz);
        try {
            FileOutputStream file_output = new FileOutputStream(f2);
            DataOutputStream data_out = new DataOutputStream(file_output);
            FileInputStream file_input = new FileInputStream(f1);
            DataInputStream data_in = new DataInputStream(file_input);
            try {
                numberOfFrequency = data_in.readInt();
                System.out.println(numberOfFrequency);
                for (i = 0; i < numberOfFrequency; i++) {
                    b = data_in.readByte();
                    j = data_in.readInt();

                    // System.out.println(ss[byteToInteger(b)]);
                }
                extraBits = data_in.readInt();
                System.out.println(extraBits);

            } catch (EOFException eof) {
                System.out.println("End of File");
            }

            while (true) {
                try {
                    b = data_in.readByte();
                    bt = byteToInteger(b);
                    largeString += makeEightDigitString(intToBinary[bt]);

                    // System.out.println(largeString);

                    while (true) {
                        ok = 1;
                        temp = "";
                        for (i = 0; i < largeString.length() - extraBits; i++) {
                            temp += largeString.charAt(i);
                            // System.out.println(temp);
                            if (got() == 1) {
                                data_out.write(putIt);
                                ok = 0;
                                String s = "";
                                for (j = temp.length(); j < largeString.length(); j++) {
                                    s += largeString.charAt(j);
                                }
                                largeString = s;
                                break;
                            }
                        }

                        if (ok == 1)
                            break;
                    }
                } catch (EOFException eof) {
                    System.out.println("End of File");
                    break;
                }
            }
            file_output.close();
            data_out.close();
            file_input.close();
            data_in.close();
        } catch (IOException e) {
            System.out.println("IO Exception =: " + e);
        }

        f1 = null;
        f2 = null;
    }


    public static void initialize(String arg1) {
        initializeZipping();
        readFrequency(arg1);
        createBinary();
        int n = arg1.length();
        String arg2 = arg1.substring(0, n - 5);
        readBinary(arg1, arg2);
        initializeZipping();
    }

}