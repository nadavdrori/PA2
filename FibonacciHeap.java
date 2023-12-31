/**
 * FibonacciHeap
 * <p>
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap {
    private HeapNode min;
    private HeapNode first;
    public int markedAmount = 0;
    private int size;
    private int treesAmount;
    public static int cuts = 0;

    public static int linksAmount = 0;


    private final float GOLDEN_RATIO = (float) 1.62;

    public HeapNode getMin() {
        return min;
    }

    public HeapNode getFirst() {
        return first;
    }

    /**
     * public void setMark(HeapNode x, boolean isMarked)
     * <p>
     * Sets the mark of the node to be isMarked.
     * Complexity: O(1)
     */
    private void setMark(HeapNode node, boolean isMarked) {
        if (node == null) {
            return;
        }

        if (isMarked && !node.getMarked()) {
            this.markedAmount++;
        } else if (!isMarked && node.getMarked()) {
            this.markedAmount--;
        }
        node.mark = isMarked;
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
        size++;
        treesAmount++;
        if (isEmpty()) {
            first = new HeapNode(key);
            min = first;
            first.prev = first;
            first.next = first;
            return first;
        } else {
            HeapNode node = new HeapNode(key);
            node.next = first;
            node.prev = first.prev;
            first.prev.next = node;
            first.prev = node;
            first = node;
            if (node.key < min.key)
                min = node;
            return node;
        }
    }

    /**
     * public HeapNode insert(HeapNode node_to_insert) {
     * <p>
     * Inserts the given node to the heap.
     * complexity: O(1)
     */
    public HeapNode insert(HeapNode node_to_insert) {
        size++;
        treesAmount++;
        if (isEmpty()) {
            first = node_to_insert;
            min = first;
            return first;
        } else {
            HeapNode node = node_to_insert;
            node.next = first;
            node.prev = first.prev;
            first.prev.next = node;
            first.prev = node;
            first = node;
            if (node.key < min.key)
                min = node;
            return node;
        }
    }

    /**
     * public void deleteMin()
     * <p>
     * Deletes the node containing the minimum key.
     * <p>
     * Complexity: O(n)
     */
    public void deleteMin() {
        if (isEmpty())
            return;
        size--;
        if (min == first && min.next == min) {
            if (min.child == null) {
                first = null;
                min = null;
                treesAmount--;
            } else {
                handleSingleTreeWithSons();
            }
            return;
        }
        removeMin();

        HeapNode node = first.next;
        while (node != first) {
            node.parent = null;
            node = node.next;
        }

        consolidation();
    }

    /**
     * private void removeMin()
     * <p>
     * Removes the minimum node from the heap.
     * Complexity: O(1)
     */
    private void removeMin() {
        if (first == min) {
            first = min.next;
        }
        if (min.child == null) {
            min.next.prev = min.prev;
            min.prev.next = min.next;
            treesAmount--;
        } else {
            HeapNode minChildPrev = min.child.prev;
            min.prev.next = min.child;
            min.child.prev = min.prev;
            min.next.prev = minChildPrev;
            minChildPrev.next = min.next;
        }
        min = first;
    }

    /**
     * private void handleSingleTreeWithSons()
     * <p>
     * Handles the case where the heap has only one tree with sons.
     * Complexity: O(n)
     */
    private void handleSingleTreeWithSons() {
        first = min.child;
        min = first;
        HeapNode curr = first;
        treesAmount--;
        do {
            if (curr.key < min.key)
                min = curr;
            curr.parent = null;
            curr = curr.next;
            treesAmount++;
        } while (curr != first);
    }

    /**
     * private void consolidation()
     * <p>
     * Consolidates the heap.
     * Complexity: O(n)
     */
    private void consolidation() {
        int rootsAmount = getsRootsAmount();
        if (isEmpty())
            return;
        int maxDegree = (int) (Math.ceil(Math.log(size) / Math.log(GOLDEN_RATIO)));
        HeapNode[] arr = new HeapNode[maxDegree + 1];
        HeapNode node = first;
        for (int i = 0; i < rootsAmount; i++) {
            HeapNode next = node.next;
            int rankOfCurrentNode = node.rank;
            while (arr[rankOfCurrentNode] != null) {
                this.linksAmount++;
                HeapNode son = arr[rankOfCurrentNode];
                if (node.key > son.key) {
                    HeapNode temp = node;
                    node = son;
                    son = temp;
                }

                // Connect node as parent of son
                son.next.prev = son.prev;
                son.prev.next = son.next;
                if (node.rank == 0) {
                    son.next = son;
                    son.prev = son;
                    node.child = son;
                    son.parent = node;
                } else {
                    son.next = node.child;
                    son.prev = node.child.prev;
                    node.child.prev.next = son;
                    node.child.prev = son;
                    node.child = son;
                    son.parent = node;
                }
                node.rank++;
                setMark(node, false);
                arr[rankOfCurrentNode] = null;
                rankOfCurrentNode++;
            }

            // Update node to the next bucket
            arr[rankOfCurrentNode] = node;
            node = next;
        }
        min = first;

        int treesCount = 0;
        boolean isFirst = true;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                treesCount++;
                if (isFirst) {
                    first = arr[i];
                    first.next = first;
                    first.prev = first;
                    isFirst = false;
                } else {
                    HeapNode curr = first;
                    while (curr.next != first) {
                        curr = curr.next;
                    }
                    curr.next = arr[i];
                    arr[i].prev = curr;
                    arr[i].next = first;
                    first.prev = arr[i];
                }
                if (arr[i].key < min.key) {
                    min = arr[i];
                }
            }
        }
        this.treesAmount = treesCount;
    }

    /**
     * private int getsRootsAmount()
     * <p>
     * Returns the amount of roots in the heap.
     * Complexity: O(n)
     */
    private int getsRootsAmount() {
        int rootsCount = 1;
        HeapNode curr = first.next;
        while (curr != first) {
            curr = curr.next;
            rootsCount++;
        }
        return rootsCount;
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
     * <p>
     * Complexity: O(1)
     */
    public void meld(FibonacciHeap heap2) {
        if (this.isEmpty()) {
            this.first = heap2.first;
            this.min = heap2.min;
            this.size = heap2.size;
            this.treesAmount = heap2.treesAmount;
            return;
        }
        if (!heap2.isEmpty()) {
            this.markedAmount += heap2.markedAmount;
            this.size += heap2.size;
            this.treesAmount += heap2.treesAmount;
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
     * <p>
     * Complexity: O(1)
     */
    public int size() {
        return size;
    }

    /**
     * public int[] countersRep()
     * <p>
     * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
     * (Note: The size of of the array depends on the maximum order of a tree.)
     * <p>
     * Complexity: O(n)
     */
    public int[] countersRep() {
        if (isEmpty()) {
            return new int[0];
        }
        int[] arr = new int[size + 1];
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

    /**
     * private int[] getArrInMaxRankSize(int[] arr, int maxRank)
     * <p>
     * Returns an array of counters in the size of the max rank.
     * Complexity: O(log(n))
     */
    private int[] getArrInMaxRankSize(int[] arr, int maxRank) {
        int[] returnedArr = new int[maxRank + 1];
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
     * complexity: O(n)
     */
    public void decreaseKey(HeapNode x, int delta) {
        int new_key = x.key - delta;
        x.key = new_key;
        if (x.parent == null) {
            if (new_key < min.key) {
                min = x;
            }
        } else {
            if ((x.key < x.parent.key) && (x.parent.getMarked() == false)) {
                remove_decreased_child(x);
                this.insert(x);
            } else if ((x.key < x.parent.key) && (x.parent.getMarked() == true)) {
                this.setMark(x, true);
                HeapNode first_not_marked = cascading_cut(x);
                if (first_not_marked.parent != null)
                    this.setMark(first_not_marked, true);
            }
        }
    }

    /**
     * private HeapNode cascading_cut(HeapNode xNode)
     * This method is used to cut the node x from its parent and then to cut the parent from its parent and so on.
     * The method returns the first node that is not marked.
     * Complexity: O(n)
     */
    private HeapNode cascading_cut(HeapNode xNode) {
        HeapNode parent;
        while ((xNode.getMarked() == true) || (xNode.getParent() != null)) {
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
     * Complexity: O(1)
     */
    private void remove_decreased_child(HeapNode x) {
        cuts++;
        this.size--;
        if (x.getParent().getParent() != null)
            this.setMark(x.getParent(), true);
        x.getParent().rank--;
        if (x.parent.child.key == x.key) {
            if (x.next.key == x.key)
                x.parent.child = null;
            else
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
     * <p>
     * Complexity: O(1)
     */
    public int nonMarked() {
        return (this.size - this.markedAmount);
    }

    /**
     * public int potential()
     * <p>
     * This function returns the current potential of the heap, which is:
     * Potential = #trees + 2*#marked
     * <p>
     * In words: The potential equals to the number of trees in the heap
     * plus twice the number of marked nodes in the heap.
     *  complexity: O(1)
     */
    public int potential() {
        return this.treesAmount + 2 * this.markedAmount;
    }

    /**
     * public static int totalLinks()
     * <p>
     * This static function returns the total number of link operations made during the
     * run-time of the program. A link operation is the operation which gets as input two
     * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
     * tree which has larger value in its root under the other tree.
     * complexity: O(1)
     */
    public static int totalLinks() {
        return linksAmount;
    }

    /**
     * public static int totalCuts()
     * <p>
     * This static function returns the total number of cut operations made during the
     * run-time of the program. A cut operation is the operation which disconnects a subtree
     * from its parent (during decreaseKey/delete methods).
     * complexity: O(1)
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
     * complexity: O(k*deg(H))
     */
    public static int[] kMin(FibonacciHeap H, int k) {
        int[] arr = new int[k];
        FibonacciHeap help_heap = new FibonacciHeap();
        help_heap.insert(H.min.key);
        help_heap.first.child_pointer = H.min.child;
        for (int i = 0; i < k; i++) {
            arr[i] = help_heap.min.key;
            HeapNode min_child = help_heap.min.child_pointer;
            insert_min_children(help_heap, min_child);
            help_heap.delete(help_heap.min);
        }
        return arr;
    }

    /**
     * private static void insert_min_children(FibonacciHeap help_heap, HeapNode min_child)
     * This method is used to insert the children of the min node to the help heap.
     * The method runs in O(deg(H)).
     */
    private static void insert_min_children(FibonacciHeap help_heap, HeapNode min_child) {
        if (min_child != null) {
            for (int i = 0; i < min_child.parent.rank; i++) {
                help_heap.insert(min_child.key);
                help_heap.first.child_pointer = min_child.child;
                min_child = min_child.next;
            }
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
        public HeapNode child_pointer = null;


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
    }
}
