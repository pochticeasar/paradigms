package queue;

import java.util.function.Predicate;

//Model: elements[0]...elements[n - 1]
//Invariant: for i = 0...n-1: elements[i] != null && Immutable(elements[i])
public class LinkedQueue extends AbstractQueue {

    private Node head = null, tail = null, curr = null;

    public void enqueue(Object k) {
        assert k != null;
        Node old = tail;
        tail = new Node(k, null);
        if (isEmpty()) {
            head = tail;
        } else {
            old.next = tail;
        }
        size++;
    }

    public Object dequeue() {
        dequeImpl();
        size--;
        Object res = head.value;
        head = head.next;
        return res;
    }


    public Object element() {
        assert size > 0;
        return head.value;
    }

    public void clear() {
        head = null;
        tail = null;
        clearImpl();
    }

    private static class Node {
        private Object value;
        private Node next;

        public Node(Object value, Node next) {
            assert value != null;

            this.value = value;
            this.next = next;
        }
    }

    @Override
    protected Object next() {
        Object p = curr.value;
        curr = curr.next;
        return p;
    }

    @Override
    protected void first() {
        curr = head;
    }
}