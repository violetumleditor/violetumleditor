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

package com.horstmann.violet.workspace.editorpart;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.editorpart.behavior.IEditorPartBehavior;

/**
 * Graph editor
 */
public class EditorPart extends JPanel implements IEditorPart
{

    /**
     * Default constructor
     * 
     * @param aGraph graph which will be drawn in this editor part
     */
    public EditorPart(IGraph aGraph)
    {
        this.graph = aGraph;
        this.zoom = 1;
        this.grid = new PlainGrid(this);
        this.graph.setGridSticker(grid.getGridSticker());
        addMouseListener(new MouseAdapter()
        {

            public void mousePressed(MouseEvent event)
            {
                behaviorManager.fireOnMousePressed(event);
            }

            public void mouseReleased(MouseEvent event)
            {
                behaviorManager.fireOnMouseReleased(event);
            }

            public void mouseClicked(MouseEvent event)
            {
                behaviorManager.fireOnMouseClicked(event);
            }
        });

        addMouseWheelListener(new MouseWheelListener()
        {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                behaviorManager.fireOnMouseWheelMoved(e);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseDragged(MouseEvent event)
            {
                behaviorManager.fireOnMouseDragged(event);
            }

            @Override
            public void mouseMoved(MouseEvent event)
            {
                behaviorManager.fireOnMouseMoved(event);
            }
        });
        setBounds(0, 0, 0, 0);
        setDoubleBuffered(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.display.clipboard.IEditorPart#getGraph()
     */
    public IGraph getGraph()
    {
        return this.graph;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.display.clipboard.IEditorPart#removeSelected()
     */
    public void removeSelected()
    {
        this.behaviorManager.fireBeforeRemovingSelectedElements();
        try
        {
            List<INode> selectedNodes = selectionHandler.getSelectedNodes();
            List<IEdge> selectedEdges = selectionHandler.getSelectedEdges();
            IEdge[] edgesArray = selectedEdges.toArray(new IEdge[selectedEdges.size()]);
            INode[] nodesArray = selectedNodes.toArray(new INode[selectedNodes.size()]);
            graph.removeNode(nodesArray);
            graph.removeEdge(edgesArray);
        }
        finally
        {
            this.selectionHandler.clearSelection();
            this.behaviorManager.fireAfterRemovingSelectedElements();
        }
    }

    public List<INode> getSelectedNodes()
    {
        return selectionHandler.getSelectedNodes();
    }

    public List<IEdge> getSelectedEdges()
    {
        return selectionHandler.getSelectedEdges();
    }
    
    public void clearSelection()
    {
        selectionHandler.clearSelection();
    }

    public void selectElement(INode node)
    {
        selectionHandler.addSelectedElement(node);
    }

    @Override
    public Dimension getPreferredSize()
    {
        Dimension parentSize = getParent().getSize();
        Rectangle2D bounds = graph.getClipBounds();
        int width = Math.max((int) (zoom * bounds.getMaxX()), (int) parentSize.getWidth());
        int height = Math.max((int) (zoom * bounds.getMaxY()), (int) parentSize.getHeight());
        if (this.lastWidth != width || this.lastHeight != height) {
            this.lastWidth = width;
            this.lastHeight = height;
        }
        return new Dimension(width, height);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.display.clipboard.IEditorPart#changeZoom(int)
     */
    public void changeZoom(int steps)
    {
        final double FACTOR = Math.sqrt(Math.sqrt(2));
        for (int i = 1; i <= steps; i++)
            zoom *= FACTOR;
        for (int i = 1; i <= -steps; i++)
            zoom /= FACTOR;
        invalidate();
        repaint();
    }

    @Override
    public double getZoomFactor()
    {
        return this.zoom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.product.workspace.editorpart.IEditorPart#getGrid()
     */
    public IGrid getGrid()
    {
        return this.grid;
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.horstmann.violet.framework.workspace.editorpart.IEditorPart# growDrawingArea()
     */
    public void growDrawingArea()
    {
        IGraph g = getGraph();
        Rectangle2D bounds = g.getClipBounds();
        bounds.add(getBounds());
        g.setBounds(new Double(0, 0, GROW_SCALE_FACTOR * bounds.getWidth(), GROW_SCALE_FACTOR * bounds.getHeight()));
        invalidate();
        repaint();
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.horstmann.violet.framework.workspace.editorpart.IEditorPart# clipDrawingArea()
     */
    public void clipDrawingArea()
    {
        IGraph g = getGraph();
        g.setBounds(null);
        invalidate();
        repaint();
    }

    public JComponent getSwingComponent()
    {
        return this;
    }
    
    
    @Override
    public void paintImmediately(int x, int y, int w, int h)
    {
        getSwingComponent().invalidate();
        super.paintImmediately(x, y, w, h);
    }
    
    
    @Override
    protected void paintComponent(Graphics g)
    {
        boolean valid = getSwingComponent().isValid();
        if (valid)
        {
            return;
        }
        getSwingComponent().revalidate(); // to inform parent scrollpane container
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(zoom, zoom);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        if (grid.isVisible()) grid.paint(g2);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graph.draw(g2);
        for (IEditorPartBehavior behavior : this.behaviorManager.getBehaviors())
        {
            behavior.onPaint(g2);
        }
    }
    
    
    
    
    @Override
    public IEditorPartSelectionHandler getSelectionHandler()
    {
        return this.selectionHandler;
    }

    @Override
    public IEditorPartBehaviorManager getBehaviorManager()
    {
        return this.behaviorManager;
    }

    
    

    
    private IGraph graph;

    private IGrid grid;

    private double zoom;

    private IEditorPartSelectionHandler selectionHandler = new EditorPartSelectionHandler();
    
    private int lastWidth = 0;
    
    private int lastHeight = 0;

    /**
     * Scale factor used to grow drawing area
     */
    private static final double GROW_SCALE_FACTOR = Math.sqrt(2);

    private IEditorPartBehaviorManager behaviorManager = new EditorPartBehaviorManager();
    


}