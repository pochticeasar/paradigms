package queue;

import java.util.function.Predicate;

public interface Queue {
    //pre: k != null
    //post: elements[n - 1] = k && size' = size + 1
    public void enqueue(Object k);

    //pre: size > 0
    //post: R = elements[0] && immutable(size') && && size' = size - 1
    public Object dequeue();

    //pre: true
    //post: R = size && size' = size
    public int size();

    //pre: true
    //post: size = 0
    public void clear();

    //pre: true
    //post: R = (size == 0) && size' = size
    public boolean isEmpty();

    //pre: size > 0
    //post: R = elements[0] && Immutable(size) && size' == size
    public Object element();

    //pre: c != null
    //post: R = k for i = 0...n-1 if С.test(elements[i]) k++
    public int countIf(Predicate<Object> С);
}
