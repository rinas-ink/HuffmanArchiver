// Inkina Ekaterina 2021v
// 18/03/2021
// Haffman algorithm archiver

import java.io.*;
import java.util.ArrayList;

public class Archiver {
    public static void main(String[] args) throws IOException {
        // Archiving randomly generated file
        int time = 0;
        int p = 1;
        float c = 0;
        String fileInput = "input.txt", fileOutput = "output.txt";
        makeRandomFile(p, fileInput);
        long t = System.currentTimeMillis();
        ArrayList<Integer> x = readSeqFile(fileInput);
        int[] freq = frequency(x);
        HuffmanTree.Vertex[] tree = HuffmanTree.prefixTreeBuilder(freq);
        String[] codes = getCodes(tree);
        c += archive(x, codes, freq, fileOutput);
        time += (int) (System.currentTimeMillis() - t);
        // Statistics
        System.out.println(p + " " + time + " " + c);
    }

    public static void makeRandomFile(int Mb, String filename) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename));
        for (int i = 0; i < Mb * 1048576; i++) {//
            bos.write((int) Math.round(Math.random() * 255));

        }
        bos.close();
    }

    public static ArrayList<Integer> readSeqFile(String filename) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filename));
        ArrayList<Integer> s = new ArrayList<>(110000000);
        int x = bis.read();
        while (x != -1) {
            s.add(x);
            x = bis.read();
        }
        bis.close();
        return s;
    }

    public static int[] frequency(ArrayList<Integer> x) {
        int[] freq = new int[256];
        for (int i : x)
            freq[i]++;
        return freq;
    }

    public static String[] getCodes(HuffmanTree.Vertex[] tree) {
        int n = (tree.length + 1) / 2;
        String[] treeCodes = new String[tree.length];
        treeCodes[tree.length - 1] = "";
        for (int i = tree.length - 1; i >= n; i--) {
            treeCodes[tree[i].left] = treeCodes[i] + "0";
            treeCodes[tree[i].right] = treeCodes[i] + "1";
        }
        String[] codes = new String[256];
        for (int i = 0; i < n; i++)
            if (tree[i].symbol != -1)
                codes[tree[i].symbol] = treeCodes[i];

        return codes;
    }

    public static float archive(ArrayList<Integer> x, String[] codes, int[] freq, String filename) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename));
        int before = 0, after = 0;
        int n = -1;
        for (int i = 0; i < 256; i++)
            if (freq[i] > 0)
                n++;
        bos.write(n);
        for (int i = 0; i < 256; i++)
            if (freq[i] > 0) {
                before += (8 * freq[i]);
                bos.write(i);
                int b1 = freq[i] >>> 24, b2 = (freq[i] << 8) >>> 24,
                        b3 = (freq[i] << 16) >>> 24, b4 = (freq[i] << 24) >>> 24;
                bos.write(b1);
                bos.write(b2);
                bos.write(b3);
                bos.write(b4);
                after += 40;
            }
        int i = 0;
        String res = "";
        while (i < x.size()) {
            res += codes[x.get(i)];
            while (res.length() >= 8) {
                bos.write(Integer.parseInt(res.substring(0, 8), 2));
                res = res.substring(8);
                after += 8;
            }
            i++;
        }
        int tmp = res.length();
        String zero = "";
        if (tmp != 0) {
            while (tmp < 8) {
                tmp++;
                zero += '0';
            }
            after += 8;
            bos.write(Integer.parseInt((res + zero), 2));
        }
        bos.close();
        return (float) before / after;
    }


}