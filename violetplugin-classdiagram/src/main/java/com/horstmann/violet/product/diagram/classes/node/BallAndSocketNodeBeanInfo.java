package com.horstmann.violet.product.diagram.classes.node;

import com.horstmann.violet.product.diagram.classes.ClassDiagramConstant;
import com.horstmann.violet.product.diagram.common.node.ColorableNodeBeanInfo;

import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 06.03.2016
 */
public class BallAndSocketNodeBeanInfo extends ColorableNodeBeanInfo
{
    public BallAndSocketNodeBeanInfo()
    {
        super(BallAndSocketNode.class);
        addResourceBundle(ClassDiagramConstant.CLASS_DIAGRAM_STRINGS);
    }

    @Override
    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        List<PropertyDescriptor> propertyDescriptorList = super.createPropertyDescriptorList();

        propertyDescriptorList.add(createPropertyDescriptor(NAME_VAR_NAME, NAME_LABEL_KEY, 1));
        propertyDescriptorList.add(createPropertyDescriptor(ORIENTATION_VAR_NAME, ORIENTATION_LABEL_KEY, 2));
        propertyDescriptorList.add(createPropertyDescriptor(TYPE_VAR_NAME, TYPE_LABEL_KEY, 3));

        return propertyDescriptorList;
    }

    protected static final String NAME_LABEL_KEY = "ball_and_socket.name";
    protected static final String ORIENTATION_LABEL_KEY = "ball_and_socket.orientation";
    protected static final String TYPE_LABEL_KEY = "ball_and_socket.type";
    private static final String NAME_VAR_NAME = "name";
    private static final String ORIENTATION_VAR_NAME = "orientation";
    private static final String TYPE_VAR_NAME = "type";
}