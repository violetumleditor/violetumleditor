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
public class LabeledLineEdgeBeanInfo extends ArrowheadEdgeBeanInfo
{
    public LabeledLineEdgeBeanInfo()
    {
        super(LabeledLineEdge.class);
        setLabelsFromResourceBundle();
    }

    protected LabeledLineEdgeBeanInfo(Class<?> beanClass)
    {
        super(beanClass);
        setLabelsFromResourceBundle();
    }

    private void setLabelsFromResourceBundle()
    {
        ResourceBundle rs = ResourceBundle.getBundle(ResourceBundleConstant.NODE_AND_EDGE_STRINGS, Locale.getDefault());
        try
        {
            startLabel = rs.getString(START_LABEL_KEY);
        }catch (Exception e){
            startLabel = START_LABEL_KEY;
        }
        try
        {
            centerLabel = rs.getString(CENTER_LABEL_KEY);
        }catch (Exception e){
            centerLabel = CENTER_LABEL_KEY;
        }
        try
        {
            endLabel = rs.getString(END_LABEL_KEY);
        }catch (Exception e){
            endLabel = END_LABEL_KEY;
        }
    }

    @Override
    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        List<PropertyDescriptor> propertyDescriptorList = super.createPropertyDescriptorList();

        if(displayStartLabel)
        {
            propertyDescriptorList.add(createPropertyDescriptor(START_VAR_NAME, startLabel,2));
        }
        if(displayCenterLabel)
        {
            propertyDescriptorList.add(createPropertyDescriptor(CENTER_VAR_NAME, centerLabel,3));
        }
        if(displayEndLabel)
        {
            propertyDescriptorList.add(createPropertyDescriptor(END_VAR_NAME, endLabel,6));
        }
        return propertyDescriptorList;
    }

    protected String startLabel;
    protected String centerLabel;
    protected String endLabel;

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
