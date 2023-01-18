
public class question1 {
    public static void main(String[] args) {
        int[] counts = {5, 10, 15, 20};
        for (int i : counts) {
            FibonacciHeap heap = new FibonacciHeap();
            int m = (int)(Math.pow(2, i));
            FibonacciHeap.HeapNode[] node_pointers = new FibonacciHeap.HeapNode[m];
            // measure time
            long startTime = System.currentTimeMillis();
            for (int j = m - 1; j >= (- 1); j--) {
                heap.insert(j);
                if (j>=0)
                    node_pointers[j] = heap.getFirst();
            }
            heap.deleteMin();
            for (int j = (int)(Math.log(m) / Math.log(2)); j >= 1; j--) {
                heap.decreaseKey(node_pointers[(int)((m-Math.pow(2, i))+1)],m+1);
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Iteration " + i + ":" + m);
            System.out.println("Run time " + (endTime - startTime) + " milliseconds");
            System.out.println("Total links: " + FibonacciHeap.totalLinks());
            System.out.println("Total cuts: " + FibonacciHeap.totalCuts());
            System.out.println("Potential: " + heap.potential());
            System.out.println("\n");
        }
    }
}
