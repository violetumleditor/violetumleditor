package com.horstmann.violet.product.diagram.classes.edges;

import com.horstmann.violet.product.diagram.abstracts.edge.SegmentedLineEdge;

/**
 * Created by Adrian Bobrowski on 27.12.2015.
 */
public class SimpleEdge extends SegmentedLineEdge{
    public SimpleEdge() {}

    protected SimpleEdge(SegmentedLineEdge clone) {
        super(clone);
    }

    @Override
    public SimpleEdge clone() {
        return new SimpleEdge(super.clone());
    }
}
