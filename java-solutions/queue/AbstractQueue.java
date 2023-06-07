package queue;

import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
    protected int size;

    public int countIf(Predicate<Object> c) {
        int cnt = 0;
        if (size == 0) return 0;
        first();
        for (int i = 0; i < size(); i++) {
            if (c.test(next()))
                cnt++;
        }
        return cnt;
    }

    //pre: c != null
    //post: R = k for i = 0...n-1 if elements[i] == c k++
    public int count(Object c) {
        int cnt = 0;
        if (size == 0) return 0;
        first();
        for (int i = 0; i < size(); i++) {
            if (next().equals(c))
                cnt++;
        }
        return cnt;
    }

    protected abstract Object next();


    protected abstract void first();

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    protected void clearImpl() {
        size = 0;
    }

    protected void dequeImpl() {
        assert size > 0;
    }

}

