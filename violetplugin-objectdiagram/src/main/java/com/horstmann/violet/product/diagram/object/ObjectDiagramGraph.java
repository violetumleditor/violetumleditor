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

package com.horstmann.violet.product.diagram.object;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.horstmann.violet.product.diagram.abstracts.AbstractGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.framework.property.text.SingleLineText;
import com.horstmann.violet.product.diagram.common.edge.BasePropertyEdge;
import com.horstmann.violet.product.diagram.common.edge.NoteEdge;
import com.horstmann.violet.product.diagram.common.node.NoteNode;
import com.horstmann.violet.product.diagram.object.edge.ObjectReferenceEdge;
import com.horstmann.violet.product.diagram.object.node.FieldNode;
import com.horstmann.violet.product.diagram.object.node.ObjectNode;

/**
 * An UML-style object diagram that shows object references.
 */
public class ObjectDiagramGraph extends AbstractGraph
{
    
    @Override
    public boolean addNode(INode newNode, Point2D p)
    {
        INode foundNode = findNode(p);
        if (foundNode == null && newNode.getClass().isAssignableFrom(FieldNode.class)) {
            return false;
        }
        return super.addNode(newNode, p);
    }
    
    public List<INode> getNodePrototypes()
    {
        return NODE_PROTOTYPES;
    }

    public List<IEdge> getEdgePrototypes()
    {
        return EDGE_PROTOTYPES;
    }

    private static final List<INode> NODE_PROTOTYPES = new ArrayList<INode>();

    private static final List<IEdge> EDGE_PROTOTYPES = new ArrayList<IEdge>();

    static
    {
        ResourceBundle rs = ResourceBundle.getBundle(ObjectDiagramConstant.OBJECT_DIAGRAM_STRINGS, Locale.getDefault());

        ObjectNode node0 = new ObjectNode();
        node0.setToolTip(rs.getString("node0.tooltip"));
        NODE_PROTOTYPES.add(node0);

        FieldNode node1 = new FieldNode();
        node1.setToolTip(rs.getString("node1.tooltip"));
        SingleLineText fn = new SingleLineText();
//        fn.setText("name");
        node1.setName(fn);
        SingleLineText fv = new SingleLineText();
//        fv.setText("value");
        node1.setValue(fv);
        NODE_PROTOTYPES.add(node1);

        NoteNode node2 = new NoteNode();
        node2.setToolTip(rs.getString("node2.tooltip"));
        NODE_PROTOTYPES.add(node2);

        ObjectReferenceEdge reference = new ObjectReferenceEdge();
        reference.setToolTip(rs.getString("edge0.tooltip"));
        EDGE_PROTOTYPES.add(reference);

        BasePropertyEdge association = new BasePropertyEdge();
//        association.setBentStyle(BentStyleChoiceList.STRAIGHT);
        association.setToolTip(rs.getString("edge1.tooltip"));
        EDGE_PROTOTYPES.add(association);

        NoteEdge noteEdge = new NoteEdge();
        noteEdge.setToolTip(rs.getString("edge2.tooltip"));
        EDGE_PROTOTYPES.add(noteEdge);
    }

}
