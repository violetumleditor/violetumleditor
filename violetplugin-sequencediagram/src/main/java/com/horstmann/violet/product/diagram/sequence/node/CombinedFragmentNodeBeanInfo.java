package com.horstmann.violet.product.diagram.sequence.node;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * The bean info for the CombinedFragmentNode type.
 */
public class CombinedFragmentNodeBeanInfo extends SimpleBeanInfo {

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor typeDescriptor = new PropertyDescriptor("type", CombinedFragmentNode.class);
            typeDescriptor.setValue("priority", new Integer(1));

            PropertyDescriptor contentDescriptor = new PropertyDescriptor("frameContent", CombinedFragmentNode.class);
            contentDescriptor.setValue("priority", new Integer(2));

            return new PropertyDescriptor[]{
                    typeDescriptor,
                    contentDescriptor
            };
        } catch (IntrospectionException exception) {
            return null;
        }
    }
}