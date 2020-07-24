package com.jakob.springbootdemo;

import com.jakob.springbootdemo.utils.LRUCache;
import com.jakob.springbootdemo.utils.LinkedLRUCache;
import org.junit.Assert;
import org.junit.Test;

public class TestLRUCache {

    @Test
    public void testCache() {
        LRUCache cache = new LRUCache(2);
        int res;
        cache.put(1, 1);
        cache.put(2, 2);
        res = cache.get(1);       // 返回  1
        Assert.assertEquals(1, res);
        cache.put(3, 3);    // 该操作会使得关键字 2 作废
        res = cache.get(2);       // 返回 -1 (未找到)
        Assert.assertEquals(-1, res);
        cache.put(4, 4);    // 该操作会使得关键字 1 作废
        res = cache.get(1);       // 返回 -1 (未找到)
        Assert.assertEquals(-1, res);
        res = cache.get(3);       // 返回  3
        Assert.assertEquals(3, res);
        res = cache.get(4);       // 返回  4
        Assert.assertEquals(4, res);
    }

    @Test
    public void testLinkedCache() {
        LinkedLRUCache cache = new LinkedLRUCache(2);
        int res;
        cache.put(1, 1);
        cache.put(2, 2);
        res = cache.get(1);       // 返回  1
        Assert.assertEquals(1, res);
        cache.put(3, 3);    // 该操作会使得关键字 2 作废
        res = cache.get(2);       // 返回 -1 (未找到)
        Assert.assertEquals(-1, res);
        cache.put(4, 4);    // 该操作会使得关键字 1 作废
        res = cache.get(1);       // 返回 -1 (未找到)
        Assert.assertEquals(-1, res);
        res = cache.get(3);       // 返回  3
        Assert.assertEquals(3, res);
        res = cache.get(4);       // 返回  4
        Assert.assertEquals(4, res);
    }
}
