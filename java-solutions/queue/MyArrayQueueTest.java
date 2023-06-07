package queue;


public class MyArrayQueueTest {
    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue();
        for (int i = 0; i < 5; i++) {
            queue.enqueue(i);
        }
        while (!queue.isEmpty()) {
            System.out.println(queue.size() + " " + queue.element() + " " + queue.count(queue.element()) + " " + queue.dequeue());
            if (queue.size() == 2) {
                break;
            }
        }
        queue.enqueue("test");
        System.out.println(queue.size());
        queue.clear();
        System.out.println(queue.isEmpty());
    }
}
