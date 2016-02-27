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
public class AggregationEdge extends LabeledLineEdge
{
    public AggregationEdge()
    {
        super();
        setStartArrowhead(ArrowheadChoiceList.NONE);
        setEndArrowhead(ArrowheadChoiceList.DIAMOND_WHITE);
        setLineStyle(LineStyleChoiceList.SOLID);
    }

    protected AggregationEdge(AggregationEdge cloned)
    {
        super(cloned);
    }

    @Override
    public void deserializeSupport()
    {
        super.deserializeSupport();
        setStartArrowhead(ArrowheadChoiceList.NONE);
        setEndArrowhead(ArrowheadChoiceList.DIAMOND_WHITE);
        setLineStyle(LineStyleChoiceList.SOLID);
    }

    @Override
    protected AggregationEdge copy() throws CloneNotSupportedException
    {
        return new AggregationEdge(this);
    }
}
