package com.horstmann.violet.product.diagram.abstracts.edge;

import com.horstmann.violet.product.diagram.abstracts.BeanInfo;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

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
    }

    protected LineEdgeBeanInfo(Class<?> beanClass)
    {
        super(beanClass);
    }

    @Override
    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        List<PropertyDescriptor> propertyDescriptorList = new ArrayList<PropertyDescriptor>();

        if(displayLineStyle)
        {
            propertyDescriptorList.add(createPropertyDescriptor("lineStyleChoiceList", lineStyleLabel, 1));
        }
        if(displayBentStyle)
        {
            propertyDescriptorList.add(createPropertyDescriptor("bentStyleChoiceList", bentStyleLabel,99));
        }
        return propertyDescriptorList;
    }

//    @ResourceBundleBean(key = "dialog.error.title")

    protected String lineStyleLabel = "LineStyleLabel";
    protected String bentStyleLabel = "BentStyleLabel";

    protected boolean displayLineStyle = true;
    protected boolean displayBentStyle = true;
}
