package com.horstmann.violet.product.diagram.activity.edge;

import com.horstmann.violet.product.diagram.common.edge.LabeledLineEdgeBeanInfo;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 23.02.2016
 */
public class ActivityTransitionEdgeBeanInfo extends LabeledLineEdgeBeanInfo
{
    public ActivityTransitionEdgeBeanInfo()
    {
        super(ActivityTransitionEdge.class);

        displayLineStyle = false;
        displayStartArrowhead = false;
        displayEndArrowhead = false;
    }

    protected ActivityTransitionEdgeBeanInfo(Class<?> beanClass)
    {
        super(beanClass);

        displayLineStyle = false;
        displayStartArrowhead = false;
        displayEndArrowhead = false;
    }
}
