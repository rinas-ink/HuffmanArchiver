// Inkina Ekaterina 2021v
// 12/04/2021
// Haffman algorithm dynamic anarchiver

import java.io.*;

public class DynamicUnarchiver {
    public static void main(String[] args) throws IOException {
        String fileInput = "output.txt";
        long t=System.currentTimeMillis();
        unarchive(fileInput);
        System.out.println("Time:  " + (System.currentTimeMillis()-t));
    }

    public static String toFormat(int x) {
        StringBuilder tmp = new StringBuilder(Integer.toBinaryString(x));
        while (tmp.length() < 8)
            tmp.insert(0, '0');
        return tmp.toString();

    }

    public static void unarchive(String fileInput) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileInput));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("anarch_" + fileInput));
        int[] freq = new int[257];
        freq[256] = 1;
        HuffmanTree.Vertex[] tree = HuffmanTree.prefixTreeBuilder(freq);
        int x = bis.read();
        StringBuilder bits = new StringBuilder();
        int m = tree.length - 1, b = 0;
        while (b == 0 && x != -1) {
            bits.append(toFormat(x));
            while (bits.length() > 0) {
                if (tree[m].symbol == -1) {
                    if (bits.charAt(0) == '1')
                        m = tree[m].right;
                    else
                        m = tree[m].left;
                    bits = new StringBuilder(bits.substring(1));
                } else {
                    if (tree[m].symbol == 256) {
                        if (bits.length() < 9) {
                            x = bis.read();
                            bits.append(toFormat(x));
                            if (bits.length() < 9) {
                                x = bis.read();
                                bits.append(toFormat(x));
                            }
                        }
                        if (bits.charAt(0) == '0') {
                            int newChar = Integer.parseInt(bits.substring(1, 9), 2);
                            bits = new StringBuilder(bits.substring(9));
                            freq[newChar]++;
                            bos.write(newChar);
                        } else {
                            b = 1;
                            break;
                        }
                    } else {
                        bos.write(tree[m].symbol);
                        freq[tree[m].symbol]++;
                    }
                    tree = HuffmanTree.prefixTreeBuilder(freq);
                    m = tree.length - 1;
                }
            }
            x = bis.read();
        }
        bis.close();
        bos.close();
    }
}


