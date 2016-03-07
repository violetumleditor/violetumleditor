package com.horstmann.violet.product.diagram.activity.node;

import com.horstmann.violet.product.diagram.activity.ActivityDiagramConstant;
import com.horstmann.violet.product.diagram.common.node.ColorableNodeBeanInfo;
import com.horstmann.violet.product.diagram.common.node.NodeBeanInfo;

import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * The bean info for the ActivityNodeBeanInfo type.
 */
public class PageLinkNodeBeanInfo extends ColorableNodeBeanInfo
{
    public PageLinkNodeBeanInfo()
    {
        super(PageLinkNode.class);
        addResourceBundle(ActivityDiagramConstant.ACTIVITY_DIAGRAM_RESOURCE);
    }

    @Override
    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        List<PropertyDescriptor> propertyDescriptorList = super.createPropertyDescriptorList();
        propertyDescriptorList.add(createPropertyDescriptor(NAME_VAR_NAME, NAME_LABEL_KEY,1));
        return propertyDescriptorList;
    }

    private static final String NAME_LABEL_KEY = "page_link_node.name";
    private static final String NAME_VAR_NAME = "name";
}
