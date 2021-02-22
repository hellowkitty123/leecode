package com.learn.day01;

public class DeleteGivenNum {

    public static class Node {
        int value;
        Node next;
        Node(int date){
            value = date;
        }
    }
    // 按照值删除链表中的元素节点
    public static Node DeleteGivenNum(Node head,int num){

        if (head == null){
            return null;
        }
        // java 对象不被任何其他对象引用会自动释放
        Node next = head.next;
        while (next!=null){
            if (next.value != num){
                break;
            }
            next = next.next;
        }
        head = next;
        Node cur = next;
        next = cur.next;

        while (next !=null ){
            if (next.value != num){
                cur.next = next;
                cur = next;
            }
            next = next.next;
        }


        return head;
    }


    public static void main(String[] args) {
        int[] arr = {3,3,1,2,3,3,4,5,6,7};
        int index = 1;
        Node node = new Node(arr[0]);
        Node head = node;
        while (index < arr.length) {
            node.next = new Node(arr[index++]);
            node = node.next;
        }
        Node newHead = DeleteGivenNum(head,3);
        System.out.println(newHead.value);
        printList(newHead);

    }
    public static void printList(Node head){
        Node node = head;
        System.out.println(node.value);
        while (node != null){
            node = node.next;
            if (node !=null ){
                System.out.println(node.value);
            }
        }
    }
}
