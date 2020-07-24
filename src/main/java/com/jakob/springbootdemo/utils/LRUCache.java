package com.jakob.springbootdemo.utils;

import java.util.HashMap;
import java.util.Map;

public class LRUCache {
    private static class Node {
        final int key;
        int val;
        Node next;
        Node prev;

        public Node(int key, int value) {
            this.key = key;
            this.val = value;
            this.next = null;
            this.prev = null;
        }

        public Node() {
            this(0, 0);
        }
    }

    private final Map<Integer, Node> map;
    private int size;
    private final int capacity;
    private final Node head;
    private final Node tail;

    public LRUCache(int capacity) {
        map = new HashMap<>(capacity);
        this.capacity = capacity;
        this.size = 0;
        this.head = new Node();
        this.tail = new Node();
        head.next = tail;
        tail.prev = head;
    }


    public int get(int key) {
        Node node = map.get(key);
        if (node == null) return -1;
        update(node);
        return node.val;
    }

    private void update(Node node) {
        remove(node);
        add(node);
    }

    private void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void add(Node node) {
        Node temp = head.next;
        head.next = node;
        node.prev = head;
        node.next = temp;
        temp.prev = node;
    }

    public void put(int key, int value) {
        Node node = map.get(key);
        if (node == null) {
            Node newNode = new Node(key, value);
            add(newNode);
            map.put(key, newNode);
            size++;
        } else {
            node.val = value;
            update(node);
        }

        if (size > capacity) {
            Node toRemove = tail.prev;
            remove(toRemove);
            map.remove(toRemove.key);
            size--;
        }
    }
}
