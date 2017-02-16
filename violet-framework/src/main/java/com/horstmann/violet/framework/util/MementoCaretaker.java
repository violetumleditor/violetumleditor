package com.horstmann.violet.framework.util;

public class MementoCaretaker<T>{
    private T state;

    public void save(T memento){
        state = memento;
    }

    public T load(){
        return state;
    }
}
