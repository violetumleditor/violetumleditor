package com.horstmann.violet.product.diagram.classes.edge;

import com.horstmann.violet.framework.property.ArrowheadChoiceList;
import com.horstmann.violet.framework.property.LineStyleChoiceList;
import com.horstmann.violet.product.diagram.abstracts.edge.LabeledLineEdge;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 23.02.2016
 */
public class InheritanceEdge extends LabeledLineEdge
{
    public InheritanceEdge()
    {
        super();
        setStartArrowhead(ArrowheadChoiceList.NONE);
        setEndArrowhead(ArrowheadChoiceList.TRIANGLE_WHITE);
        setLineStyle(LineStyleChoiceList.SOLID);
    }

    protected InheritanceEdge(InheritanceEdge cloned)
    {
        super(cloned);
    }

    @Override
    public void deserializeSupport()
    {
        super.deserializeSupport();
        setStartArrowhead(ArrowheadChoiceList.NONE);
        setEndArrowhead(ArrowheadChoiceList.TRIANGLE_WHITE);
        setLineStyle(LineStyleChoiceList.SOLID);
    }

    @Override
    protected InheritanceEdge copy() throws CloneNotSupportedException
    {
        return new InheritanceEdge(this);
    }
}
