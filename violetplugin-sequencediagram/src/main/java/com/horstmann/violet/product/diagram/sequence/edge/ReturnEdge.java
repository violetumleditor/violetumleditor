/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.product.diagram.sequence.edge;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.horstmann.violet.product.diagram.abstracts.edge.bentstyle.BentStyle;
import com.horstmann.violet.product.diagram.common.edge.LabeledLineEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.property.ArrowheadChoiceList;
import com.horstmann.violet.product.diagram.property.LineStyleChoiceList;
import com.horstmann.violet.product.diagram.sequence.SequenceDiagramConstant;

/**
 * An edge that joins two call node.
 */
public class ReturnEdge extends LabeledLineEdge
{
    public ReturnEdge()
    {
        setEndArrowhead(ArrowheadChoiceList.V);
        setStartArrowhead(ArrowheadChoiceList.NONE);
        setLineStyle(LineStyleChoiceList.DOTTED);
        setBentStyle(BentStyle.STRAIGHT);
    }

    protected ReturnEdge(ReturnEdge clone)
    {
        super(clone);
    }

    @Override
    public void deserializeSupport()
    {
        setBentStyle(BentStyle.STRAIGHT);

        super.deserializeSupport();
        setEndArrowhead(ArrowheadChoiceList.V);
        setStartArrowhead(ArrowheadChoiceList.NONE);
        setLineStyle(LineStyleChoiceList.DOTTED);
    }

    @Override
    public ReturnEdge copy()
    {
        return new ReturnEdge(this);
    }


    @Override
    public String getToolTip()
    {
        return SequenceDiagramConstant.SEQUENCE_DIAGRAM_RESOURCE.getString("return_edge.tooltip");
    }

    @Override
    public Line2D getConnectionPoints()
    {
        return new Line2D.Double(
                getStartNode().getConnectionPoint(this),
                getEndNode().getConnectionPoint(this)
        );
    }
}
