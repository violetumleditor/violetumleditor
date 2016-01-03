package com.horstmann.violet.product.diagram.activity.nodes;


import com.horstmann.violet.product.diagram.activity.ActivityDiagramConstant;
import com.horstmann.violet.product.diagram.activity.ActivityResource;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.Locale;
import java.util.ResourceBundle;

public class DecisionNodeBeanInfo extends SimpleBeanInfo
{
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        try
        {
            PropertyDescriptor nameDescriptor = new PropertyDescriptor("name", DecisionNode.class);
            nameDescriptor.setValue("priority", new Integer(1));
            nameDescriptor.setName(ActivityResource.ACTIVITY.getResourceString("decision.node.name"));

            return new PropertyDescriptor[]
            {
                nameDescriptor,
            };
        }
        catch (IntrospectionException exception)
        {
            return null;
        }
    }
}
