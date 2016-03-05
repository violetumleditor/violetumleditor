package com.horstmann.violet.product.diagram.sequence.node;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * The bean info for the CombinedFragmentNode type.
 */
public class LifelineNodeBeanInfo extends SimpleBeanInfo {

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor nameDescriptor = new PropertyDescriptor("name", LifelineNode.class);
            nameDescriptor.setValue("priority", 0);

            PropertyDescriptor typeDescriptor = new PropertyDescriptor("type", LifelineNode.class);
            typeDescriptor.setValue("priority", 1);

            PropertyDescriptor contentDescriptor = new PropertyDescriptor("endOfLife", LifelineNode.class);
            contentDescriptor.setValue("priority", 2);

            return new PropertyDescriptor[]{
                    nameDescriptor,
                    typeDescriptor,
                    contentDescriptor
            };
        } catch (IntrospectionException exception) {
            return null;
        }
    }
}