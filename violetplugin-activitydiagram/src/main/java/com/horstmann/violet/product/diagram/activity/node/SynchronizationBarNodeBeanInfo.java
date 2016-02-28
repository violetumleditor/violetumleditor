package com.horstmann.violet.product.diagram.activity.node;

import com.horstmann.violet.product.diagram.activity.ActivityDiagramConstant;
import com.horstmann.violet.product.diagram.common.node.NodeBeanInfo;

import java.beans.PropertyDescriptor;
import java.util.List;

public class SynchronizationBarNodeBeanInfo extends NodeBeanInfo
{
    public SynchronizationBarNodeBeanInfo()
    {
        super(SynchronizationBarNode.class);
        addResourceBundle(ActivityDiagramConstant.ACTIVITY_DIAGRAM_RESOURCE);
    }

    @Override
    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        List<PropertyDescriptor> propertyDescriptorList = super.createPropertyDescriptorList();
        propertyDescriptorList.add(createPropertyDescriptor(NAME_VAR_NAME, NAME_LABEL_KEY,1));
        return propertyDescriptorList;
    }

    private static final String NAME_LABEL_KEY = "synchronization_bar_node.orientation";
    private static final String NAME_VAR_NAME = "orientation";
}
