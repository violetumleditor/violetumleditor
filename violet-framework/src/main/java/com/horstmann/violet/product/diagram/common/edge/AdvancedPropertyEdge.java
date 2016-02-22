package com.horstmann.violet.product.diagram.common.edge;

import com.horstmann.violet.product.diagram.abstracts.edge.LabeledLineEdge;

/**
 * Created by Adrian Bobrowski on 27.12.2015.
 */
public class AdvancedPropertyEdge extends LabeledLineEdge {
    public AdvancedPropertyEdge() {}

    @Override
    protected AdvancedPropertyEdge copy()
    {
        return new AdvancedPropertyEdge(this);
    }

    protected AdvancedPropertyEdge(LabeledLineEdge clone) {
        super(clone);
    }
}
