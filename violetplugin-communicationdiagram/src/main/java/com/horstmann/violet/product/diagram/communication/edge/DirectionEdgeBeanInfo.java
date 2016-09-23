package com.horstmann.violet.product.diagram.communication.edge;

import com.horstmann.violet.product.diagram.common.edge.LabeledLineEdgeBeanInfo;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.List;

/**
 * 
 * @author Artur Ratajczak
 *
 */
public class DirectionEdgeBeanInfo  extends LabeledLineEdgeBeanInfo
{
	protected DirectionEdgeBeanInfo(Class<?> beanClass)
	{
		super(beanClass);

		displayLineStyle = false;
		displayEndArrowhead = false;
		displayStartArrowhead = false;
        displayStartLabel = false;
        displayCenterLabel = false;
        displayEndLabel = false;
	}

	public DirectionEdgeBeanInfo()
	{
		this(DirectionEdge.class);
	}

    @Override
    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        List<PropertyDescriptor> propertyDescriptorList = super.createPropertyDescriptorList();

        propertyDescriptorList.add(createPropertyDescriptor(SEQUENCE_NUMBER_VAR_NAME, SEQUENCE_NUMBER_LABEL_KEY,2));
        propertyDescriptorList.add(createPropertyDescriptor(SEQUENTIAL_LOOP_VAR_NAME, SEQUENTIAL_LOOP_LABEL_KEY,3));
        propertyDescriptorList.add(createPropertyDescriptor(MESSAGE_VAR_NAME, MESSAGE_LABEL_KEY,4));
        propertyDescriptorList.add(createPropertyDescriptor(CONCURRENT_LOOP_VAR_NAME, CONCURRENT_LOOP_LABEL_KEY,5));

        return propertyDescriptorList;
    }

    protected static final String SEQUENCE_NUMBER_LABEL_KEY = "label.sequence_number";
    protected static final String SEQUENTIAL_LOOP_LABEL_KEY = "label.sequential_loop";
    protected static final String CONCURRENT_LOOP_LABEL_KEY = "label.concurrent_loop";
    protected static final String MESSAGE_LABEL_KEY = "label.message";
    private static final String SEQUENCE_NUMBER_VAR_NAME = "SequenceNumber";
    private static final String SEQUENTIAL_LOOP_VAR_NAME = "SequentialLoop";
    private static final String CONCURRENT_LOOP_VAR_NAME = "ConcurrentLoop";
    private static final String MESSAGE_VAR_NAME = "Message";
}