package com.horstmann.violet.product.diagram.sequence.edge;

import com.horstmann.violet.product.diagram.common.edge.LabeledLineEdgeBeanInfo;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 04.03.2016
 */
public abstract class CallEdgeBeanInfo extends LabeledLineEdgeBeanInfo
{
    protected CallEdgeBeanInfo(Class<?> beanClass)
    {
        super(beanClass);

        displayLineStyle = false;
        displayEndArrowhead = false;
        displayStartArrowhead = false;
        displayBentStyle = false;
    }
}
