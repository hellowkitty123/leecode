package com.learn.day06;

/*
给定两个可能有环也可能无环的单链表，头节点head1和head2。请实现一个函数，如果两个链表相交，请返回相交的 第一个节点。如果不相交，返回null
【要求】
如果两个链表长度之和为N，时间复杂度请达到O(N)，额外空间复杂度 请达到O(1)。
 */
public class FindFirstIntersectNode {
    public static class Node{
        int value;
        Node next;
        Node(int value){
            this.value =value;
        }
    }
    public static Node findFisrstInterSectNode(Node head1,Node head2){
        if (head1 == null || head2 == null){
            return null;
        }
        Node loop1 = getLoopNode(head1);
        Node loop2 = getLoopNode(head2);
        // 连个相交 有环就都有环
        if (loop1 != null && loop2 != null){
           return getBothLoopNode(head1,loop1,head2,loop2);
        }
        // 相交 无环就都无环
        if (loop1 == null && loop2 == null){
            return getNoLoopNode(head1,head2);
        }
        return null;
    }

    public static Node getBothLoopNode(Node head1,Node loop1,Node head2,Node loop2){
        if ( loop1 == loop2) {
             int n = 0;
             Node node = head1;
             Node node2 = head2;
             while (node != loop1){
                 n++;
                 node = node.next;
             }
             while (node2 != loop2){
                 n--;
                 node2 = node2.next;
             }
             node = n > 0 ? head1 : head2;
             node2 = n > 0 ? head2 : head1;
             n = Math.abs(n);
             while (n >0){
                 n--;
                 node = node.next;
             }

             while (node != node2){
                 node = node.next;
                 node2 = node2.next;
             }
             return node;

        }else{
            Node cur = loop1;
            while (loop2 != cur){
                cur = cur.next;
            }
            return cur;
        }
    }

    // 无环前提下，链表节点无环
    public static Node getNoLoopNode(Node head1 , Node head2){
        if (head1 == null || head2 == null){
            return null;
        }
        int n = 1;
        Node node = head1;
        Node node2 = head2;
        while (node.next != null){
            n ++;
            node = node.next;
        }

        while (node2.next !=null){
            n--;
            node2 = node2.next;
        }
        //说明 不相交
        if (node == node2){
            Node cur = n > 0 ? head1 : head2; //cur 长
            Node cur2= n > 0 ? head2 : head1;  //cur2 短
            n = Math.abs(n);
            while (n > 0){
                n--;
                cur = cur.next;
            }

            while (cur != cur2){
                cur = cur.next;
                cur2 = cur2.next;
            }
        }
        return null;
    }

    // 第一种解题思路，额外空间复杂度O(n) hastset
    // 第二种解题思路，快慢指针
    public static Node getLoopNode(Node head){
        if (head ==null || head.next == null || head.next.next == null){
            return null;
        }

        Node slow = head.next ;
        Node fast = head.next.next;

        // 找到快慢指针第一次碰撞的地方, 判断是否有环
        while (fast.next != null && fast.next.next != null){
            if (slow == fast){
                break;
            }
            slow = slow.next;
            fast = fast.next.next;
        }

        // 有环情况下，fast从head出发 一次一步，slow 从碰撞位置出发一次一步
        if (fast == slow){
            fast = head;
            while (slow != fast){
                fast = fast.next;
                slow = slow.next;
            }
            return slow;
        }

        return  null;
    }
    public static void main(String[] args) {
        // 1->2->3->4->5->6->7->null
        Node head1 = new Node(1);
        head1.next = new Node(2);
        head1.next.next = new Node(3);
        head1.next.next.next = new Node(4);
        head1.next.next.next.next = new Node(5);
        head1.next.next.next.next.next = new Node(6);
        head1.next.next.next.next.next.next = new Node(7);


        // 0->9->8->6->7->null
        Node head2 = new Node(0);
        head2.next = new Node(9);
        head2.next.next = new Node(8);
        head2.next.next.next = head1.next.next.next.next.next; // 8->6
//        System.out.println(getIntersectNode(head1, head2).value);

        // 1->2->3->4->5->6->7->4...
        head1 = new Node(1);
        head1.next = new Node(2);
        head1.next.next = new Node(3);
        head1.next.next.next = new Node(4);
        head1.next.next.next.next = new Node(5);
        head1.next.next.next.next.next = new Node(6);
        head1.next.next.next.next.next.next = new Node(7);
        head1.next.next.next.next.next.next = head1.next.next.next; // 7->4
        Node loopNode = getLoopNode(head1);
        System.out.println(loopNode== null ? -1:loopNode.value);
        // 0->9->8->2...
        head2 = new Node(0);
        head2.next = new Node(9);
        head2.next.next = new Node(8);
        head2.next.next.next = head1.next; // 8->2
        System.out.println(findFisrstInterSectNode(head1, head2).value);

        // 0->9->8->6->4->5->6..
        head2 = new Node(0);
        head2.next = new Node(9);
        head2.next.next = new Node(8);
        head2.next.next.next = head1.next.next.next.next.next; // 8->6
        System.out.println(findFisrstInterSectNode(head1, head2).value);
    }
}
