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
            propertyDescriptorList.add(createPropertyDescriptor(START_VAR_NAME, START_LABEL_KEY,2));
        }
        if(displayCenterLabel)
        {
            propertyDescriptorList.add(createPropertyDescriptor(CENTER_VAR_NAME, CENTER_LABEL_KEY,3));
        }
        if(displayEndLabel)
        {
            propertyDescriptorList.add(createPropertyDescriptor(END_VAR_NAME, END_LABEL_KEY,6));
        }
        return propertyDescriptorList;
    }

    protected boolean displayStartLabel = true;
    protected boolean displayCenterLabel = true;
    protected boolean displayEndLabel = true;

    protected static final String START_LABEL_KEY = "label.start";
    protected static final String CENTER_LABEL_KEY = "label.center";
    protected static final String END_LABEL_KEY = "label.end";
    private static final String START_VAR_NAME = "startLabel";
    private static final String CENTER_VAR_NAME = "centerLabel";
    private static final String END_VAR_NAME = "endLabel";
}
