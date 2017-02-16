package com.horstmann.violet.framework.util;

public class LifelineNodeMemento {
    private final String firstValue;
    private final String secondValue;
    private final boolean thirdValue;


    public LifelineNodeMemento(String first, String second, boolean third) {
        firstValue = first;
        secondValue = second;
        thirdValue = third;
    }

    public String getFirstValue() {
        return firstValue;
    }

    public String getSecondValue() {
        return secondValue;
    }

    public boolean getThirdValue() {
        return thirdValue;
    }
}
