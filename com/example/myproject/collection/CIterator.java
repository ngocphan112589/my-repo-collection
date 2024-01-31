package com.example.myproject.collection;

public interface CIterator<T> {
    boolean hasNext();

    T next();

    default void remove() {
        throw new RuntimeException("Not Impl default behavior for remove operation");
    }
}
