package queue;


public class ArrayQueueADTTest {
    public static void main(String[] args) {
        ArrayQueueADT queue = new ArrayQueueADT();
        for (int i = 0; i < 5; i++) {
            ArrayQueueADT.enqueue(queue, i);
        }
        while (!ArrayQueueADT.isEmpty(queue)) {
            System.out.println(ArrayQueueADT.size(queue) + " " + ArrayQueueADT.element(queue) + " " + ArrayQueueADT.count(queue, ArrayQueueADT.element(queue)) + " " + ArrayQueueADT.dequeue(queue));
            if (ArrayQueueADT.size(queue) == 2) {
                break;
            }
        }
        ArrayQueueADT.enqueue(queue, "test");
        System.out.println(ArrayQueueADT.size(queue));
        ArrayQueueADT.clear(queue);
        System.out.println(ArrayQueueADT.isEmpty(queue));
    }
}
