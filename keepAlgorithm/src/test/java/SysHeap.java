import java.util.PriorityQueue;

public class SysHeap {
    public static void main(String[] args) {
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        heap.add(3);
        heap.add(4);
        heap.add(2);
        heap.add(7);
        heap.add(1);
        while (!heap.isEmpty()){
            System.out.println(heap.poll());
        }

    }
}
