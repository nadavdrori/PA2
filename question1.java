
public class question1 {
    public static void main(String[] args) {
//        int[] counts = {5, 10, 15, 20};
        for (int i =5; i<=20; i+=5) {
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
            int intialValue = (int) (Math.log(m) / Math.log(2));
            for (int j = intialValue; j >= 1; j--) {
                int index =  m - (int)Math.pow(2, i) + 1;
                FibonacciHeap.HeapNode nodeToDecrease = node_pointers[index];
                heap.decreaseKey(nodeToDecrease,m+1);
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
