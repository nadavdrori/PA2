public class question2 {
    public static void main(String[] args) {
        for (int i = 6; i <= 14; i = i + 2) {
            FibonacciHeap heap = new FibonacciHeap();
            // measure time
            long startTime = System.nanoTime();
            int m = (int) Math.pow(3, i) - 1;
            for (int j = 0; j <= m; j++) {
                heap.insert(j);
            }
            for (int j = 1; j <= 3 * (m / 4); j++) {
                heap.deleteMin();
            }
            long endTime = System.nanoTime();
            System.out.println("Iteration " + i + ":" + m);
            System.out.println("That took " + (endTime - startTime) / 1000 + " milliseconds");
            System.out.println("Total links: " + FibonacciHeap.totalLinks());
            System.out.println("Total cuts: " + FibonacciHeap.totalCuts());
            System.out.println("Potential: " + heap.potential());
        }
    }
}
