package com.horstmann.violet.product.diagram.classes.node;


import com.horstmann.violet.product.diagram.classes.ClassDiagramConstant;
import com.horstmann.violet.product.diagram.common.node.ColorableNodeBeanInfo;

import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * The bean info for the ClassNode type.
 */
public class EnumNodeBeanInfo extends ColorableNodeBeanInfo
{
    public EnumNodeBeanInfo()
    {
        super(EnumNode.class);
        addResourceBundle(ClassDiagramConstant.CLASS_DIAGRAM_STRINGS);
    }

    @Override
    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        List<PropertyDescriptor> propertyDescriptorList = super.createPropertyDescriptorList();

        propertyDescriptorList.add(createPropertyDescriptor(NAME_VAR_NAME, NAME_LABEL_KEY, 1));
        propertyDescriptorList.add(createPropertyDescriptor(ATTRIBUTES_VAR_NAME, ATTRIBUTES_LABEL_KEY, 2));
        propertyDescriptorList.add(createPropertyDescriptor(METHODS_VAR_NAME, METHODS_LABEL_KEY, 3));

        return propertyDescriptorList;
    }

    protected static final String NAME_LABEL_KEY = "enum.name";
    protected static final String ATTRIBUTES_LABEL_KEY = "enum.attributes";
    protected static final String METHODS_LABEL_KEY = "class.methods";
    private static final String NAME_VAR_NAME = "name";
    private static final String ATTRIBUTES_VAR_NAME = "attributes";
    private static final String METHODS_VAR_NAME = "methods";
}