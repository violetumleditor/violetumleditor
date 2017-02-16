package com.horstmann.violet.framework.util;

public class BallAndSocketMemento {
    private final String name;
    private final int selectedOrientation;
    private final int selectedType;


    public BallAndSocketMemento(String name, int selectedOrientation, int selectedType) {
        this.name = name;
        this.selectedOrientation = selectedOrientation;
        this.selectedType = selectedType;
    }

    public String getName() {
        return name;
    }

    public int getSelectedOrientation() {
        return selectedOrientation;
    }

    public int getSelectedType() {
        return selectedType;
    }
}
