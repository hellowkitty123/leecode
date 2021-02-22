package com.learn.day06;

import java.util.HashMap;

public class CopyListWithRandom {

    public static class Node{
        int value;
        Node next;
        Node rand;
        Node(int value){
            this.value = value;
        }
    }

    public static Node copyListWithRandom(Node head){
        if (head == null){
            return null;
        }
        HashMap<Node,Node> map = new HashMap<>();
        Node node = head;

        while (node !=null ){
            map.put(node,new Node(node.value));
            node =node.next;
        }

        node = head;

        Node copyNode;
        while (node != null){
            copyNode = map.get(node);
            copyNode.next = map.get(node.next);
            copyNode.rand = map.get(node.rand);
            node = node.next;
        }
        return map.get(head);
    }
    // 1-1`-2-2`
    public static Node copyListWithRandom2(Node head) {
        if (head == null){
            return null;
        }
        Node node = head;
        Node next;
        while (node != null){
            next = node.next;
            node.next= new Node(node.value+10);
            node.next.next = next;
            node = node.next.next;
        }
        node = head;
        Node newHead = head.next;
        Node newNode;

        while (node != null){
            newNode = node.next;
            newNode.rand = node.rand == null ? null : node.rand.next ;
            node = node.next.next;

        }

        node = head;
        while (node != null){

            newNode = node.next;
            node.next = newNode.next;
            newNode.next = node.next == null ? null : node.next.next  ; // 边界判断比较麻烦
            System.out.println(node.value);
            System.out.println(newNode.value);
            node = node.next;
        }

        return newHead;
    }

    public static void main(String[] args) {
        Node head = null;
        Node res1 = null;
        Node res2 = null;
        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(4);
        head.next.next.next.next = new Node(5);
        head.next.next.next.next.next = new Node(6);

        head.rand = head.next.next.next.next.next; // 1 -> 6
        head.next.rand = head.next.next.next.next.next; // 2 -> 6
        head.next.next.rand = head.next.next.next.next; // 3 -> 5
        head.next.next.next.rand = head.next.next; // 4 -> 3
        head.next.next.next.next.rand = null; // 5 -> null
        head.next.next.next.next.next.rand = head.next.next.next; // 6 -> 4
        printRandLinkedList(head);
        Node newHead = copyListWithRandom2(head);
        printRandLinkedList(newHead);
        printRandLinkedList(head);


    }


    public static void printRandLinkedList(Node head) {
        Node cur = head;
        System.out.print("order: ");
        while (cur != null) {
            System.out.print(cur.value + " ");
            cur = cur.next;
        }
        System.out.println();
        cur = head;
        System.out.print("rand:  ");
        while (cur != null) {
            System.out.print(cur.rand == null ? "- " : cur.rand.value + " ");
            cur = cur.next;
        }
        System.out.println();
    }

}
