package com.horstmann.violet.product.diagram.abstracts.edge;

import com.horstmann.violet.framework.injection.resources.ResourceBundleConstant;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

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
        setLabelsFromResourceBundle();
    }

    protected ArrowheadEdgeBeanInfo(Class<?> beanClass)
    {
        super(beanClass);
        setLabelsFromResourceBundle();
    }

    private void setLabelsFromResourceBundle()
    {
        ResourceBundle rs = ResourceBundle.getBundle(ResourceBundleConstant.NODE_AND_EDGE_STRINGS, Locale.getDefault());
        try
        {
            startArrowheadLabel = rs.getString(START_ARROWHEAD_LABEL_KEY);
        }catch (Exception e){
            startArrowheadLabel = START_ARROWHEAD_LABEL_KEY;
        }
        try
        {
            endArrowheadLabel = rs.getString(END_ARROWHEAD_LABEL_KEY);
        }catch (Exception e){
            endArrowheadLabel = END_ARROWHEAD_LABEL_KEY;
        }
    }

    @Override
    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        List<PropertyDescriptor> propertyDescriptorList = super.createPropertyDescriptorList();

        if(displayStartArrowhead)
        {
            propertyDescriptorList.add(createPropertyDescriptor(START_ARROWHEAD_VAR_NAME, startArrowheadLabel,0));
        }
        if(displayEndArrowhead)
        {
            propertyDescriptorList.add(createPropertyDescriptor(END_ARROWHEAD_VAR_NAME, endArrowheadLabel,10));
        }
        return propertyDescriptorList;
    }

    protected String startArrowheadLabel;
    protected String endArrowheadLabel;

    protected boolean displayStartArrowhead = true;
    protected boolean displayEndArrowhead = true;

    protected static final String START_ARROWHEAD_LABEL_KEY = "arrowhead.start";
    protected static final String END_ARROWHEAD_LABEL_KEY = "arrowhead.end";
    private static final String START_ARROWHEAD_VAR_NAME = "startArrowheadChoiceList";
    private static final String END_ARROWHEAD_VAR_NAME = "endArrowheadChoiceList";
}
