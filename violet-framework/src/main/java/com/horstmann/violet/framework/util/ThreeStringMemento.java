package com.horstmann.violet.framework.util;

public class ThreeStringMemento {
    private final String firstValue;
    private final String secondValue;
    private final String thirdValue;


    public ThreeStringMemento(String first, String second, String third) {
        firstValue = first;
        secondValue = second;
        thirdValue = third;
    }

    public ThreeStringMemento(String first, String second) {
        firstValue = first;
        secondValue = second;
        thirdValue = null;
    }

    public String getFirstValue() {
        return firstValue;
    }

    public String getSecondValue() {
        return secondValue;
    }

    public String getThirdValue() {
        return thirdValue;
    }
}
