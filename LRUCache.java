import java.util.HashMap;

public class LRUCache {
    class Node {
        int key;
        int val;
        Node pre;
        Node post;

        public Node(int key, int val) {
            this.key = key;
            this.val = val;
        }
    }

    private int capacity;
    private HashMap<Integer, Node> map;
    private Node head, tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        map = new HashMap<>();
        head = new Node(0, 0);
        tail = new Node(0, 0);
        head.post = tail;
        tail.pre = head;
    }

    private void removeNode(Node node) {
        Node prev = node.pre;
        Node next = node.post;
        prev.post = next;
        next.pre = prev;
    }

    private void addNode(Node node) {
        Node dummyHeadNext = head.post;
        head.post = node;
        node.pre = head;
        node.post = dummyHeadNext;
        dummyHeadNext.pre = node;
    }

    private void moveToHead(Node node) {
        removeNode(node);
        addNode(node);
    }    

    public int get(int key) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            moveToHead(node);
            return node.val;
        }
        return -1;
    }

    public void put(int key, int val) {
       if (map.containsKey(key)) {
           Node node = map.get(key);
           node.val = val;
           moveToHead(node);
       } else {
           Node node = new Node(key, val);
           map.put(key, node);
           addNode(node);
           if (map.size() > capacity) {
               Node removed = tail.pre;
               removeNode((removed));
               map.remove(removed.key);
           }
       }
    } 

    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2); // 2 capacity

        cache.put(1, 10); // Cache is {1->10}
        cache.put(2, 20); // Cache is {1->10, 2->20}

        System.out.println("Value for key 1: " + cache.get(1)); // Returns 10, Cache is {2->20, 1->10}
        System.out.println("Value for key 2: " + cache.get(2)); // Returns 20, Cache is {1->10, 2->20}

        cache.put(2, 30); // Cache is {1->10, 2->30}
        System.out.println("Updated value for key 2: " + cache.get(2)); // Returns 30

        cache.put(3, 30); // Evicts key 1, Cache is {2->30, 3->30}
        System.out.println("Value for key 1 (should be -1): " + cache.get(1)); // Returns not found

        cache.put(4, 40); // Evicts key 2, Cache is {3->30, 4->40}
        System.out.println("Value for key 2 (should be -1): " + cache.get(2)); // Returns not found
        System.out.println("Value for key 3: " + cache.get(3)); // Returns 30
        System.out.println("Value for key 4: " + cache.get(4)); // Returns 40
    }

}