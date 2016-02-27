package com.horstmann.violet.product.diagram.abstracts.edge;

import com.horstmann.violet.framework.injection.resources.ResourceBundleConstant;
import com.horstmann.violet.framework.util.BeanInfo;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 21.02.2016
 */
public class LineEdgeBeanInfo extends BeanInfo
{
    public LineEdgeBeanInfo()
    {
        super(LineEdge.class);

        setLabelsFromResourceBundle();
    }

    protected LineEdgeBeanInfo(Class<?> beanClass)
    {
        super(beanClass);

        setLabelsFromResourceBundle();
    }

    private void setLabelsFromResourceBundle()
    {
        ResourceBundle rs = ResourceBundle.getBundle(ResourceBundleConstant.NODE_AND_EDGE_STRINGS, Locale.getDefault());
        try
        {
            lineStyleLabel = rs.getString(LINE_STYLE_LABEL_KEY);
        }catch (Exception e){
            lineStyleLabel = LINE_STYLE_LABEL_KEY;
        }
        try
        {
            bentStyleLabel = rs.getString(BENT_STYLE_LABEL_KEY);
        }catch (Exception e){
            bentStyleLabel = BENT_STYLE_LABEL_KEY;
        }
    }

    @Override
    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        List<PropertyDescriptor> propertyDescriptorList = super.createPropertyDescriptorList();

        if(displayLineStyle)
        {
            propertyDescriptorList.add(createPropertyDescriptor(LINE_STYLE_VAR_NAME, lineStyleLabel, 1));
        }
        if(displayBentStyle)
        {
            propertyDescriptorList.add(createPropertyDescriptor(BENT_STYLE_VAR_NAME, bentStyleLabel,99));
        }
        return propertyDescriptorList;
    }

    protected String lineStyleLabel;
    protected String bentStyleLabel;

    protected boolean displayLineStyle = true;
    protected boolean displayBentStyle = true;

    protected static final String LINE_STYLE_LABEL_KEY = "style.line";
    protected static final String BENT_STYLE_LABEL_KEY = "style.bent";
    private static final String LINE_STYLE_VAR_NAME = "lineStyleChoiceList";
    private static final String BENT_STYLE_VAR_NAME = "bentStyleChoiceList";
}
