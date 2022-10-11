// Haffman algorithm unarchiver

import java.io.*;

public class Unarchiver {
    public static void main(String[] args) throws IOException {
        int j = 0;
        String fileOutput = "output.txt";
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileOutput));
        long t = System.currentTimeMillis();
        int[] freq = readFreqTable(bis);
        int l = 0, n = 0;
        for (int i = 0; i < 256; i++) {
            l += freq[i];
        }
        HuffmanTree.Vertex[] tree = HuffmanTree.prefixTreeBuilder(freq);
        unarchive(("output.txt"), bis, l, tree);
        t = (System.currentTimeMillis() - t);
        bis.close();
        System.out.println(" " + t);
    }

    public static int[] readFreqTable(BufferedInputStream bis) throws IOException {
        int n = bis.read() + 1;
        int[] freq = new int[256];
        for (int i = 0; i < n; i++) {
            int c = bis.read();
            freq[c] = (bis.read() << 24) + (bis.read() << 16) + (bis.read() << 8) + bis.read();
        }
        return freq;
    }

    public static void unarchive(String filename, BufferedInputStream bis, int l, HuffmanTree.Vertex[] tree) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename + "_unarch.txt"));
        int j = 0, x = 0, m;
        long i = 0;
        while (j < l) {
            m = tree.length - 1;
            while (tree[m].symbol == -1) {
                if (i % 8 == 0) {
                    x = bis.read();
                }
                if ((x << (24 + i % 8) >>> 31) == 1)
                    m = tree[m].right;
                else
                    m = tree[m].left;
                i++;
            }
            j++;
            bos.write(tree[m].symbol);
        }
        bos.close();
        bis.close();
    }

}
