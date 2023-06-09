package br.com.anticheat.gui;

@FunctionalInterface
public interface TypedCallback<T> {

    public void execute(T type);

}

