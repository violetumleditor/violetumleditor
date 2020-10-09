package com.horstmann.violet.product.diagram.classes.node;

import java.beans.PropertyDescriptor;
import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.node.AbstractNodeBeanInfo;
import com.horstmann.violet.product.diagram.classes.ClassDiagramConstant;

/**
 * The bean info for the InterfaceNode type.
 */
public class InterfaceNodeBeanInfo extends AbstractNodeBeanInfo
{
    public InterfaceNodeBeanInfo()
    {
        super(InterfaceNode.class);
        addResourceBundle(ClassDiagramConstant.CLASS_DIAGRAM_STRINGS);
    }

    @Override
    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        List<PropertyDescriptor> propertyDescriptorList = super.createPropertyDescriptorList();

        propertyDescriptorList.add(createPropertyDescriptor(NAME_VAR_NAME, NAME_LABEL_KEY, 1));
        propertyDescriptorList.add(createPropertyDescriptor(METHODS_VAR_NAME, METHODS_LABEL_KEY, 3));

        return propertyDescriptorList;
    }

    protected static final String NAME_LABEL_KEY = "interface.name";
    protected static final String METHODS_LABEL_KEY = "interface.methods";
    private static final String NAME_VAR_NAME = "name";
    private static final String METHODS_VAR_NAME = "methods";
}