package com.horstmann.violet.product.diagram.activity.node;

import com.horstmann.violet.product.diagram.activity.ActivityDiagramConstant;
import com.horstmann.violet.product.diagram.common.node.ColorableNodeBeanInfo;
import com.horstmann.violet.product.diagram.common.node.NodeBeanInfo;

import java.beans.PropertyDescriptor;
import java.util.List;

public class SignalSendingNodeBeanInfo extends ColorableNodeBeanInfo
{
    public SignalSendingNodeBeanInfo()
    {
        super(SignalReceiptNode.class);
        addResourceBundle(ActivityDiagramConstant.ACTIVITY_DIAGRAM_RESOURCE);
    }

    @Override
    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        List<PropertyDescriptor> propertyDescriptorList = super.createPropertyDescriptorList();
        propertyDescriptorList.add(createPropertyDescriptor(SIGNAL_VAR_NAME, SIGNAL_LABEL_KEY,1));
        return propertyDescriptorList;
    }

    private static final String SIGNAL_LABEL_KEY = "signal_sending_node.signal";
    private static final String SIGNAL_VAR_NAME = "signal";
}
