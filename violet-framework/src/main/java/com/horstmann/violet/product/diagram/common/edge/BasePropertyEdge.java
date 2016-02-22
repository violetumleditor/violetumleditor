package com.horstmann.violet.product.diagram.common.edge;

import com.horstmann.violet.product.diagram.abstracts.edge.SegmentedLineEdge;

/**
 * TODO javadoc
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 27.12.2015
 */
public class BasePropertyEdge extends SegmentedLineEdge{
    public BasePropertyEdge() {}

    @Override
    public BasePropertyEdge copy() {
        return new BasePropertyEdge(this);
    }

    protected BasePropertyEdge(SegmentedLineEdge clone) {
        super(clone);
    }
}
