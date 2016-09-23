package com.horstmann.violet.product.diagram.classes.edge;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 23.02.2016
 */
public class AggregationEdgeBeanInfo extends ClassRelationshipEdgeBeanInfo
{
    protected AggregationEdgeBeanInfo(Class<?> beanClass)
    {
        super(beanClass);
    }

    public AggregationEdgeBeanInfo()
    {
        this(AggregationEdge.class);
    }
}
