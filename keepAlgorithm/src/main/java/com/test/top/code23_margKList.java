package com.test.top;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class code23_margKList {
      public static class ListNode {
          int val;
          ListNode next;
          ListNode() {}
          ListNode(int val) { this.val = val; }
          ListNode(int val, ListNode next) { this.val = val; this.next = next; }
      }
    public  static ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> heap = new PriorityQueue<>(new MyCompare());
        for(ListNode node : lists){
            while(node != null){
                heap.add(node);
                node = node.next;
            }
        }
        ListNode head = heap.poll();
        ListNode cur = head;
        while(!heap.isEmpty()){
            cur.next = heap.poll();
            cur = cur.next;
        }
        return head;
    }

    public  static class MyCompare implements Comparator<ListNode> {
        @Override
        public int compare(ListNode o1,ListNode o2){
            return o1.val - o2.val;
        }
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(-1);
        head.next = new ListNode(-2);

        ListNode[] list = new ListNode[]{head,null};
        ListNode a = mergeKLists(list);
        System.out.println(a.val);
        System.out.println(a.next.val);
        System.out.println(a.next.next.val);
    }
}
