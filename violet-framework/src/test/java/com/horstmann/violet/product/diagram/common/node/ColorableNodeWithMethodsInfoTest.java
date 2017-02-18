package com.horstmann.violet.product.diagram.common.node;

import com.horstmann.violet.product.diagram.property.text.SingleLineText;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ColorableNodeWithMethodsInfoTest {
    @Before
    public void beforeEachTest() {
        node = new ColorableNodeWithMethodsInfo();
    }

    @Test
    public void replaceNodeOccurrences_OccurrenceAsMethodName_IsNotReplaced() {
        String methodText = "OldName(variable: String, variable2: Int): void";
        testMethod(methodText, methodText);
    }

    @Test
    public void replaceNodeOccurrences_OccurrenceAsMethodParameterName_IsNotReplaced() {
        String methodText = "sampleMethod(parameter1: String, OldName: Int): void";
        testMethod(methodText, methodText);
    }

    @Test
    public void replaceNodeOccurrences_OccurrenceAsMethodParameterType_IsReplaced() {
        String methodText = "sampleMethod(parameter1: OldName, parameter2: String, parameter3: OldName): String";
        String expectedMethodText = "sampleMethod(parameter1: NewName, parameter2: String, parameter3: NewName): String";
        testMethod(methodText, expectedMethodText);
    }

    @Test
    public void replaceNodeOccurrences_OccurrenceAsMethodReturnedValue_IsReplaced() {
        String methodText = "sampleMethod(parameter: String): OldName";
        String expectedMethodText = "sampleMethod(parameter: String): NewName";
        testMethod(methodText, expectedMethodText);
    }

    private void testMethod(String givenMethodSignature, String expectedMethodSignature) {
        setMethod(givenMethodSignature);
        node.replaceNodeOccurrences(oldName, newName);
        assertEquals(expectedMethodSignature, node.getMethods().toEdit());
    }

    private void setMethod(String methodText) {
        SingleLineText method = new SingleLineText();
        method.setText(methodText);
        node.setMethods(method);
    }

    private ColorableNodeWithMethodsInfo node;
    private static final String oldName = "OldName";
    private static final String newName = "NewName";
}