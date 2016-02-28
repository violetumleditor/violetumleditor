package com.horstmann.violet.product.diagram.common.edge;

/**
 * TODO javadoc
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 27.12.2015
 */
public class BasePropertyEdge extends LabeledLineEdge {
    public BasePropertyEdge() {}

    @Override
    public BasePropertyEdge copy() {
        return new BasePropertyEdge(this);
    }

    protected BasePropertyEdge(LabeledLineEdge clone) {
        super(clone);
    }
}
