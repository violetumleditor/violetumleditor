package com.horstmann.violet.product.diagram.sequence.node;


import com.horstmann.violet.product.diagram.abstracts.node.AbstractNodeBeanInfo;
import com.horstmann.violet.product.diagram.sequence.SequenceDiagramConstant;

import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * The bean info for the CombinedFragmentNode type.
 */
public class CombinedFragmentNodeBeanInfo extends AbstractNodeBeanInfo
{
    public CombinedFragmentNodeBeanInfo()
    {
        super(CombinedFragmentNode.class);
        addResourceBundle(SequenceDiagramConstant.SEQUENCE_DIAGRAM_STRINGS);
    }

    @Override
    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        List<PropertyDescriptor> propertyDescriptorList = super.createPropertyDescriptorList();

        propertyDescriptorList.add(createPropertyDescriptor(TYPE_VAR_NAME, TYPE_LABEL_KEY, 1));
        propertyDescriptorList.add(createPropertyDescriptor(CONTENT_VAR_NAME, CONTENT_LABEL_KEY, 2));

        return propertyDescriptorList;
    }

    protected static final String CONTENT_LABEL_KEY = "fragment.content";
    protected static final String TYPE_LABEL_KEY = "fragment.operator";
    private static final String CONTENT_VAR_NAME = "frameContent";
    private static final String TYPE_VAR_NAME = "operator";
}