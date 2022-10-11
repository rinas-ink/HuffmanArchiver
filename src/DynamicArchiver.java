// Inkina Ekaterina 2021v
// 12/04/2021
// Haffman algorithm dynamic archiver

import java.io.*;

public class DynamicArchiver {
    public static void main(String[] args) throws IOException {
        String fileInput = "input.txt", fileOutput = "output.txt";
        makeRandomFile(fileInput, 1000);
        long t=System.currentTimeMillis();
        float c = archive(fileInput, fileOutput);
        System.out.println("Compressed in " + c);
        System.out.println("Time:  " + (System.currentTimeMillis()-t));
    }

    public static void makeRandomFile(String filename, int Kb) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename));
        for (int i = 0; i < Kb*1024; i++) {
            bos.write((int) Math.round(Math.random() * 255));
        }
        bos.close();
    }

    public static float archive(String fileInput, String fileOutput) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileInput));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileOutput));
        long before = 0, after = 0;
        int[] freq = new int[257];
        freq[256] = 1;
        String[] codes = new String[0];
        String res = "";
        HuffmanTree.Vertex[] tree = HuffmanTree.prefixTreeBuilder(freq);
        int x = bis.read();
        while (x != -1) {
            before += 8;
            while (res.length() >= 8) {
                bos.write(Integer.parseInt(res.substring(0, 8), 2));
                res = res.substring(8);
                after += 8;
            }
            codes = getCodes(tree);
            if (freq[x] != 0) {
                res = res + codes[x];
            } else {
                res = res + codes[256] + '0';
                String xStr = Integer.toBinaryString(x);
                while (xStr.length() < 8)
                    xStr = '0' + xStr;
                res = res + xStr;
            }
            freq[x]++;
            tree = HuffmanTree.prefixTreeBuilder(freq);
            x = bis.read();
        }
        codes=getCodes(tree);
        res = res + codes[256] + '1';
        String tmp = codes[256];
        while (tmp.length() < 8)
            tmp = '0' + tmp;
        res = res + tmp;

        while (res.length() >= 8) {
            bos.write(Integer.parseInt(res.substring(0, 8), 2));
            res = res.substring(8);
            after += 8;
        }
        if (res.length() > 0) {
            while (res.length() < 8)
                res = res + '0';
            bos.write(Integer.parseInt(res, 2));
            after += 8;
        }
        bis.close();
        bos.close();
        return (float) before / after;
    }

    public static String[] getCodes(HuffmanTree.Vertex[] tree) {
        int n = (tree.length + 1) / 2;
        String[] treeCodes = new String[tree.length];
        treeCodes[tree.length - 1] = "";
        for (int i = tree.length - 1; i >= n; i--) {
            treeCodes[tree[i].left] = treeCodes[i] + "0";
            treeCodes[tree[i].right] = treeCodes[i] + "1";
        }
        String[] codes = new String[257];
        for (int i = 0; i < n; i++)
            if (tree[i].symbol != -1)
                codes[tree[i].symbol] = treeCodes[i];

        return codes;
    }

}
