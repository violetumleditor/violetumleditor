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

import com.horstmann.violet.framework.injection.resources.ResourceBundleConstant;
import com.horstmann.violet.framework.util.nodeusage.NodeUsage;
import com.horstmann.violet.framework.util.nodeusage.NodeUsagesFinder;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.editorpart.behavior.IEditorPartBehavior;
import com.horstmann.violet.workspace.editorpart.enums.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.List;

/**
 * Graph editor.
 */
public class EditorPart extends JPanel implements IEditorPart
{

    /**
     * Type of graph.
     */
    private IGraph graph;

    /**
     * Editor grid.
     */
    private IGrid grid;

    /**
     * Support for selective operations.
     */
    private IEditorPartSelectionHandler selectionHandler = new EditorPartSelectionHandler();

    /**
     * Used for firing event for editors behaviors. {@link IEditorPartBehavior}
     */
    private IEditorPartBehaviorManager behaviorManager = new EditorPartBehaviorManager();

    /**
     * Factor used to grow drawing area.
     */
    private static final double GROW_FACTOR;

    /**
     * Factor used to scale editor.
     */
    private static final double ZOOM_FACTOR;

    /**
     * Minimal zoom.
     */
    private static final double MIN_ZOOM;

    /**
     * Maximum zoom.
     */
    private static final double MAX_ZOOM;

    /**
     * Actual zoom value.
     */
    private double zoom;

    static
    {
        GROW_FACTOR = Double.parseDouble(ResourceBundleConstant.EDITOR_RESOURCE.getString("editorPart.grow.factor"));
        ZOOM_FACTOR = Double.parseDouble(ResourceBundleConstant.EDITOR_RESOURCE.getString("editorPart.zoom.factor"));
        MIN_ZOOM = Double.parseDouble(ResourceBundleConstant.EDITOR_RESOURCE.getString("editorPart.zoom.min"));
        MAX_ZOOM = Double.parseDouble(ResourceBundleConstant.EDITOR_RESOURCE.getString("editorPart.zoom.max"));
    }

