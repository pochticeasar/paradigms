package search;

/* pre: for each i a[i] is int, x is int
   for each pair i < j a[i] <= a[j],
   exist k in [0...a.length - 1] a[k] <= x && k = min(j_1, ..., j_m), any a[j_i] <= x
 */
// post: k` == k

public class BinarySearchMissing {
    public static void main(String[] args) {
        int n = args.length - 1;
        int[] a = new int[n];
        int x = Integer.parseInt(args[0]);
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(args[i + 1]);
        }
        int k = binSearchRecursive(x, a, -1, n);
        assert binSearchRecursive(x, a, -1, n) == binSearchIterative(x, a);
        System.out.println(k);
    }

    /*pre: for any i, j in [0...a.length - 1] i < j a[i] <= a[j]
           exist k in [0...a.length - 1] a[k] <= x && k = min(j_1, ..., j_m), any a[j_i] <= x
     */
    //post: r == k
    private static int binSearchIterative(int x, int[] a) {
        int l = -1, r = a.length;
        // k > l && k <= r
        // r' - l' < r - l
        // Inv: r' > l'
        while (r - l > 1) {
            // pre:  r' > l' && l' < (l + r)/2 <= r' && a[l'] < a[(l + r)/2] <= a[r']
            int mid = (l + r) / 2;
            // post: r' > l' && l' < mid <= r' && a[l'] < a[mid] <= a[r']
            // pre:  r' > l' && (a[mid] >= x or a[mid] < x)
            if (a[mid] >= x) {
                // r' > l' && a[mid] >= x && a[mid] >= a[k]
                r = mid;
                // r' > l' && a[mid] >= x && a[r'] >= a[k]
            } else {
                // r' > l' && a[mid] >= x && a[mid] < a[k]
                l = mid;
                // Inv && a[mid] >= x && a[l'] < a[k]
            }
        }

        if (r >= a.length || a[r] != x) {
            // k >= a.size() or a[k] != x -> x does not exist
            return -(r + 1);
        } else {
            // x exist and a.get(k) == x
            return r;
        }
    }

    /*pre: for any i, j in [0...a.length - 1] i < j a[i] <= a[j]
           exist k in [l`...r`] a[k] <= x && k = min(j_1, ..., j_m), any a[j_i] <= x
     */
    //post: r == k
    private static int binSearchRecursive(int x, int[] a, int l, int r) {
        if (r - l <= 1) {
            if (r >= a.length || a[r] != x) {
                // k >= a.size() or a[k] != x -> x does not exist
                return -(r + 1);
            } else {
                // x exist and a[k] == x
                return r;
            }
        }
        // pre:  r' > l' && l' < (l + r)/2 <= r' && a[l'] < a[(l + r)/2] <= a[r']
        int mid = (l + r) / 2;
        // post: r' > l' && l' < mid <= r' && a[l'] < a[mid] <= a[r']

        if (a[mid] >= x) {
            // a[mid] >= x &&  r' - l' < r - l
            return binSearchRecursive(x, a, l, mid);
        } else {
            //a[mid] < x &&  r' - l' < r - l
            return binSearchRecursive(x, a, mid, r);
        }
    }
}
