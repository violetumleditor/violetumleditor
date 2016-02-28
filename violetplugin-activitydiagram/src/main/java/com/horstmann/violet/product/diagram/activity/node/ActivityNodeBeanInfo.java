package com.horstmann.violet.product.diagram.activity.node;

import com.horstmann.violet.product.diagram.activity.ActivityDiagramConstant;
import com.horstmann.violet.product.diagram.common.node.NodeBeanInfo;

import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * The bean info for the ActivityNodeBeanInfo type.
 */
public class ActivityNodeBeanInfo extends NodeBeanInfo
{
    public ActivityNodeBeanInfo()
    {
        super(ActivityNode.class);
        addResourceBundle(ActivityDiagramConstant.ACTIVITY_DIAGRAM_RESOURCE);
    }

    @Override
    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        List<PropertyDescriptor> propertyDescriptorList = super.createPropertyDescriptorList();
        propertyDescriptorList.add(createPropertyDescriptor(NAME_VAR_NAME, NAME_LABEL_KEY,1));
        return propertyDescriptorList;
    }

    private static final String NAME_LABEL_KEY = "activity_node.waitTimeAction";
    private static final String NAME_VAR_NAME = "waitTimeAction";
}
