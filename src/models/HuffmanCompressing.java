package models;

import java.io.*;
import java.util.PriorityQueue;

public class HuffmanCompressing {


    private static PriorityQueue<TREE> priorityQueue = new PriorityQueue<TREE>();
    private static int[] frequency = new int[300];
    private static String[] intToString = new String[300];
    private static byte bt;
    private static int numberOfChar; // number of different characters

    // main tree class
    static class TREE implements Comparable<TREE> {
        TREE leftChild;
        TREE rightChild;
        String deb;
        int Bite;
        int frequency;

        public int compareTo(TREE tree) {
            return Integer.compare(this.frequency, tree.frequency);
        }
    }

    private static TREE root;

    private static void calculateFrequency(String fileName) {
        File file;
        Byte bt;

        file = new File(fileName);

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            while (true) {
                try {

                    bt = dataInputStream.readByte();
                    frequency[byteToBinary(bt)]++;
                } catch (EOFException eof) {
                    System.out.println("End of File");
                    break;
                }
            }
            fileInputStream.close();
            dataInputStream.close();
        } catch (IOException e) {
            System.out.println("IO Exception =: " + e);
        }
        file = null;
    }

    private static int byteToBinary(Byte bt) {
        int temp = bt;

        if (temp < 0) {
            temp = ~bt;
            temp = temp + 1;
            temp = temp ^ 255;
            temp += 1;
        }
        return temp;
    }

    private static void initialize() {

        numberOfChar = 0;
        if (root != null)
            byteToBinary(root);
        for (int i = 0; i < 300; i++)
            frequency[i] = 0;
        for (int i = 0; i < 300; i++)
            intToString[i] = "";
        priorityQueue.clear();
    }

    private static void byteToBinary(TREE tree) {

        if (tree.leftChild == null && tree.rightChild == null) {
            return;
        }
        if (tree.leftChild != null)
            byteToBinary(tree.leftChild);
        if (tree.rightChild != null)
            byteToBinary(tree.rightChild);
    }


    private static void depthFirstSearch(TREE tree, String string) {
        tree.deb = string;
        if ((tree.leftChild == null) && (tree.rightChild == null)) {
            intToString[tree.Bite] = string;
            return;
        }
        if (tree.leftChild != null)
            depthFirstSearch(tree.leftChild, string + "0");
        if (tree.rightChild != null)
            depthFirstSearch(tree.rightChild, string + "1");
    }


    private static void createNode() {

        priorityQueue.clear();

        for (int i = 0; i < 300; i++) {
            if (frequency[i] != 0) {
                TREE Temp = new TREE();
                Temp.Bite = i;
                Temp.frequency = frequency[i];
                Temp.leftChild = null;
                Temp.rightChild = null;
                priorityQueue.add(Temp);
                numberOfChar++;
            }

        }

        TREE temp1, temp2;

        if (numberOfChar == 0) {
            return;
        } else if (numberOfChar == 1) {
            for (int i = 0; i < 300; i++)
                if (frequency[i] != 0) {
                    intToString[i] = "0";
                    break;
                }
            return;
        }

        while (priorityQueue.size() != 1) {
            TREE Temp = new TREE();
            temp1 = priorityQueue.poll();
            temp2 = priorityQueue.poll();
            Temp.leftChild = temp1;
            Temp.rightChild = temp2;
            if (temp1 != null && temp2 != null){
                Temp.frequency = temp1.frequency + temp2.frequency;
            }

            priorityQueue.add(Temp);
        }

        root = priorityQueue.poll();
    }

    // encrypting
    public static void encrypt(String fileName) {
        File file = null;
        file = new File(fileName);

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            while (true) {
                try {

                    bt = dataInputStream.readByte();
                    frequency[bt]++;
                } catch (EOFException eof) {
                    System.out.println("End of File");
                    break;
                }
            }
            fileInputStream.close();
            dataInputStream.close();

        } catch (IOException e) {
            System.out.println("IO Exception =: " + e);
        }
        file = null;
    }

    private static void zipping(String fileName, String fname1) {
        File fileInput;
        File fileOutput;

        byte btt;

        fileInput = new File(fileName);
        fileOutput = new File(fname1);

        try {
            FileInputStream fileInputStream = new FileInputStream(fileInput);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            FileOutputStream fileOutputStream = new FileOutputStream(fileOutput);
            DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);

            dataOutputStream.writeInt(numberOfChar);

            for (int i = 0; i < 256; i++) {
                if (frequency[i] != 0) {
                    btt = (byte) i;
                    dataOutputStream.write(btt);
                    dataOutputStream.writeInt(frequency[i]);
                }
            }

            long texbits;

            texbits = fileInput.length() % 8;
            texbits = (8 - texbits) % 8;
            int extraBits = (int) texbits;

            dataOutputStream.writeInt(extraBits);
            while (true) {
                try {
                    bt = 0;
                    byte ch;
                    for (extraBits = 0; extraBits < 8; extraBits++) {
                        ch = dataInputStream.readByte();
                        bt *= 2;
                        if (ch == '1')
                            bt++;
                    }
                    dataOutputStream.write(bt);

                } catch (EOFException eof) {
                    int x;
                    if (extraBits != 0) {
                        for (x = extraBits; x < 8; x++) {
                            bt *= 2;
                        }
                        dataOutputStream.write(bt);
                    }

                    extraBits = (int) texbits;
                    System.out.println("extrabits: " + extraBits);
                    System.out.println("End of File");
                    break;
                }
            }
            dataInputStream.close();
            dataOutputStream.close();
            fileInputStream.close();
            fileOutputStream.close();
            System.out.println("output file's size: " + fileOutput.length());

        } catch (IOException e) {
            System.out.println("IO exception = " + e);
        }
        fileInput.delete();
        fileInput = null;
        fileOutput = null;
    }


    public static void initialize(String arg) {
        initialize();
        calculateFrequency(arg);
        createNode();

        if (numberOfChar > 1) {
            depthFirstSearch(root, "");
        }

        zipping(arg, arg + ".hufz");
        initialize();
    }

}
