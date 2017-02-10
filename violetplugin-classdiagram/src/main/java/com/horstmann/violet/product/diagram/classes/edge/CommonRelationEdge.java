package com.horstmann.violet.product.diagram.classes.edge;

import com.horstmann.violet.product.diagram.classes.ClassDiagramConstant;
import com.horstmann.violet.product.diagram.common.edge.LabeledLineEdge;
import com.horstmann.violet.product.diagram.property.ArrowheadChoiceList;
import com.horstmann.violet.product.diagram.property.LineStyleChoiceList;

/**
 * Created by Narin on 04.11.2016.
 * This class is used to create an arrow: "this class is associated with...".
 */
public class CommonRelationEdge extends LabeledLineEdge {

    public CommonRelationEdge() {
        super();
        setStartArrowhead(ArrowheadChoiceList.NONE);
        setEndArrowhead(ArrowheadChoiceList.NONE);
        setLineStyle(LineStyleChoiceList.SOLID);
    }

    protected CommonRelationEdge(final CommonRelationEdge cloned) {
        super(cloned);
    }

    @Override
    protected void beforeReconstruction() {
        super.beforeReconstruction();
        setStartArrowhead(ArrowheadChoiceList.NONE);
        setEndArrowhead(ArrowheadChoiceList.NONE);
        setLineStyle(LineStyleChoiceList.SOLID);
    }

    @Override
    protected CommonRelationEdge copy() throws CloneNotSupportedException {
        return new CommonRelationEdge(this);
    }

    @Override
    public String getToolTip() {
        return ClassDiagramConstant.CLASS_DIAGRAM_RESOURCE.getString("tooltip.common_relation_edge");
    }

}
