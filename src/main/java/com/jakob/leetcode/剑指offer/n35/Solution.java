package com.jakob.leetcode.剑指offer.n35;

import com.jakob.leetcode.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请实现 copyRandomList 函数，复制一个复杂链表。在复杂链表中，每个节点除了有一个 next 指针指向下一个节点，还有一个 random 指针指向链表中的任意节点或者 null。
 *
 *  
 *
 * 示例 1：
 *
 *
 *
 * 输入：head = [[7,null],[13,0],[11,4],[10,2],[1,0]]
 * 输出：[[7,null],[13,0],[11,4],[10,2],[1,0]]
 * 示例 2：
 *
 *
 *
 * 输入：head = [[1,1],[2,1]]
 * 输出：[[1,1],[2,1]]
 * 示例 3：
 *
 *
 *
 * 输入：head = [[3,null],[3,0],[3,null]]
 * 输出：[[3,null],[3,0],[3,null]]
 * 示例 4：
 *
 * 输入：head = []
 * 输出：[]
 * 解释：给定的链表为空（空指针），因此返回 null。
 *  
 *
 * 提示：
 *
 * -10000 <= Node.val <= 10000
 * Node.random 为空（null）或指向链表中的节点。
 * 节点数目不超过 1000 。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/fu-za-lian-biao-de-fu-zhi-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    public Node copyRandomList(Node head) {
        if (head == null) return null;
        List<Node> list = new ArrayList<>();
        List<Node> newList = new ArrayList<>();
        Map<Node, Integer> map = new HashMap<>();
        Node temp = head;
        while (temp != null) {
            map.put(temp, list.size());
            list.add(temp);
            temp = temp.next;
        }
        for (Node n : list) {
            newList.add(new Node(n.val));
        }
        for (int i = 0; i < newList.size(); i++) {
            Node node = newList.get(i);
            if (i != newList.size() - 1) node.next = newList.get(i + 1);
            Node random = list.get(i).random;
            if (random != null) node.random = newList.get(map.get(random));
        }
        return newList.get(0);
    }
}
