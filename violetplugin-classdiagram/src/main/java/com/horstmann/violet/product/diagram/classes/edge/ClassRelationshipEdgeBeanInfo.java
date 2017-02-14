package com.horstmann.violet.product.diagram.classes.edge;

import com.horstmann.violet.product.diagram.common.edge.LabeledLineEdgeBeanInfo;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 23.02.2016
 */
public abstract class ClassRelationshipEdgeBeanInfo extends LabeledLineEdgeBeanInfo
{
    protected ClassRelationshipEdgeBeanInfo(Class<?> beanClass)
    {
        super(beanClass);

        displayLineStyle = false;
    }
}
