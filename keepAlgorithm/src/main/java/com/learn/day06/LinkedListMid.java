package com.learn.day06;

public class LinkedListMid {
    public static class  Node{
        int value;
        Node next;
        Node(int value){
            this.value = value;
        }
    }

    //快慢指针，扣边界
    //输入链表头节点，奇数长度返回中点，偶数长度返回上中点
    public static Node linkedListUpMid(Node head){
        if(head == null || head.next == null || head.next.next == null){
            return head;
        }

        Node slow = head.next;
        Node fast = head.next.next;

        while (fast.next != null && fast.next.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }
    //输入链表头节点，奇数长度返回中点，偶数长度返回下中点
    public static Node linkedListDownMid(Node head){
         if (head == null || head.next == null){
             return head;
         }

         Node slow = head.next;
         Node fast = head.next;

         while (fast.next !=null && fast.next.next != null){
             slow = slow.next;
             fast = fast.next.next;
         }
         return slow;
    }
    //输入链表头节点，奇数长度返回中点前一个，偶数长度返回上中点前一个
    public static Node linkedListUpUpMid(Node head){
        if (head == null || head.next == null || head.next.next == null){
            return null;
        }
        Node slow = head;
        Node fast = head.next.next;
        while (fast.next != null && fast.next.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }
    //输入链表头节点，奇数长度返回中点前一个，偶数长度返回下中点前一个
    public static Node linkedListDonwDownMid(Node head){
        if (head == null || head.next ==null || head.next.next == null){
            return null;
        }

        Node slow = head.next.next;
        Node fast = head.next;
        while (fast.next != null && fast.next.next !=null){
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }


    public static void main(String[] args) {
        Node head = new Node(1);
        Node node = head;
        node.next = new Node(2);
        node = node.next;
        node.next = new Node(3);
        node = node.next;
        node.next = new Node(4);
        node = node.next;
        node.next = new Node(5);
        node = node.next;
//        node.next = new Node(6);
//        node = node.next;
//        node.next = new Node(7);

//        Node midNode = linkedListUpMid(head);
//        Node midNode = linkedListDownMid(head);
//        Node midNode = linkedListUpUpMid(head);
        Node midNode = linkedListDonwDownMid(head);
        if (midNode == null){
            System.out.println("mid is null");
            return;
        }
        System.out.println(midNode.value);
    }
}
