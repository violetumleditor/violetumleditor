package com.horstmann.violet.product.diagram.object.edge;

import com.horstmann.violet.product.diagram.common.edge.LabeledLineEdgeBeanInfo;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 23.02.2016
 */
public class AssociationEdgeBeanInfo extends LabeledLineEdgeBeanInfo
{
    protected AssociationEdgeBeanInfo(Class<?> beanClass)
    {
        super(beanClass);

        displayBentStyle = false;
        displayLineStyle = false;
        displayEndArrowhead = false;
        displayStartArrowhead = false;
    }

    public AssociationEdgeBeanInfo()
    {
        this(AssociationEdge.class);
    }
}
