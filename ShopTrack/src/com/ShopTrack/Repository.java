package com.ShopTrack;

import java.util.ArrayList;

public class Repository<T> {
    private final ArrayList<T> items;

    public Repository (ArrayList<T> items) {
        this.items = items;
    }

    public void add(T item) {
        items.add(item);
    }

    public boolean remove(T item) {
        return items.remove(item);
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public ArrayList<T> getAll() {
        return items;
    }
}
