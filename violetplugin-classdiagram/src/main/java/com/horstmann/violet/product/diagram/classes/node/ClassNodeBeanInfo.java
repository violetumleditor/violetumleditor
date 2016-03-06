package com.horstmann.violet.product.diagram.classes.node;

import com.horstmann.violet.product.diagram.classes.ClassDiagramConstant;
import com.horstmann.violet.product.diagram.common.node.ColorableNodeBeanInfo;

import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 20.02.2016
 */
public class ClassNodeBeanInfo extends ColorableNodeBeanInfo
{
    public ClassNodeBeanInfo()
    {
        super(ClassNode.class);
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

    protected static final String NAME_LABEL_KEY = "class.name";
    protected static final String ATTRIBUTES_LABEL_KEY = "class.attributes";
    protected static final String METHODS_LABEL_KEY = "class.methods";
    private static final String NAME_VAR_NAME = "name";
    private static final String ATTRIBUTES_VAR_NAME = "attributes";
    private static final String METHODS_VAR_NAME = "methods";
}