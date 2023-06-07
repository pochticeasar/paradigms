package queue;

//Model: elements[0]...elements[n - 1]
//Invariant: for i = 0...n-1: elements[i] != null && Immutable(elements[i])
public class MyArrayQueueModule {
    private static Object[] elements = new Object[2];
    private static int size = 0;
    private static int tail = 0, head = 0;

    //pre: k != null
    //post: elements[n - 1] = k && size' = size + 1
    public static void enqueue(Object k) {
        assert k != null;
        ensure();
        elements[tail] = k;
        size++;
        tail = (tail + 1) % elements.length;
    }

    //pre: size > 0
    //post: R = elements[0] && immutable(size') && && size' = size - 1
    public static Object dequeue() {
        assert size > 0;
        Object res = element();
        elements[head] = null;
        size--;
        head = (head + 1) % elements.length;
        return res;
    }

    //pre: true
    //post: R = size && size' = size
    public static int size() {
        return size;
    }

    //pre: true
    //post: R = (size == 0) && size' = size
    public static boolean isEmpty() {
        return size == 0;
    }

    //pre: size > 0
    //post: R = elements[0] && Immutable(size) && size' == size
    public static Object element() {
        assert size > 0;
        return elements[head];
    }

    private static void ensure() {
        if (size + 1 >= elements.length || head == tail) {
            Object[] copy;
            if (head > tail) {
                copy = new Object[(size + 1) * 2];
                for (int i = head; i < elements.length; i++) {
                    copy[i - head] = elements[i];
                }
                int sz = size + 1 - head;
                for (int i = 0; i < tail; i++) {
                    copy[sz + i] = elements[i];
                }
            } else {
                copy = new Object[(size + 1) * 2];
                for (int i = 0; i < size; i++) {
                    copy[i] = elements[i];
                }
            }
            elements = copy;
            head = 0;
            tail = size;
        }
    }

    //pre: true
    //post: size = 0
    public static void clear() {
        elements = new Object[2];
        size = 0;
        head = 0;
        tail = 0;
    }

    //pre: c != null
    //post: R = k for i = 0...n-1 if elements[i] == c k++
    public static int count(Object c) {
        int cnt = 0;
        if (head < tail) {
            for (int i = head; i < tail; i++) {
                if (elements[i].equals(c))
                    cnt++;
            }
        } else if (head > tail) {
            for (int i = 0; i < tail; i++) {
                if (elements[i].equals(c))
                    cnt++;
            }
            for (int i = head; i < elements.length; i++) {
                if (elements[i].equals(c))
                    cnt++;
            }
        } else {
            if (size > 0 && elements[head].equals(c)) {
                cnt = 1;
            } else cnt = 0;
        }
        return cnt;
    }
}
