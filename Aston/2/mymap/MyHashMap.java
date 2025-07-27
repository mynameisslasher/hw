package mymap;

public class MyHashMap<K, V> {
    private static class Node<K, V>{
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    private int hash(K key) {
        int h = (key == null) ? 0 : key.hashCode();
        return h ^ (h >>> 16);
    }

    public void put(K key, V value) {
        int hash = hash(key);
        int index = (table.length - 1) & hash;

        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash && current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(hash, key, value, table[index]);
        table[index] = newNode;
        size++;

        if (size > threshold) {
            resize();
        }
    }

    public V get(K key) {
        int hash = hash(key);
        int index = (table.length - 1) & hash;

        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash && current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public V remove(K key) {
        int hash = hash(key);
        int index = (table.length - 1) & hash;

        Node<K, V> current = table[index];
        Node<K, V> prev = null;

        while (current != null) {
            if (current.hash == hash && current.key.equals(key)) {
                if (prev == null) {
                    table[index] = current.next;
                }
                else {
                    prev.next = current.next;
                }
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int oldCapacity = table.length;
        int newCapacity = oldCapacity * 2;

        Node<K, V>[] oldTable = table;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];

        table = newTable;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        size = 0;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    public int getSize() {
        return size;
    }

    public int getThreshold() {
        return threshold;
    }

    public int getCapacity() {
        return table.length;
    }
}
