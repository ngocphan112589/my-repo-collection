package com.example.myproject.collection;

/**
 * @des Customize a simple HashMap
 */

import java.util.ConcurrentModificationException;
import java.util.Objects;

public class CHashMap<K,V> implements CIterable<K> {

    // linked list numbers
    private CLinkedList<Entry<K,V>>[] table;

    private static final int DEFAULT_CAPACITY = 16;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int capacity;

    private float loadFactor;

    private int threshold;

    // size
    int size;

	 // Initialize
    public CHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public CHashMap(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    public CHashMap(int capacity, float loadFactor) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity value is negative and not valid");
        }
        if (loadFactor <= 0 || Double.isNaN(loadFactor) || Double.isInfinite(loadFactor)) {
            throw new IllegalArgumentException("Load factor value is not valid");
        }
        this.loadFactor = loadFactor;
        this.capacity = Integer.max(DEFAULT_CAPACITY, capacity);
        this.threshold = (int) (this.capacity * this.loadFactor);
        this.table = new CLinkedList[this.capacity];
    }


    /**
     * How to get the size
     * return
     */
    public int size() {
        return size;
    }

    /**
     * Map addition method
     *
     * @param key
     * param value
     */
    public void put(K key, V value) {

        Entry<K,V> entry = new Entry<>(key, value);
        // define place to put entry
        int index = Math.abs(key.hashCode()) % this.capacity;
        //get list element in this index
        CLinkedList<Entry<K,V>> list = table[index];

        if (list == null) {
            list = new CLinkedList<>();
            list.add(entry);
            table[index] = list;
        } else {
            list.add(entry);
        }
        size++;
    }

    /**
     * This private method is used internally to resize the current hashmap when
     * it reached to our specified scale quantity
     */
    private void handleResizingTable() {
        this.capacity *=2;
        this.threshold = (int) (this.capacity * this.loadFactor);
        CLinkedList<Entry<K,V>>[] newTable = new CLinkedList[this.capacity];

        for (int i = 0; i < this.table.length; i++) {
            CLinkedList<Entry<K,V>> entryCLinkedList = this.table[i];
            if (Objects.isNull(entryCLinkedList)) {
                continue;
            }
            CIterator<Entry<K,V>> iterator = entryCLinkedList.iterator();
            while (iterator.hasNext()) {
                Entry<K,V> entry = iterator.next();
                int index = Math.abs(entry.key.hashCode()) % this.capacity;
                CLinkedList<Entry<K,V>> entryLinkedList = newTable[index];
                if (Objects.isNull(entryLinkedList)) {
                    newTable[index] = new CLinkedList<>();
                }
                newTable[index].add(entry);
            }
            table[i].clear();
            table[i] = null;
        }
        this.table = newTable;
    }

    /**
     * Get the value of value by key
     * @param key
     * return
     */
    public V get(K key) {
        // 1 Get the corresponding linked list
        // key is placed on that specific linked list
        int index = Math.abs(key.hashCode()) % this.capacity;

        CLinkedList<Entry<K,V>> list = table[index];
        if (list != null) {
            for (int i = 0; i <= list.size(); i++) {
                Entry<K,V> entry = list.get(i);
                if (entry != null) {
                    if (entry.key.equals(key)) {
                        return entry.value;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Whether to include key
     * @param key
     * return
     */
    public boolean containsKey(K key) {
        // 1 Get the corresponding linked list
        // key is placed on that specific linked list
        int index = Math.abs(key.hashCode()) % this.capacity;
        // remove the linked list
        CLinkedList<Entry<K,V>> list = table[index];
        // 2 convenience list Find entry (key judgment find entry)
        if (list != null) {

            for (int i = 0; i < list.size(); i++) {
                // get the entry
                Entry<K,V> entry = list.get(i);
                if (entry != null) {
                    // judge is the entry I am looking for
                    if (entry.key.equals(key)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Whether to include key
     * @param key
     * return
     */
    public void remove(K key) {
        // 1 Get the corresponding linked list
        // key is placed on that specific linked list
        int index = Math.abs(key.hashCode()) % this.capacity;

        // remove the linked list
        CLinkedList<Entry<K,V>> list = table[index];
        // 2 convenience list Find entry (key judgment find entry)

        if (list != null) {

            for (int i = 0; i < list.size(); i++) {
                // get the entry
                Entry<K,V> entry = list.get(i);
                if (entry != null) {
                    // judge is the entry I am looking for
                    if (entry.key.equals(key)) {
                        list.remove(entry);
                        break;
                    }
                }
            }
        }
        size--;
    }

    /**
     * This method is the generating method which provide an iterator instance for the current hashmap
     *
     * @return Iterator<K>
     */
    @Override
    public CIterator<K> iterator() {

        final int elementCount = size();

        return new CIterator<K>() {
            int index = 0;

            CIterator<Entry<K,V>> bucketCIterator = table[0] == null ? null : table[0].iterator();

            @Override
            public boolean hasNext() {
                if (elementCount != size()) {
                    throw new ConcurrentModificationException("The table has been change it's state to empty while the client iterates elements !");
                }

                if (bucketCIterator == null || !bucketCIterator.hasNext()) {
                    while (++index < table.length) {
                        if (table[index] != null && !table[index].isEmpty()) {
                            bucketCIterator = table[index].iterator();
                            break;
                        }
                    }
                }
                return index < table.length;
            }

            @Override
            public K next() {
                return bucketCIterator.next().getKey();
            }
        };
    }


    static class Entry<K,V> {

        K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }
    }
}
