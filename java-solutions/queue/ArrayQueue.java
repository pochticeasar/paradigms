package queue;

import java.util.function.Predicate;

//Model: elements[0]...elements[n - 1]
//Invariant: for i = 0...n-1: elements[i] != null && Immutable(elements[i])
public class ArrayQueue extends AbstractQueue {
    private Object[] elements = new Object[2];
    private int tail = 0, head = 0;
    private int pos = tail;

    public void enqueue(Object k) {
        assert k != null;
        ensure();
        elements[tail] = k;
        size++;
        tail = (tail + 1) % elements.length;
    }


    public Object dequeue() {
        assert size > 0;
        Object res = element();
        elements[head] = null;
        size--;
        head = (head + 1) % elements.length;
        return res;
    }

    public Object element() {
        assert size > 0;
        return elements[head];
    }


    @Override
    protected Object next() {
        Object p = elements[pos];
        pos = (pos + 1) % elements.length;
        return p;
    }

    @Override
    protected void first() {
        pos = head;
    }

    private void ensure() {
        if (size + 1 >= elements.length || head == tail) {
            Object[] copy;
            copy = new Object[(size + 1) * 2];
            if (head > tail) {
                for (int i = head; i < elements.length; i++) {
                    copy[i - head] = elements[i];
                }
                int sz = size + 1 - head;
                for (int i = 0; i < tail; i++) {
                    copy[sz + i] = elements[i];
                }
            } else {
                for (int i = 0; i < size; i++) {
                    copy[i] = elements[i];
                }
            }
            elements = copy;
            head = 0;
            tail = size;
        }
    }

    public void clear() {
        elements = new Object[2];
        clearImpl();
        head = 0;
        tail = 0;
    }

}

