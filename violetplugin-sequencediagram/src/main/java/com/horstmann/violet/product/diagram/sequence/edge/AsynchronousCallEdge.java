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

import com.horstmann.violet.product.diagram.property.ArrowheadChoiceList;
import com.horstmann.violet.product.diagram.sequence.SequenceDiagramConstant;

/**
 * An edge that joins two call node. Typically, call edges are used in sequence diagram to represent calls between entities (call
 * node).
 */
public class AsynchronousCallEdge extends CallEdge
{
    public AsynchronousCallEdge()
    {
        setEndArrowhead(ArrowheadChoiceList.V);
    }

    protected AsynchronousCallEdge(AsynchronousCallEdge clone)
    {
        super(clone);
    }

    @Override
    public void deserializeSupport()
    {
        super.deserializeSupport();
        setEndArrowhead(ArrowheadChoiceList.V);
    }

    @Override
    public AsynchronousCallEdge copy()
    {
        return new AsynchronousCallEdge(this);
    }

    @Override
    public String getToolTip()
    {
        return SequenceDiagramConstant.SEQUENCE_DIAGRAM_RESOURCE.getString("asynchronous_call_edge.tooltip");
    }
}
