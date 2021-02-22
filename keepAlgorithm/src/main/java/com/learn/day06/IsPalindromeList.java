package com.learn.day06;

import com.learn.day04.Heap02;

import java.util.Stack;

// 单链表是否为回文
public class IsPalindromeList {
    public static class Node {
        String value ;
        Node next ;
        Node(String value){
            this.value = value;
        }
        public void println(){
            Node node = this;

            while (node !=null){
                System.out.println(node.value);
                node = node.next;
            }
        }
    }
    //通过栈（笔试）
    public static boolean isPalindromeList1(Node head){
        if (head == null || head.next == null){
            return true;
        }

        Stack<String> stack = new Stack<>();
        Node node = head;
        while (node != null){
            stack.push(node.value);
            node = node.next;
        }
        node = head;
        while (!stack.isEmpty()){
            if (!stack.pop().equals( node.value)){
                return false;
            }
            node = node.next;
        }

        return true;
    }
    //链表反转（面试用）
    public static boolean isPalindromeList2(Node head){
        if (head == null || head.next == null) {
            return true;
        }
        Node slow = head;
        Node fast = head;
        while (fast.next != null && fast.next.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }
        Node pre = slow;
        Node cur = slow.next;
        Node forword = cur.next;

        while (cur != null){
            cur.next = pre;
            pre = cur;
            cur = forword;
            forword = forword != null ? forword.next : null;
        }
        slow.next = null;
        // 记录 末尾的头节点
        cur = new Node("");
        cur.next =pre;

        Node endhead = cur;
        endhead.next = pre;
        Node head2 = endhead.next;

        boolean isPalind = true;
        while (head != null){
            System.out.println(" head = "+head.value + " head2 = " + head2.value);
            if (!head.value.equals(head2.value)){
                isPalind =false;
                break;
            }
            head = head.next;
            head2 =head2.next;
        }
        return isPalind;
    }

    public static void main(String[] args) {
        Node head = new Node("1");
        Node node = head;
        node.next = new Node("2");
        node = node.next;
        node.next = new Node("3");
        node = node.next;
        node.next = new Node("3");
        node = node.next;
        node.next = new Node("2");
        node = node.next;
        node.next = new Node("1");
        node = node.next;
        boolean isPal = isPalindromeList2(head);
        System.out.println(isPal);
    }
}
