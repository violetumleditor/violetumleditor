package com.horstmann.violet.product.diagram.classes.edge;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 23.02.2016
 */
public class InheritanceEdgeBeanInfo extends ClassRelationshipEdgeBeanInfo
{
    public InheritanceEdgeBeanInfo()
    {
        super(InheritanceEdge.class);
        displayStartArrowhead = false;
    }

    protected InheritanceEdgeBeanInfo(Class<?> beanClass)
    {
        super(beanClass);
        displayStartArrowhead = false;
    }
}