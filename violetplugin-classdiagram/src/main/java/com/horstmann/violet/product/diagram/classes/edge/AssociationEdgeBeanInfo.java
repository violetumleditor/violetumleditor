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
    public AssociationEdgeBeanInfo()
    {
        super(AssociationEdge.class);
    }

    protected AssociationEdgeBeanInfo(Class<?> beanClass)
    {
        super(beanClass);
    }
}
