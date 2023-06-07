package queue;

//Model: elements[0]...elements[n - 1]
//Invariant: for i = 0...n-1: elements[i] != null && Immutable(elements[i]) && elements != null
public class ArrayQueueADT {
    private Object[] elements = new Object[2];
    private int size = 0;
    private int tail = 0, head = 0;

    //pre: k != null
    //post: elements[n - 1] = k && size' = size + 1
    public static void enqueue(ArrayQueueADT queue, Object k) {
        assert k != null;
        ensure(queue);
        queue.elements[queue.tail] = k;
        queue.size++;
        queue.tail = (queue.tail + 1) % queue.elements.length;
    }

    //pre: size > 0
    //post: R = elements[0] && immutable(size') && && size' = size - 1
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.size > 0;
        Object res = element(queue);
        queue.elements[queue.head] = null;
        queue.size--;
        queue.head = (queue.head + 1) % queue.elements.length;
        return res;
    }

    //pre: true
    //post: R = size && size' = size
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    //pre: true
    //post: R = (size == 0) && size' = size
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }


    //pre: size > 0
    //post: R = elements[0] && Immutable(size) && size' == size
    public static Object element(ArrayQueueADT queue) {
        assert queue.size > 0;
        return queue.elements[queue.head];
    }

    private static void ensure(ArrayQueueADT queue) {
        if (queue.size + 1 >= queue.elements.length || queue.head == queue.tail) {
            Object[] copy;
            if (queue.head > queue.tail) {
                copy = new Object[(queue.size + 1) * 2];
                for (int i = queue.head; i < queue.elements.length; i++) {
                    copy[i - queue.head] = queue.elements[i];
                }
                int sz = queue.size + 1 - queue.head;
                for (int i = 0; i < queue.tail; i++) {
                    copy[sz + i] = queue.elements[i];
                }
            } else {
                copy = new Object[(queue.size + 1) * 2];
                for (int i = 0; i < queue.size; i++) {
                    copy[i] = queue.elements[i];
                }
            }
            queue.elements = copy;
            queue.head = 0;
            queue.tail = queue.size;
        }
    }

    //pre: true
    //post: size = 0
    public static void clear(ArrayQueueADT queue) {
        queue.elements = new Object[2];
        queue.size = 0;
        queue.head = 0;
        queue.tail = 0;
    }

    //pre: c != null
    //post: R = k for i = 0...n-1 if elements[i] == c k++
    public static int count(ArrayQueueADT queue, Object c) {
        int cnt = 0;
        if (queue.head < queue.tail) {
            for (int i = queue.head; i < queue.tail; i++) {
                if (queue.elements[i].equals(c))
                    cnt++;
            }
        } else if (queue.head > queue.tail) {
            for (int i = 0; i < queue.tail; i++) {
                if (queue.elements[i].equals(c))
                    cnt++;
            }
            for (int i = queue.head; i < queue.elements.length; i++) {
                if (queue.elements[i].equals(c))
                    cnt++;
            }
        } else {
            if (queue.size > 0 && queue.elements[queue.head].equals(c)) {
                cnt = 1;
            } else cnt = 0;
        }
        return cnt;
    }
}

