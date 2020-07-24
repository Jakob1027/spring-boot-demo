package com.jakob.springbootdemo.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedLRUCache extends LinkedHashMap<Integer, Integer> {

    private final int capacity;

    public LinkedLRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    public int get(int key) {
        return super.getOrDefault(key, -1);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
        return size() > capacity;
    }
}
