package com.horstmann.violet.product.diagram.abstracts.edge;

import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 22.02.2016
 */
public class ArrowheadEdgeBeanInfo extends LineEdgeBeanInfo
{
    public ArrowheadEdgeBeanInfo()
    {
        super(ArrowheadEdge.class);
    }

    protected ArrowheadEdgeBeanInfo(Class<?> beanClass)
    {
        super(beanClass);
    }

    @Override
    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        List<PropertyDescriptor> propertyDescriptorList = super.createPropertyDescriptorList();

        if(displayStartArrowhead)
        {
            propertyDescriptorList.add(createPropertyDescriptor("startArrowheadChoiceList", startArrowheadLabel,0));
        }
        if(displayEndArrowhead)
        {
            propertyDescriptorList.add(createPropertyDescriptor("endArrowheadChoiceList", endArrowheadLabel,10));
        }
        return propertyDescriptorList;
    }

    protected String startArrowheadLabel = "startArrowheadLabel";
    protected String endArrowheadLabel = "endArrowheadLabel";

    protected boolean displayStartArrowhead = true;
    protected boolean displayEndArrowhead = true;
}
