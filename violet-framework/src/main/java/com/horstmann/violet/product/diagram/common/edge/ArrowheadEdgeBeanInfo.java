package com.horstmann.violet.product.diagram.common.edge;

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
            propertyDescriptorList.add(createPropertyDescriptor(START_ARROWHEAD_VAR_NAME, START_ARROWHEAD_LABEL_KEY,0));
        }
        if(displayEndArrowhead)
        {
            propertyDescriptorList.add(createPropertyDescriptor(END_ARROWHEAD_VAR_NAME, END_ARROWHEAD_LABEL_KEY,10));
        }
        return propertyDescriptorList;
    }

    protected boolean displayStartArrowhead = true;
    protected boolean displayEndArrowhead = true;

    protected static final String START_ARROWHEAD_LABEL_KEY = "arrowhead.start";
    protected static final String END_ARROWHEAD_LABEL_KEY = "arrowhead.end";
    private static final String START_ARROWHEAD_VAR_NAME = "startArrowheadChoiceList";
    private static final String END_ARROWHEAD_VAR_NAME = "endArrowheadChoiceList";
}
