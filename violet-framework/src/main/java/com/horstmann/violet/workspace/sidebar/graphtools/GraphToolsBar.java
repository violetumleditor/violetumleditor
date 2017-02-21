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

package com.horstmann.violet.workspace.sidebar.graphtools;

import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.sidebar.ISideBarElement;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A tool bar that contains node_old and edge prototype icons. Exactly one icon is selected at any time.
 */
public class GraphToolsBar implements IGraphToolsBar, ISideBarElement
{


    
    public void install(IWorkspace workspace)
    {
        IEditorPart editorPart = workspace.getEditorPart();
        IGraph graph = editorPart.getGraph();
        this.nodeTools = getStandardNodeTools(graph);
        this.edgeTools = getStandardEdgeTools(graph);
        this.panel = new GraphToolsBarPanel(this);
        this.panel.setUI(new GraphToolsBarPanelUI());
        reset();
        editorPart.getSelectionHandler().setSelectedTool(getSelectedTool());
    }
    
    public void selectNextTool()
    {
        int nextPos = 0;
        GraphTool selectedTool = getSelectedTool();
        int posForNodes = this.nodeTools.indexOf(selectedTool);
        if (posForNodes >= 0)
        {
            nextPos = posForNodes + 1;
            if (nextPos < this.nodeTools.size())
            {
                setSelectedTool(this.nodeTools.get(nextPos));
            }
            if (nextPos >= this.nodeTools.size() && this.edgeTools.size() > 0)
            {
                setSelectedTool(this.edgeTools.get(0));
            }
            return;
        }
        int posForEdges = this.edgeTools.indexOf(selectedTool);
        if (posForEdges >= 0)
        {
            nextPos = posForEdges + 1;
            if (nextPos < this.edgeTools.size())
            {
                setSelectedTool(this.edgeTools.get(nextPos));
            }
            return;
        }
    }

    public void selectPreviousTool()
    {
        int previousPos = 0;
        GraphTool selectedButton = getSelectedTool();
        int posForNodes = this.nodeTools.indexOf(selectedButton);
        if (posForNodes >= 0)
        {
            previousPos = posForNodes - 1;
            if (previousPos >= 0)
            {
                setSelectedTool(this.nodeTools.get(previousPos));
            }
            return;
        }
        int posForEdges = this.edgeTools.indexOf(selectedButton);
        if (posForEdges >= 0)
        {
            previousPos = posForEdges - 1;
            if (previousPos >= 0)
            {
                setSelectedTool(this.edgeTools.get(previousPos));
            }
            if (previousPos < 0 && this.nodeTools.size() > 0)
            {
                setSelectedTool(this.nodeTools.get(this.nodeTools.size() - 1));
            }
            return;
        }
    }

    public Component getAWTComponent()
    {
        return this.panel;
    }

    @Override
    public List<GraphTool> getNodeTools() {
        return this.nodeTools;
    }
    
