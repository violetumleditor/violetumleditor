package com.horstmann.violet.product.diagram.classes.edge;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 23.02.2016
 */
public class DependencyEdgeBeanInfo extends ClassRelationshipEdgeBeanInfo
{
    public DependencyEdgeBeanInfo()
    {
        super(DependencyEdge.class);
    }

    protected DependencyEdgeBeanInfo(Class<?> beanClass)
    {
        super(beanClass);
    }
}