package com.jakob.zookeeper;

import java.util.ArrayList;
import java.util.List;

public class ChildrenCache {
    private List<String> children;

    public ChildrenCache(List<String> children) {
        this.children = children;
    }

    public ChildrenCache() {
        this.children = null;
    }

    public List<String> getList() {
        return children;
    }

    public List<String> addAndSet(List<String> newChildren) {
        List<String> diff = null;
        if (children == null) {
            diff = new ArrayList<>(newChildren);
        } else {
            for (String child : newChildren) {
                if (!children.contains(child)) {
                    if (diff == null) diff = new ArrayList<>();
                    diff.add(child);
                }
            }
        }
        return diff;
    }

    public List<String> removeAndSet(List<String> newChildren) {
        List<String> diff = null;
        if (children != null) {
            for (String child : children) {
                if (!newChildren.contains(child)) {
                    if (diff == null) diff = new ArrayList<>();
                    diff.add(child);
                }
            }
        }
        children = newChildren;
        return diff;
    }
}
