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
public class DependencyEdge extends LabeledLineEdge
{
    public DependencyEdge()
    {
        super();
        setStartArrowhead(ArrowheadChoiceList.NONE);
        setEndArrowhead(ArrowheadChoiceList.V);
        setLineStyle(LineStyleChoiceList.DOTTED);
    }

    protected DependencyEdge(DependencyEdge cloned)
    {
        super(cloned);
    }

    @Override
    public void deserializeSupport()
    {
        super.deserializeSupport();
        setStartArrowhead(ArrowheadChoiceList.NONE);
        setEndArrowhead(ArrowheadChoiceList.V);
        setLineStyle(LineStyleChoiceList.DOTTED);
    }

    @Override
    protected DependencyEdge copy() throws CloneNotSupportedException
    {
        return new DependencyEdge(this);
    }
}
