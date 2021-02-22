package com.learn.day06;

import java.util.Arrays;

/*
将单向链表按某值划分成左边小、中间相等、右边大的形式

1）把链表放入数组里，在数组上做partition（笔试用）

2）分成小、中、大三部分，再把各个部分之间串起来（面试用）
 */
public class SmallerEqualBigger {

    public static class Node{
        int value;
        Node next;
        Node(int value){
            this.value =value;
        }
    }

    public static void partition(Node[] arr,int L ,int R , int pivot){
        if (L > R){
            return;
        }
        int left = L-1;
        int right = R;
        int move = L ;
        while (move <= right){

            if (arr[move].value < pivot){
                swap(arr,move++,++left);

            }else if(arr[move].value > pivot){
                swap(arr,move,right--);
            }else{
                move++;
            }
            printNodeArr(arr);
        }
    }

    private static void swap(Node[]arr,int i,int j){
        Node temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    //笔试用
    public static Node smallEqualBigger1(Node head,int pivot){
        Node node = head;
        int count = 0;
        while (node != null){
            count ++;
            node = node.next;
        }
        node = head;
        Node[] arr = new Node[count];
        count = 0;
        while (node !=null){
            arr[count++] = node;
            node = node.next;
        }

        partition(arr,0,arr.length-1,pivot);

        head = arr[0];
        for (int i=0;i<arr.length-1;i++){
            arr[i].next = arr[i+1];
            System.out.print(" "+ arr[i].value);
        }
        arr[arr.length-1].next = null;
        // 换头，重新返回头部
        return head;
    }
    //面试用
    public static Node smallEqualBigger2(Node head,int pivot){
        if (head == null ){
            return head;
        }
        Node AS = null;
        Node AE = null;
        Node BS = null;
        Node BE = null;
        Node CS = null;
        Node CE = null;
        Node move = head;

        while (move != null){
            if (move.value < pivot){
                if (AS == null){
                    AS = move;
                    AE = move;
                }else {
                    AE.next = move;
                    AE = move;
                }
                move = move.next;
                AE.next = null;
            }else if(move.value == pivot){
                if (BS == null){
                    BS = move;
                    BE = move;
                }else {
                    BE.next = move;
                    BE = move;
                }
                move = move.next;
                BE.next = null;
            }else{
                if (CS == null){
                    CS = move;
                    CE = move;
                }else {
                    CE.next = move;
                    CE = move;

                }
                move = move.next;
                CE.next = null;
            }

        }

        Node[] row = linkTwoLinkedList(AS,AE,BS,BE);
        printLinkedList(row[0]);
        Node start = row[0];
        Node end =  row[1];
        row = linkTwoLinkedList(start,end,CS,CE);
        row[1].next = null;
        return row[0];
    }

    private static Node[] linkTwoLinkedList(Node head1,Node end1,Node head2,Node end2){
        if (head1 == null && head2 == null){
            return new Node[]{null,null};
        }
        if (head1 !=null && head2 ==null){
            return new Node[]{head1,end1};
        }
        if (head1 == null){
            return new Node[]{head2,end2};
        }

        end1.next = head2;
        return new Node[]{head1,end2};
    }
    public static void printNodeArr(Node[] arr){
        int[] myarr = new int[arr.length];
        for (int i=0;i<arr.length;i++){
            myarr[i] = arr[i].value;
        }
        System.out.println(Arrays.toString(myarr));
    }

    public static void printLinkedList(Node node) {
        System.out.print("Linked List: ");
        while (node != null) {
            System.out.print(node.value + " ");
            node = node.next;
        }
        System.out.println();
    }
    public static void main(String[] args) {
        Node head1 = new Node(7);
        head1.next = new Node(9);
        head1.next.next = new Node(1);
        head1.next.next.next = new Node(8);
        head1.next.next.next.next = new Node(5);
        head1.next.next.next.next.next = new Node(2);
        head1.next.next.next.next.next.next = new Node(5);
//        printLinkedList(head1);
//        Node head = smallEqualBigger1(head1,7);
        Node head = smallEqualBigger2(head1,2);
        printLinkedList(head);
    }
}
