package com.horstmann.violet.product.diagram.classes.edge;

import com.horstmann.violet.product.diagram.property.ArrowheadChoiceList;
import com.horstmann.violet.product.diagram.property.LineStyleChoiceList;
import com.horstmann.violet.product.diagram.abstracts.edge.LabeledLineEdge;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 23.02.2016
 */
public class InterfaceInheritanceEdge extends LabeledLineEdge
{
    public InterfaceInheritanceEdge()
    {
        super();
        setStartArrowhead(ArrowheadChoiceList.NONE);
        setEndArrowhead(ArrowheadChoiceList.TRIANGLE_WHITE);
        setLineStyle(LineStyleChoiceList.DOTTED);
    }

    protected InterfaceInheritanceEdge(InterfaceInheritanceEdge cloned)
    {
        super(cloned);
    }

    @Override
    public void deserializeSupport()
    {
        super.deserializeSupport();
        setStartArrowhead(ArrowheadChoiceList.NONE);
        setEndArrowhead(ArrowheadChoiceList.TRIANGLE_WHITE);
        setLineStyle(LineStyleChoiceList.DOTTED);
    }

    @Override
    protected InterfaceInheritanceEdge copy() throws CloneNotSupportedException
    {
        return new InterfaceInheritanceEdge(this);
    }
}
