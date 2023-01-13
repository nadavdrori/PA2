/**
 * FibonacciHeap
 * <p>
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap {
    private HeapNode min;
    private HeapNode first;
    private int markedAmount = 0;
    private int size;
    private static int cuts = 0;

    public HeapNode getMin() {
        return min;
    }

    public HeapNode getFirst() {
        return first;
    }

    public int getSize() {
        return size;
    }

    private void setMark(HeapNode node, boolean isMarked) {
        node.mark = isMarked;
        if (isMarked == true) {
            this.markedAmount++;
        } else
            this.markedAmount--;
    }


    /**
     * public boolean isEmpty()
     * <p>
     * Returns true if and only if the heap is empty.
     */
    public boolean isEmpty() {
        return min == null;
    }

    /**
     * public HeapNode insert(int key)
     * <p>
     * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
     * The added key is assumed not to already belong to the heap.
     * <p>
     * Returns the newly created node.
     */
    public HeapNode insert(int key) {
        if (isEmpty()) {
            first = new HeapNode(key);
            min = first;
            size++;
            first.prev = first;
            first.next = first;
            return first;
        } else {
            HeapNode node = new HeapNode(key);
            node.next = first;
            node.prev = first.prev;
            first.prev.next = node;
            first.prev = node;
            if (node.key < min.key)
                min = node;
            size++;
            return node;
        }
    }

    public HeapNode insert(HeapNode node_to_insert) {
        if (isEmpty()) {
            first = node_to_insert;
            min = first;
            size++;
            return first;
        } else {
            HeapNode node = node_to_insert;
            node.next = first;
            node.prev = first.prev;
            first.prev.next = node;
            first.prev = node;
            if (node.key < min.key)
                min = node;
            size++;
            return node;
        }
    }

    /**
     * public void deleteMin()
     * <p>
     * Deletes the node containing the minimum key.
     */
    public void deleteMin() {
        if (isEmpty())
            return;
        this.min.prev.next = this.min.child;
        this.min.next.prev = this.min.child.next;
        this.min.child.prev = this.min.prev;
        this.min.child.prev.next = this.min.next;

        HeapNode node = min.next;
        while (node != min) {
            node.parent = null;
            node = node.next;
        }
        size--;

        consolidation();
    }

    private void consolidation() {
        if (isEmpty())
            return;
        int maxDegree = (int) (Math.log(size) / Math.log(2));
        HeapNode[] arr = new HeapNode[maxDegree + 1]; // TODO: Check if should add one
        HeapNode node = first;
        HeapNode next = node.next;
        while (node != first) {
            int rankOfCurrentNode = node.rank;
            while (arr[rankOfCurrentNode] != null) {
                HeapNode other = arr[rankOfCurrentNode];
                if (node.key > other.key) {
                    HeapNode temp = node;
                    node = other;
                    other = temp;
                }
                if (other == min)
                    min = node;

                // Connect node as parent of other
                other.next.prev = other.prev;
                other.prev.next = other.next;
                other.parent = node;
                if (node.child == null) {
                    node.child = other;
                    other.next = other;
                    other.prev = other;
                } else {
                    other.next = node.child;
                    other.prev = node.child.prev;
                    node.child.prev.next = other;
                    node.child.prev = other;
                }
                node.rank++;
                arr[rankOfCurrentNode] = null;
                rankOfCurrentNode++;
            }
            // Update node to the next bucket
            arr[rankOfCurrentNode] = node;
            node = next;
            next = node.next;
        }
        min = null;

        // Update the min and connect all the roots
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                if (min == null) {
                    min = arr[i];
                    first = min;
                } else {
                    // Add to the end of roots list
                    arr[i].next = first;
                    arr[i].prev = first.prev;
                    first.prev.next = arr[i];
                    first.prev = arr[i];
                    // Update min
                    if (arr[i].key < min.key)
                        min = arr[i];
                }
            }
        }
    }


    /**
     * public HeapNode findMin()
     * <p>
     * Returns the node of the heap whose key is minimal, or null if the heap is empty.
     */
    public HeapNode findMin() {
        if (this.isEmpty()) {
            return null;
        }
        return min;
    }

    /**
     * public void meld (FibonacciHeap heap2)
     * <p>
     * Melds heap2 with the current heap.
     */
    public void meld(FibonacciHeap heap2) {
        if (!heap2.isEmpty()) {
            this.markedAmount += heap2.markedAmount;
            this.size += heap2.size;
            if (heap2.min.key < this.min.key)
                this.min = heap2.min;
            HeapNode last_tree = this.first.prev;
            last_tree.next = heap2.first;
            this.first.prev = heap2.first.prev;
            heap2.first.prev.next = this.first;
            heap2.first.prev = last_tree;
        }
    }

    /**
     * public int size()
     * <p>
     * Returns the number of elements in the heap.
     */
    public int size() {
        return size;
    }

    /**
     * public int[] countersRep()
     * <p>
     * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
     * (Note: The size of of the array depends on the maximum order of a tree.)
     */
    public int[] countersRep() {
        int[] arr = new int[size];
        int maxRank = 0;
        HeapNode node = this.first;
        do {
            arr[node.rank]++;
            node = node.next;
            if (node.rank > maxRank) {
                maxRank = node.rank;
            }
        } while (node != this.first);

        return getArrInMaxRankSize(arr, maxRank);
    }

    private int[] getArrInMaxRankSize(int[] arr, int maxRank) {
        int[] returnedArr = new int[maxRank +1];
        for (int i = 0; i <= maxRank; i++) {
            returnedArr[i] = arr[i];
        }
        return returnedArr;
    }

    /**
     * public void delete(HeapNode x)
     * <p>
     * Deletes the node x from the heap.
     * It is assumed that x indeed belongs to the heap.
     */
    public void delete(HeapNode x) {
        decreaseKey(x, Integer.MIN_VALUE);
        deleteMin();
    }

    /**
     * public void decreaseKey(HeapNode x, int delta)
     * <p>
     * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
     * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
     */
    public void decreaseKey(HeapNode x, int delta) {
        int new_key = x.key - delta;
        x.key = new_key;
        HeapNode root = x.find_root();
        if ((x.key < x.parent.key) && (x.parent.getMarked() == false)) {
            remove_decreased_child(x);
            this.insert(x);
        } else if ((x.key < x.parent.key) && (x.parent.getMarked() == true)) {
            this.setMark(x, true);
            HeapNode first_not_marked = cascading_cut(x);
            this.setMark(first_not_marked, true);
        }
    }

    /**
     * private HeapNode cascading_cut(HeapNode xNode)
     * This method is used to cut the node x from its parent and then to cut the parent from its parent and so on.
     * The method returns the first node that is not marked.
     */
    private HeapNode cascading_cut(HeapNode xNode) {
        HeapNode parent;
        while (xNode.getMarked() == true) {
            parent = xNode.parent;
            remove_decreased_child(xNode);
            this.insert(xNode);
            xNode = parent;
        }
        return xNode;
    }

    /**
     * private void remove_decreased_child(HeapNode x)
     * This method is used to remove the node x from its parent.
     */
    private void remove_decreased_child(HeapNode x) {
        cuts++;
        this.setMark(x.getParent(), true);
        x.getParent().rank--;
        if (x.parent.child.key == x.key) {
            x.parent.child = x.next;
        }
        x.parent = null;
        x.next.prev = x.prev;
        x.prev.next = x.next;
        this.setMark(x, false);
    }

    /**
     * public int nonMarked()
     * <p>
     * This function returns the current number of non-marked items in the heap
     */
    public int nonMarked() {
        return this.markedAmount;
    }

    /**
     * public int potential()
     * <p>
     * This function returns the current potential of the heap, which is:
     * Potential = #trees + 2*#marked
     * <p>
     * In words: The potential equals to the number of trees in the heap
     * plus twice the number of marked nodes in the heap.
     */
    public int potential() {
        return -234; // should be replaced by student code
    }

    /**
     * public static int totalLinks()
     * <p>
     * This static function returns the total number of link operations made during the
     * run-time of the program. A link operation is the operation which gets as input two
     * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
     * tree which has larger value in its root under the other tree.
     */
    public static int totalLinks() {
        return -345; // should be replaced by student code
    }

    /**
     * public static int totalCuts()
     * <p>
     * This static function returns the total number of cut operations made during the
     * run-time of the program. A cut operation is the operation which disconnects a subtree
     * from its parent (during decreaseKey/delete methods).
     */
    public static int totalCuts() {
        return cuts;
    }

    /**
     * public static int[] kMin(FibonacciHeap H, int k)
     * <p>
     * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
     * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
     * <p>
     * ###CRITICAL### : you are NOT allowed to change H.
     */
    public static int[] kMin(FibonacciHeap H, int k) {
        int[] arr = new int[k];
        FibonacciHeap help_heap = new FibonacciHeap();
        help_heap.insert(H.min);
        for (int i = 0; i < k; i++) {
            arr[i] = help_heap.min.key;
            HeapNode x = help_heap.min;
            insert_min_children(help_heap, x);
            help_heap.delete(help_heap.min);
        }
        return arr;
    }

    private static void insert_min_children(FibonacciHeap help_heap, HeapNode x) {
        if (x.child != null) {
            x = x.child;
            do {
                help_heap.insert(x);
                x = x.next;
            }
            while (x.key != x.parent.child.key);
        }
    }

    /**
     * public class HeapNode
     * <p>
     * If you wish to implement classes other than FibonacciHeap
     * (for example HeapNode), do it in this file, not in another file.
     */
    public static class HeapNode {
        public int key;
        public int rank;
        public boolean mark;
        public HeapNode child;
        public HeapNode next;
        public HeapNode prev;
        public HeapNode parent;


        public HeapNode(int key) {
            this.key = key;
            this.mark = false;
            this.rank = 0;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public boolean getMarked() {
            return mark;
        }

        public HeapNode getChild() {
            return child;
        }

        public HeapNode getNext() {
            return next;
        }

        public HeapNode getPrev() {
            return prev;
        }

        public HeapNode getParent() {
            return parent;
        }

        public void setParent(HeapNode parent) {
            this.parent = parent;
        }

        public int getKey() {
            return this.key;
        }

        public HeapNode find_root() {
            HeapNode x = this;
            while (x.parent != null) {
                x = x.parent;
            }
            return x;
        }
    }
}
