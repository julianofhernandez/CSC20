public static int mystery6(int n, int k) {
    if (k == 0 || k == n) {
        return 1;
    } else if (k > n) {
        return 0;
    } else {
        return mystery6(n - 1, k - 1) + mystery6(n - 1, k);
    }
}

System.out.print(mystery6(7,1));
