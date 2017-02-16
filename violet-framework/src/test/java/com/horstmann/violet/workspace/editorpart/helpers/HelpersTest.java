package com.horstmann.violet.workspace.editorpart.helpers;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HelpersTest {

    private List<Double> positions = new ArrayList<>(Arrays.asList(6.5, 5.5, 44.4 ,17.0));
    private static final double DELTA = 1e-15;

    @Test
    public void findTheSmallestElementTest(){
        assertEquals(Helpers.findTheSmallestElement(positions),5.5,DELTA);
    }

    @Test
    public void findTheBiggestElementTest() throws Exception {
        assertEquals(Helpers.findTheBiggestElement(positions),44.4,DELTA);
    }

}