    @Override
    public List<GraphTool> getEdgeTools() {
        return this.edgeTools;
    }

    

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.display.clipboard.sidebar.ISideToolPanel#addCustomTool(com.horstmann.violet.product.diagram.abstracts.Node,
     *      java.lang.String)
     */
    public void addTool(INode nodePrototype, String title)
    {
        GraphTool newTool = new GraphTool(nodePrototype, title);
        nodeTools.add(newTool);
        // FIXME : rebuild UI
        
    }

    /**
     * Returns standard node_old tools associated to a graph
     * 
     * @param graph
     * @return tools collection
     */
    private List<GraphTool> getStandardNodeTools(IGraph graph)
    {
    	List<INode> nodeTypes = graph.getNodePrototypes();
        List<GraphTool> tools = new ArrayList<GraphTool>();
        GraphTool firstTool = GraphTool.SELECTION_TOOL;
        tools.add(firstTool);
        if (nodeTypes.size() == 0)
        {
            return tools;
        }
        for (int i = 0; i < nodeTypes.size(); i++)
        {
            GraphTool aTool = new GraphTool(nodeTypes.get(i), nodeTypes.get(i).getToolTip());
            tools.add(aTool);
        }
        return tools;
    }

    /**
     * Returns standard edge tools associated to a graph
     * 
     * @param graph
     * @return tools collection
     */
    private List<GraphTool> getStandardEdgeTools(IGraph graph)
    {
        List<IEdge> edgeTypes = graph.getEdgePrototypes();
        List<GraphTool> tools = new ArrayList<GraphTool>();
        if (edgeTypes.size() == 0)
        {
            return tools;
        }
        for (int i = 0; i < edgeTypes.size(); i++)
        {
            GraphTool aTool = new GraphTool(edgeTypes.get(i), edgeTypes.get(i).getToolTip());
            tools.add(aTool);
        }
        return tools;
    }




    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.display.clipboard.sidebar.ISideToolPanel#getSelectedTool()
     */
    public GraphTool getSelectedTool()
    {
        return this.selectedTool;
    }


    @Override
    public void setSelectedTool(GraphTool t) {
        boolean isNewSelection = !t.equals(this.selectedTool);
        this.selectedTool = t;
        if (isNewSelection) {
            fireToolChangeEvent(t);
        }
    }

    @Override
    public void notifyMouseEvent(GraphTool t, MouseEvent event) {
    	fireToolMouseEvent(t, event);
    }
    
    private void fireToolMouseEvent(GraphTool tool, MouseEvent event)
    {
        Iterator<IGraphToolsBarMouseListener> it = this.clickListeners.iterator();
        while (it.hasNext())
        {
            IGraphToolsBarMouseListener listener = it.next();
            if (event.getID() == MouseEvent.MOUSE_PRESSED) {
            	listener.onMouseToolClicked(tool);
            } else if (event.getID() == MouseEvent.MOUSE_DRAGGED) {
            	listener.onMouseToolDragged(event);
            } else if (event.getID() == MouseEvent.MOUSE_RELEASED) {
            	listener.onMouseToolReleased(event);
            }
        }
    }
    
    /**
     * Remenbers selected tool and informs all listeners about this change
     * 
     * @param nodeOrEdge
     */
    private void fireToolChangeEvent(GraphTool tool)
    {
        Iterator<IGraphToolsBarListener> it = this.listeners.iterator();
        while (it.hasNext())
        {
            IGraphToolsBarListener listener = it.next();
            listener.toolSelectionChanged(tool);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.display.clipboard.sidebar.ISideToolPanel#reset()
     */
    public void reset()
    {
        if (this.nodeTools.size() > 0)
        {
            setSelectedTool(this.nodeTools.get(0));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.display.clipboard.sidebar.ISideToolPanel#addListener(com.horstmann.violet.framework.display.clipboard.sidebar.SideToolPanel.Listener)
     */
    public void addListener(IGraphToolsBarListener listener)
    {
        this.listeners.add(listener);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.display.clipboard.sidebar.ISideToolPanel#removeListener(com.horstmann.violet.framework.display.clipboard.sidebar.SideToolPanel.Listener)
     */
    public void removeListener(IGraphToolsBarListener listener)
    {
        this.listeners.remove(listener);
    }

    public void addMouseListener(IGraphToolsBarMouseListener listener)
    {
        this.clickListeners.add(listener);
    }
    
    public void removeMouseListener(IGraphToolsBarMouseListener listener)
    {
        this.clickListeners.remove(listener);
    }
    
    private List<IGraphToolsBarListener> listeners = new ArrayList<IGraphToolsBarListener>();
    private List<IGraphToolsBarMouseListener> clickListeners = new ArrayList<IGraphToolsBarMouseListener>();
    private List<GraphTool> nodeTools;
    private List<GraphTool> edgeTools;
    private GraphTool selectionTool = GraphTool.SELECTION_TOOL;
    private GraphTool selectedTool;
    private GraphToolsBarPanel panel;


    


}