    /**
     * Default constructor
     *
     * @param aGraph graph which will be drawn in this editor part
     */
    public EditorPart(final IGraph aGraph)
    {
        this.graph = aGraph;
        this.zoom = 1;
        this.grid = new PlainGrid(this);
        this.graph.setGridSticker(grid.getGridSticker());
        addMouseListener(new MouseAdapter()
        {

            @Override
            public void mousePressed(final MouseEvent event)
            {
                behaviorManager.fireOnMousePressed(event);
            }

            @Override
            public void mouseReleased(final MouseEvent event)
            {
                behaviorManager.fireOnMouseReleased(event);
            }

            @Override
            public void mouseClicked(final MouseEvent event)
            {
                behaviorManager.fireOnMouseClicked(event);
            }
        });

        addMouseWheelListener(new MouseWheelListener()
        {
            @Override
            public void mouseWheelMoved(final MouseWheelEvent e)
            {
                behaviorManager.fireOnMouseWheelMoved(e);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseDragged(final MouseEvent event)
            {
                behaviorManager.fireOnMouseDragged(event);
            }

            @Override
            public void mouseMoved(final MouseEvent event)
            {
                behaviorManager.fireOnMouseMoved(event);
            }
        });
        setBounds(0, 0, 0, 0);
        setDoubleBuffered(false);
    }

    @Override
    public IGraph getGraph()
    {
        return this.graph;
    }

    @Override
    public void removeSelected()
    {
        this.behaviorManager.fireBeforeRemovingSelectedElements();
        try
        {
            final List<INode> selectedNodes = selectionHandler.getSelectedNodes();
            final List<IEdge> selectedEdges = selectionHandler.getSelectedEdges();
            remove(selectedNodes, selectedEdges);
        }
        finally
        {
            this.selectionHandler.clearSelection();
            this.behaviorManager.fireAfterRemovingSelectedElements();
        }
    }

    @Override
    public List<NodeUsage> getSelectedNodesUsages()
    {
        final List<INode> selectedNodes = selectionHandler.getSelectedNodes();
        final Collection<INode> allNodes = graph.getAllNodes();

        final NodeUsagesFinder nodeUsagesFinder = new NodeUsagesFinder();
        return nodeUsagesFinder.findNodesUsages(selectedNodes, allNodes);
    }

    @Override
    public List<INode> getSelectedNodes()
    {
        return selectionHandler.getSelectedNodes();
    }

    @Override
    public void clearSelection()
    {
        selectionHandler.clearSelection();
    }

    @Override
    public void selectElement(final INode node)
    {
        selectionHandler.addSelectedElement(node);
    }

    @Override
    public void zoomIn()
    {
        final double newZoom = zoom * ZOOM_FACTOR;
        if (newZoom < MAX_ZOOM)
        {
            zoom = newZoom;
        }
        invalidate();
        repaint();
    }

    @Override
    public void zoomOut()
    {
        final double newZoom = zoom / ZOOM_FACTOR;
        if (newZoom > MIN_ZOOM)
        {
            zoom = newZoom;
        }
        invalidate();
        repaint();

    }

    @Override
    public Dimension getPreferredSize()
    {
        Rectangle2D viewPortBounds = getParent().getBounds();
        Rectangle2D clipBounds = graph.getClipBounds();

        viewPortBounds = calculateZoomedBounds(viewPortBounds);
        clipBounds = calculateZoomedBounds(clipBounds);

        if (viewPortBounds.contains(clipBounds))
        {
            graph.setBounds(viewPortBounds);
            return viewPortBounds.getBounds().getSize();
        }
        return clipBounds.getBounds().getSize();
    }

    private Rectangle2D calculateZoomedBounds(final Rectangle2D bounds)
    {

        final double viewPortMaxX = bounds.getMaxX();
        final double viewPortMaxY = bounds.getMaxY();
        final int width = (int) (zoom * viewPortMaxX);
        final int height = (int) (zoom * viewPortMaxY);
        return new Rectangle(width, height);
    }

    @Override
    public double getZoomFactor()
    {
        return this.zoom;
    }

    @Override
    public IGrid getGrid()
    {
        return this.grid;
    }

    @Override
    public void growDrawingArea()
    {
        final IGraph g = getGraph();
        final Rectangle2D bounds = g.getClipBounds();
        bounds.add(getBounds());
        g.setBounds(new Rectangle2D.Double(0, 0, GROW_FACTOR * bounds.getWidth(), GROW_FACTOR * bounds.getHeight()));
        invalidate();
        repaint();
    }

    @Override
    public void clipDrawingArea()
    {
        final IGraph g = getGraph();
        g.setBounds(null);
        invalidate();
        repaint();
    }

    @Override
    public JComponent getSwingComponent()
    {
        return this;
    }

    @Override
    public void paintImmediately(final int x, final int y, final int w, final int h)
    {
        getSwingComponent().invalidate();
        super.paintImmediately(x, y, w, h);
    }

    @Override
    protected void paintComponent(final Graphics g)
    {
        final boolean valid = getSwingComponent().isValid();
        if (valid)
        {
            return;
        }
        getSwingComponent().revalidate(); // to inform parent scrollpane container
        final Graphics2D g2 = (Graphics2D) g;
        g2.scale(zoom, zoom);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        if (grid.isVisible())
        {
            grid.paint(g2);
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graph.draw(g2);
        for (final IEditorPartBehavior behavior : this.behaviorManager.getBehaviors())
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

    private void remove(final List<INode> nodes, final List<IEdge> edges)
    {
        final IEdge[] edgesArray = edges.toArray(new IEdge[edges.size()]);
        final INode[] nodesArray = nodes.toArray(new INode[nodes.size()]);
        graph.removeNode(nodesArray);
        graph.removeEdge(edgesArray);
    }

    @Override
    public void align(Direction direction){
        Align align = new Align();
        align.alignElements(getSelectedNodes(),direction);
        repaint();
    }


}