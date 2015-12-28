package com.horstmann.violet.product.diagram.common.edge;

import com.horstmann.violet.product.diagram.abstracts.edge.SegmentedLineEdge;

/**
 * Created by Adrian Bobrowski on 27.12.2015.
 */
public class AdvancedPropertyEdge extends SegmentedLineEdge{
    public AdvancedPropertyEdge() {}

    @Override
    public AdvancedPropertyEdge clone() {
        return new AdvancedPropertyEdge(this);
    }

    protected AdvancedPropertyEdge(SegmentedLineEdge clone) {
        super(clone);
    }
}
