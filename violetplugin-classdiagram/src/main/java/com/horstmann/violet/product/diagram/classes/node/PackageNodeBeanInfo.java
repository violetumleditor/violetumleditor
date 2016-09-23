package com.horstmann.violet.product.diagram.classes.node;


import com.horstmann.violet.product.diagram.classes.ClassDiagramConstant;
import com.horstmann.violet.product.diagram.common.node.ColorableNodeBeanInfo;

import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * The bean info for the PackageNode type.
 */
public class PackageNodeBeanInfo extends ColorableNodeBeanInfo
{
    public PackageNodeBeanInfo()
    {
        super(PackageNode.class);
        addResourceBundle(ClassDiagramConstant.CLASS_DIAGRAM_STRINGS);
    }

    @Override
    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        List<PropertyDescriptor> propertyDescriptorList = super.createPropertyDescriptorList();

        propertyDescriptorList.add(createPropertyDescriptor(NAME_VAR_NAME, NAME_LABEL_KEY, 1));
        propertyDescriptorList.add(createPropertyDescriptor(CONTEXT_VAR_NAME, CONTEXT_LABEL_KEY, 2));

        return propertyDescriptorList;
    }

    protected static final String NAME_LABEL_KEY = "package.name";
    protected static final String CONTEXT_LABEL_KEY = "package.context";
    private static final String NAME_VAR_NAME = "name";
    private static final String CONTEXT_VAR_NAME = "context";
}
