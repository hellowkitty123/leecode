package com.learn.day01;

public class ReverseList {

    public static class Node{
        Integer value;
        Node next;
        Node(int date){
            value = date;
        }
    }
    //链表翻转
    public static Node ReverseList(Node head){
        Node pre = null;
        Node cur = head;
        Node next = null;
        while (cur != null){
            next = cur.next; // 抓住 cur 的next
            cur.next = pre;  // 调整 cur 的next指针
            pre = cur;       // 移动 pre 和 cur
            cur = next;
        }
        //cur == null 退出，所以需要把pre返回
        return pre;
    }

    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5,6,7};
        int index = 1;
        Node node = new Node(arr[0]);
        Node head = node;
        while (index < arr.length) {
            node.next = new Node(arr[index++]);
            node = node.next;
        }
        Node newHead = ReverseList(head);
        printList(newHead);

    }
    public static void printList(Node head){
        Node node = head;
        System.out.println(node.value);
        while (node != null){
            node = node.next;
            if (node !=null && node.value != null){
                System.out.println(node.value);
            }
        }
    }
}


