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
public class LabeledLineEdgeBeanInfo extends ArrowheadEdgeBeanInfo
{
    public LabeledLineEdgeBeanInfo()
    {
        super(LabeledLineEdge.class);
    }

    protected LabeledLineEdgeBeanInfo(Class<?> beanClass)
    {
        super(beanClass);
    }

    @Override
    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        List<PropertyDescriptor> propertyDescriptorList = super.createPropertyDescriptorList();

        if(displayStartLabel)
        {
            propertyDescriptorList.add(createPropertyDescriptor("startLabel", startLabel,2));
        }
        if(displayCenterLabel)
        {
            propertyDescriptorList.add(createPropertyDescriptor("centerLabel", centerLabel,3));
        }
        if(displayEndLabel)
        {
            propertyDescriptorList.add(createPropertyDescriptor("endLabel", endLabel,6));
        }
        return propertyDescriptorList;
    }

    protected String startLabel = "startLabel";
    protected String centerLabel = "centerLabel";
    protected String endLabel = "endLabel";

    protected boolean displayStartLabel = true;
    protected boolean displayCenterLabel = true;
    protected boolean displayEndLabel = true;
}
