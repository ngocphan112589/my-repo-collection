package com.example.myproject.collection;

import java.util.NoSuchElementException;

interface Deque<T>{
    void addFirst(T data);

    void addLast(T data);

    boolean removeFirst(T data);

    boolean removeLast(T data);

    T pop();

    void push(T data);

    void clear();
}

public class CLinkedList<T> implements Deque<T>, CIterable<T> {
    private Node head;
    private Node tail;
    private int size;

    public void setSize(int size) {
        this.size = size;
    }

    public int size() {
        return size;
    }

    protected boolean isEmpty(){
        return head == null && tail == null;
    }
    private Node<T> createNewNode(T data){
        if (data == null){
            throw new RuntimeException("Invalid data");
        }
        return new Node<>(data);
    }
    public boolean add(T data){
        Node<T> node = createNewNode(data);
        if (isEmpty()){
            this.head = node;
            this.tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size ++;
        return true;
    };

    public void add (int index, T data) {
        Node<T> node = createNewNode(data);
        if (isEmpty()) {
            head = node;
        } else {
            Node<T> current = head;
            int currentIndex = 0;
            while(current != null) {
                if (currentIndex == index-1) {
                    node.next = current.next;
                    current.next = node;
                    size++;
                    break;
                }
                current = current.next;
                currentIndex ++;
            }
        }
    }

    public boolean remove(){
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        head = head.next;
        size--;
        return true;
    }

    public T remove(int index){
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> current = head;

        if (index == 0) {
            head = head.next;
            size--;
            return current.data;
        }
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }
        Node<T> removedNode = current.next;
        current.next = removedNode.next;
        size--;
        return removedNode.data;
    }

    public boolean remove(T data){
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (head.data.equals(data)) {
            head = head.next;
            size--;
            return true;
        }
        Node<T> current = head;
        while (current.next != null) {
            if (current.next.data.equals(data)) {
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public T get(int index){
        if (isEmpty()) {
            throw new NoSuchElementException("Invalid");
        }
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    @Override
    public void addFirst(T data) {
        Node<T> node = createNewNode(data);
        if (isEmpty()) {
            head = node;
            tail = node;
        } else {
            node.next = head;
            head = node;
        }
        size++;
    }

    @Override
    public void addLast(T data) {
        add(data);
    }

    @Override
    public boolean removeFirst(T data) {
        return remove();
    }

    @Override
    public boolean removeLast(T data) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node current = head;
        Node lastMatch = null;
        while (current != null) {
            if (current.data.equals(data)) {
                lastMatch = current;
            }
            current = current.next;
        }
        if (lastMatch == null) {
            return false;
        }
        if (head == lastMatch) {
            head = head.next;
        } else {
            Node<T> previous = head;
            while (previous.next == lastMatch) {
                previous.next = lastMatch.next;
            }
            previous = previous.next;
        }
        size--;
        return true;
    }

    @Override
    public T pop() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        head = head.next;
        size--;
        return (T)head;
    }

    @Override
    public void push(T data) {
        addFirst(data);
    }

    @Override
    public void clear() {
        Node<T> currentNode = head;
        while (currentNode != null) {
            Node<T> nextNode = currentNode.getNext();
            currentNode.setNext(null);
            currentNode.setData(null);
            currentNode = nextNode;
        }
        head = tail = null;
    }

    @Override
    public CIterator<T> iterator() {
        return new CLinkedListIterator<>();
    }

    static class Node<T> {
        T data;
        Node<T> next;

        public Node(T data){
            this.data = data ;
            this.next = null;
        }

        public void setData(T data) {
            this.data = data;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }

        public T getData() {
            return data;
        }

        public Node<T> getNext() {
            return next;
        }
    }

    class CLinkedListIterator<T> implements CIterator<T> {

        private Node<T> runner;

        public CLinkedListIterator() {
            if (isEmpty()) {
                throw new RuntimeException("Empty Linked List");
            }
            runner = head;
        }

        @Override
        public boolean hasNext() {
            if (runner != null) {
                if (runner.next != null) {
                    return true;
                } else {
                    return runner == tail;
                }
            }
            return false;
        }

        @Override
        public T next() {
            T value = runner.data;
            runner = runner.next;
            return value;
        }

        @Override
        public void remove() {
            if (isEmpty()){
                throw new NoSuchElementException();
            }
            if (runner == head) {
                head = head.next;
            } else {
                Node<T> current = head;
                while (current.next != runner) {
                    current = current.next;
                }
                current.next = runner.next;
            }
            size--;
        }
    }
}