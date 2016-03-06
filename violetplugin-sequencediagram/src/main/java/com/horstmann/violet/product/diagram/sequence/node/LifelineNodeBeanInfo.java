package com.horstmann.violet.product.diagram.sequence.node;


import com.horstmann.violet.product.diagram.common.node.ColorableNodeBeanInfo;
import com.horstmann.violet.product.diagram.sequence.SequenceDiagramConstant;

import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * The bean info for the CombinedFragmentNode type.
 */
public class LifelineNodeBeanInfo extends ColorableNodeBeanInfo
{
    public LifelineNodeBeanInfo()
    {
        super(LifelineNode.class);
        addResourceBundle(SequenceDiagramConstant.SEQUENCE_DIAGRAM_STRINGS);
    }

    @Override
    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        List<PropertyDescriptor> propertyDescriptorList = super.createPropertyDescriptorList();

        propertyDescriptorList.add(createPropertyDescriptor(NAME_VAR_NAME, NAME_LABEL_KEY, 1));
        propertyDescriptorList.add(createPropertyDescriptor(TYPE_VAR_NAME, TYPE_LABEL_KEY, 2));
        propertyDescriptorList.add(createPropertyDescriptor(END_OF_LIVE_VAR_NAME, END_OF_LIVE_LABEL_KEY, 3));

        return propertyDescriptorList;
    }

    protected static final String NAME_LABEL_KEY = "object.name";
    protected static final String TYPE_LABEL_KEY = "object.type";
    protected static final String END_OF_LIVE_LABEL_KEY = "object.end_of_life";
    private static final String NAME_VAR_NAME = "name";
    private static final String TYPE_VAR_NAME = "type";
    private static final String END_OF_LIVE_VAR_NAME = "endOfLife";
}
