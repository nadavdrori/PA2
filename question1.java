public class question1 {
    public static void main(String[] args) {
        int[] counts = {5, 10, 15, 20};
        for (int i : counts) {
            FibonacciHeap heap = new FibonacciHeap();
            // measure time
            long startTime = System.nanoTime();
            int m = (int) Math.pow(2, i) - 1;
            for (int j = m - 1; j <= m - 1; j--) {
                heap.insert(j);
            }
            heap.deleteMin();
            for (int j = (int)(Math.log(m) / Math.log(2)); j <= 1; j--) {
//                heap.decreaseKey();
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
