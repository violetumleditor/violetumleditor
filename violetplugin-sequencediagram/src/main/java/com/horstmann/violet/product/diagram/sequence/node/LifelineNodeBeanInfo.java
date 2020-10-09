package com.horstmann.violet.product.diagram.sequence.node;


import java.beans.PropertyDescriptor;
import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.node.AbstractNodeBeanInfo;
import com.horstmann.violet.product.diagram.sequence.SequenceDiagramConstant;

/**
 * The bean info for the CombinedFragmentNode type.
 */
public class LifelineNodeBeanInfo extends AbstractNodeBeanInfo
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
