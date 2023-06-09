package br.com.anticheat.util.evictinglist;

import java.util.Collection;
import java.util.LinkedList;

public class EvictingList<T> extends LinkedList<T> {

    public static String user = "%%__USER__%%";
    public static String username = "%%__USERNAME__%%";

    private int maxSize;

    public EvictingList(int maxSize) {
        this.maxSize = maxSize;
    }

    public EvictingList(Collection<? extends T> c, int maxSize) {
        super(c);
        this.maxSize = maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public boolean add(T t) {
        if (size() >= getMaxSize()) removeFirst();
        return super.add(t);
    }
}