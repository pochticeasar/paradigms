package queue;


public class ArrayQueueModuleTest {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            ArrayQueueModule.enqueue(i);
        }
        while (! ArrayQueueModule.isEmpty()) {
            System.out.println( ArrayQueueModule.size() + " " +  ArrayQueueModule.element() + " " +  ArrayQueueModule.count( ArrayQueueModule.element()) + " " +  ArrayQueueModule.dequeue());
            if ( ArrayQueueModule.size() == 2) {
                break;
            }
        }
        ArrayQueueModule.enqueue("test");
        System.out.println( ArrayQueueModule.size());
        ArrayQueueModule.clear();
        System.out.println( ArrayQueueModule.isEmpty());
    }
}
