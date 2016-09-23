package com.horstmann.violet.product.diagram.common.edge;

import com.horstmann.violet.framework.injection.resources.ResourceBundleConstant;
import com.horstmann.violet.framework.util.BeanInfo;
import com.horstmann.violet.product.diagram.abstracts.edge.AbstractEdgeBeanInfo;

import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 21.02.2016
 */
public class LineEdgeBeanInfo extends AbstractEdgeBeanInfo
{
    public LineEdgeBeanInfo()
    {
        super(LineEdge.class);
    }

    protected LineEdgeBeanInfo(Class<?> beanClass)
    {
        super(beanClass);
    }

    @Override
    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        List<PropertyDescriptor> propertyDescriptorList = super.createPropertyDescriptorList();

        if(displayLineStyle)
        {
            propertyDescriptorList.add(createPropertyDescriptor(LINE_STYLE_VAR_NAME, LINE_STYLE_LABEL_KEY, 1));
        }
        if(displayBentStyle)
        {
            propertyDescriptorList.add(createPropertyDescriptor(BENT_STYLE_VAR_NAME, BENT_STYLE_LABEL_KEY,99));
        }
        return propertyDescriptorList;
    }

    protected boolean displayLineStyle = true;
    protected boolean displayBentStyle = true;

    protected static final String LINE_STYLE_LABEL_KEY = "style.line";
    protected static final String BENT_STYLE_LABEL_KEY = "style.bent";
    private static final String LINE_STYLE_VAR_NAME = "lineStyleChoiceList";
    private static final String BENT_STYLE_VAR_NAME = "bentStyleChoiceList";
}
