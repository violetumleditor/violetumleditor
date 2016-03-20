package com.horstmann.violet.product.diagram.classes.edge;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 23.02.2016
 */
public class AssociationEdgeBeanInfo extends ClassRelationshipEdgeBeanInfo
{
    protected AssociationEdgeBeanInfo(Class<?> beanClass)
    {
        super(beanClass);
    }

    public AssociationEdgeBeanInfo()
    {
        this(AssociationEdge.class);
    }
}
