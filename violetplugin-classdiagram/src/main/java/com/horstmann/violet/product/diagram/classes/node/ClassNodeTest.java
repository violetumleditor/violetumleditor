package com.horstmann.violet.product.diagram.classes.node;

import com.horstmann.violet.product.diagram.property.text.SingleLineText;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClassNodeTest {

    @Before
    public void beforeEachTest() {
        classNode = new ClassNode();
    }

    @Test
    public void replaceNodeOccurrences_OccurrenceAsAttributeType_IsReplaced() {
        testAttributes("variable: OldName", "variable: NewName");
    }

    @Test
    public void replaceNodeOccurrences_OccurrenceAsAttributeName_IsNotReplaced() {
        String attributeText = "OldName: String";
        testAttributes(attributeText, attributeText);
    }

    @Test
    public void replaceNodeOccurrences_OccurrenceAsPartOfAttributeType_IsNotReplaced() {
        String attributeText = "variable: OldNameIsOnlyPartOfTypeName";
        testAttributes(attributeText, attributeText);
    }

    @Test
    public void replaceNodeOccurrences_OccurrenceAsAttributeTypeWithWhiteSpaces_IsReplaced() {
        testAttributes("variable:   OldName   ", "variable: NewName");
    }

    private void testAttributes(String givenAttribute, String expectedAttribute) {
        setAttribute(givenAttribute);
        classNode.replaceNodeOccurrences(oldName, newName);
        assertEquals(expectedAttribute, classNode.getAttributes().toEdit());
    }

    private void setAttribute(String attributeText) {
        SingleLineText attribute = new SingleLineText();
        attribute.setText(attributeText);
        classNode.setAttributes(attribute);
    }

    private ClassNode classNode;
    private static final String oldName = "OldName";
    private static final String newName = "NewName";
}