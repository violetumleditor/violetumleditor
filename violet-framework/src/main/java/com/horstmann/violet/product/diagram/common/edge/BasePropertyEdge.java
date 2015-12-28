package com.horstmann.violet.product.diagram.common.edge;

import com.horstmann.violet.product.diagram.abstracts.edge.SegmentedLineEdge;

/**
 * Created by Adrian Bobrowski on 27.12.2015.
 */
public class BasePropertyEdge extends SegmentedLineEdge{
    public BasePropertyEdge() {}

    @Override
    public BasePropertyEdge clone() {
        return new BasePropertyEdge(this);
    }

    protected BasePropertyEdge(SegmentedLineEdge clone) {
        super(clone);
    }
}
