package com.horstmann.violet.product.diagram.sequence.node;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * The bean info for the IntegrationFrameNode type.
 */
public class LifelineNodeBeanInfo extends SimpleBeanInfo {

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor typeDescriptor = new PropertyDescriptor("name", LifelineNode.class);
            typeDescriptor.setValue("priority", 0);

            PropertyDescriptor contentDescriptor = new PropertyDescriptor("endOfLife", LifelineNode.class);
            contentDescriptor.setValue("priority", 1);

            return new PropertyDescriptor[]{
                    typeDescriptor,
                    contentDescriptor
            };
        } catch (IntrospectionException exception) {
            return null;
        }
    }
}