package com.horstmann.violet.workspace.editorpart.helpers;

import java.util.List;


public class Helpers {

    /**
     *
     * @return the smallest value from all Elements
     */
    public static double findTheSmallestElement(List<Double> positions){
        double min = positions.get(0);
        for (Double i : positions) {
            if (i < min) min = i;
        }
        return min;
    }

    /**
     *
     * @return the biggest value from all Elements
     */
    public static double findTheBiggestElement(List<Double> positions){
        double max = positions.get(0);
        for (Double i : positions) {
            if (i > max) max = i;
        }
        return max;
    }
}
