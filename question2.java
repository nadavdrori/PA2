public class question2 {
    public static void main(String[] args) {
        for (int i = 6; i <= 14; i = i + 2) {
            FibonacciHeap heap = new FibonacciHeap();
            // measure time
            long startTime = System.currentTimeMillis();;
            int m = (int) Math.pow(3, i) - 1;
            for (int j = 0; j <= m; j++) {
                heap.insert(j);
            }
//            System.out.println("Deleting " + 3 * (m / 4) + " nodes");
            for (int j = 1; j <= 3 * (m / 4); j++) {
                heap.deleteMin();
            }
            long endTime = System.currentTimeMillis();;
            System.out.println("Iteration " + i + ":" + m);
            System.out.println("That took " + (endTime - startTime)+ " milliseconds");
            System.out.println("Total links: " + FibonacciHeap.totalLinks());
            System.out.println("Total cuts: " + FibonacciHeap.totalCuts());
            System.out.println("Potential: " + heap.potential());
        }
    }
}
