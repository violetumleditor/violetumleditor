package com.horstmann.violet.product.diagram.activity.node;

import com.horstmann.violet.product.diagram.activity.ActivityDiagramConstant;
import com.horstmann.violet.product.diagram.common.node.ColorableNodeBeanInfo;
import com.horstmann.violet.product.diagram.common.node.NodeBeanInfo;

import java.beans.PropertyDescriptor;
import java.util.List;

public class DecisionNodeBeanInfo extends ColorableNodeBeanInfo
{
    public DecisionNodeBeanInfo()
    {
        super(DecisionNode.class);
        addResourceBundle(ActivityDiagramConstant.ACTIVITY_DIAGRAM_RESOURCE);
    }

    @Override
    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        List<PropertyDescriptor> propertyDescriptorList = super.createPropertyDescriptorList();
        propertyDescriptorList.add(createPropertyDescriptor(CONDITION_VAR_NAME, CONDITION_LABEL_KEY,1));
        return propertyDescriptorList;
    }

    private static final String CONDITION_LABEL_KEY = "decision_node.condition";
    private static final String CONDITION_VAR_NAME = "condition";
}
