// Common code for archiver und unarchiver
public class HuffmanTree {
    public static Vertex[] prefixTreeBuilder(int[] freq) {
        int n = 0, m = 0;
        for (int k : freq) {
            if (k > 0)
                n++;
        }
        if (n == 1) n = 2;
        Vertex[] tree = new Vertex[2 * n - 1];
        for (int i = 0; i < freq.length; i++) {
            if (freq[i] != 0) {
                tree[m] = new Vertex();
                tree[m].val = freq[i];
                tree[m].symbol = i;
                m++;
            }
        }
        if (m < n) {
            tree[m] = new Vertex();
            m++;
        }
        int min1, min2;
        for (int i = 0; i < n - 1; i++) {
            min1 = -1;
            for (int j = 0; j < m; j++)
                if ((tree[j].isUsed == 0) && ((min1 == -1) || (tree[j].val < tree[min1].val)))
                    min1 = j;
            tree[min1].isUsed = 1;
            min2 = -1;
            for (int j = 0; j < m; j++)
                if ((tree[j].isUsed == 0) && ((min2 == -1) || (tree[j].val < tree[min2].val)))
                    min2 = j;
            tree[min2].isUsed = 1;
            tree[m] = new Vertex();
            tree[m].val = tree[min1].val + tree[min2].val;
            tree[m].left = min1;
            tree[m].right = min2;
            m++;
        }
        return tree;
    }
    static class Vertex {
        int val;
        int left;
        int right;
        int isUsed;
        int symbol;

        public Vertex() {
            val = 0;
            left = -1;
            right = -1;
            isUsed = 0;
            symbol = -1;
        }
    }
}
