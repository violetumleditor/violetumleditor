package com.horstmann.violet.product.diagram.classes.edge;

import com.horstmann.violet.product.diagram.classes.ClassDiagramConstant;
import com.horstmann.violet.product.diagram.property.ArrowheadChoiceList;
import com.horstmann.violet.product.diagram.property.LineStyleChoiceList;
import com.horstmann.violet.product.diagram.common.edge.LabeledLineEdge;

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
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();

        setStartArrowhead(ArrowheadChoiceList.NONE);
        setEndArrowhead(ArrowheadChoiceList.TRIANGLE_WHITE);
        setLineStyle(LineStyleChoiceList.DOTTED);
    }

    @Override
    protected InterfaceInheritanceEdge copy() throws CloneNotSupportedException
    {
        return new InterfaceInheritanceEdge(this);
    }

    @Override
    public String getToolTip()
    {
        return ClassDiagramConstant.CLASS_DIAGRAM_RESOURCE.getString("interface_inheritance_edge.tooltip");
    }
}
