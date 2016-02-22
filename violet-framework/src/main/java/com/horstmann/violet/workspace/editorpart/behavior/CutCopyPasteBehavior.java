package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;

import com.horstmann.violet.framework.file.GraphFile;
import com.horstmann.violet.framework.file.IGraphFile;
import com.horstmann.violet.framework.file.persistence.IFilePersistenceService;
import com.horstmann.violet.framework.file.persistence.XStreamBasedPersistenceService;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.Id;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;

public class CutCopyPasteBehavior extends AbstractEditorPartBehavior
{

    /**
     * The concerned workspace
     */
    private IEditorPart editorPart;

    /**
     * Used to convert graph to XML and to get graph back from XML
     */
    
    private IFilePersistenceService persistenceService = new XStreamBasedPersistenceService();

    /**
     * Keep mouse location to paste on just above the current mouse location
     */
    private Point2D lastMouseLocation = new Point2D.Double(0, 0);

    /**
     * Default constructor
     * 
     * @param editorPart
     */
    public CutCopyPasteBehavior(IEditorPart editorPart)
    {
        BeanInjector.getInjector().inject(this);
    	this.editorPart = editorPart;
    }

    @Override
    public void onMousePressed(MouseEvent event)
    {
        double zoom = editorPart.getZoomFactor();
        this.lastMouseLocation = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
    }

    /**
     * Cuts selected graph elements
     */
    public void cut()
    {
        copy();
        editorPart.removeSelected();
        editorPart.getSwingComponent().invalidate();
        editorPart.getSwingComponent().repaint();
    }

    /**
     * Copies selected graph elements to system clipboard
     */
    public void copy()
    {
        IGraph graph = editorPart.getGraph();
        Class<? extends IGraph> graphClass = graph.getClass();
        IGraphFile newGraphFile = new GraphFile(graphClass);
        IGraph newGraph = newGraphFile.getGraph();
        IEditorPartSelectionHandler selectionHandler = editorPart.getSelectionHandler();
        List<INode> selectedNodes = selectionHandler.getSelectedNodes();
        // We use an mapper for ids because they are changed when each node_old is added to the newGraph
        Map<Id, Id> idMapper = new HashMap<Id, Id>();
        for (INode aSelectedNode : selectedNodes)
        {
        	if (isAncestorInCollection(aSelectedNode, selectedNodes)) {
        		// In this case, id doesn't change. It only changes on newGraph.addNode()
        		Id currentId = aSelectedNode.getId();
				idMapper.put(currentId, currentId);
        		continue;
        	}
        	INode clone = aSelectedNode.clone();
            Id oldId = clone.getId();
            Point2D locationOnGraph = aSelectedNode.getLocationOnGraph();
            newGraph.addNode(clone, locationOnGraph);
            Id newId = clone.getId();
            idMapper.put(oldId, newId);
        }
        List<IEdge> selectedEdges = selectionHandler.getSelectedEdges();
        for (IEdge aSelectedEdge : selectedEdges)
        {
            IEdge clone = aSelectedEdge.clone();
            Point2D startLocation = clone.getStartLocation();
            Point2D endLocation = clone.getEndLocation();
            Point2D[] transitionPoints = clone.getTransitionPoints();
            Id oldStartId = clone.getStartNode().getId();
            Id oldEndId = clone.getEndNode().getId();
            Id newStartId = idMapper.get(oldStartId);
            Id newEndId = idMapper.get(oldEndId);
			INode startNode = newGraph.findNode(newStartId);
			INode endNode = newGraph.findNode(newEndId);
            if (startNode != null && endNode != null)
            {
                newGraph.connect(clone, startNode, startLocation, endNode, endLocation, transitionPoints);
            }
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        persistenceService.write(newGraph, byteArrayOutputStream);
        byteArrayOutputStream.toString();
        String xmlContent = byteArrayOutputStream.toString();
        pushContentToSystemClipboard(xmlContent);
        
    }

    /**
     * Paste elements from system wide clipboard to graph
     */
    public void paste()
    {
        IGraph graph = this.editorPart.getGraph();
        try
        {
            String xmlContent = getContentFromSystemClipboard();
            if (xmlContent == null)
            {
                return; // If no content, we stop here
            }
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xmlContent.getBytes());
            IGraph deserializedGraph = persistenceService.read(byteArrayInputStream);
            deserializedGraph = translateToMouseLocation(deserializedGraph, this.lastMouseLocation);

            Collection<INode> nodesFromClipboard = deserializedGraph.getAllNodes();
            List<INode> nodes = filterOnNodePrototypes(nodesFromClipboard);
            List<INode> nodesReallyPasted = new ArrayList<INode>();
            for (INode aNode : nodes)
            {
                if (isAncestorInCollection(aNode, nodes)) continue;
                boolean isAdded = graph.addNode(aNode, aNode.getLocationOnGraph());
                if (isAdded)
                {
                    nodesReallyPasted.add(aNode);
                }
            }

            Collection<IEdge> edgesFromClipboard = deserializedGraph.getAllEdges();
            List<IEdge> edges = filterOnEdgePrototypes(edgesFromClipboard);
            List<IEdge> edgesReallyPasted = new ArrayList<IEdge>();
            for (IEdge anEdge : edges)
            {
                Point2D startLocation = anEdge.getStartLocation();
                Point2D endLocation = anEdge.getEndLocation();
                Point2D[] transitionPoints = anEdge.getTransitionPoints();
                INode startNode = graph.findNode(anEdge.getStartNode().getId());
                INode endNode = graph.findNode(anEdge.getEndNode().getId());
                if (startNode != null && endNode != null)
                {
                    boolean isConnected = graph.connect(anEdge, startNode, startLocation, endNode, endLocation, transitionPoints);
                    if (isConnected)
                    {
                        edgesReallyPasted.add(anEdge);
                    }
                }
            }

            addUndoRedoSupport(nodesReallyPasted, edgesReallyPasted);
            selectPastedElements(nodesReallyPasted, edgesReallyPasted);

            editorPart.getSwingComponent().invalidate();
            editorPart.getSwingComponent().repaint();
        }
        catch (IOException e)
        {
            // Nothing to do
        }
    }

    /**
     * Adds Undo/Redo support to copy pastes
     * 
     * @param nodesPasted
     * @param edgesPasted
     */
    private void addUndoRedoSupport(List<INode> nodesPasted, List<IEdge> edgesPasted)
    {
        IEditorPartBehaviorManager behaviorManager = this.editorPart.getBehaviorManager();
        List<UndoRedoCompoundBehavior> found = behaviorManager.getBehaviors(UndoRedoCompoundBehavior.class);
        if (found.size() != 1)
        {
            return;
        }
        UndoRedoCompoundBehavior undoRedoBehavior = found.get(0);

        undoRedoBehavior.startHistoryCapture();
        CompoundEdit capturedEdit = undoRedoBehavior.getCurrentCapturedEdit();
        for (final INode aNode : nodesPasted)
        {
            UndoableEdit edit = new AbstractUndoableEdit()
            {
                @Override
                public void undo() throws CannotUndoException
                {
                    IGraph graph = editorPart.getGraph();
                    graph.removeNode(aNode);
                    super.undo();
                }

                @Override
                public void redo() throws CannotRedoException
                {
                    super.redo();
                    IGraph graph = editorPart.getGraph();
                    graph.addNode(aNode, aNode.getLocationOnGraph());
                }
            };
            capturedEdit.addEdit(edit);
        }

        for (final IEdge anEdge : edgesPasted)
        {
            UndoableEdit edit = new AbstractUndoableEdit()
            {
                @Override
                public void undo() throws CannotUndoException
                {
                    IGraph graph = editorPart.getGraph();
                    graph.removeEdge(anEdge);
                    super.undo();
                }

                @Override
                public void redo() throws CannotRedoException
                {
                    super.redo();
                    IGraph graph = editorPart.getGraph();
                    graph.connect(anEdge, anEdge.getStartNode(), anEdge.getStartLocation(), anEdge.getEndNode(), anEdge.getEndLocation(), anEdge.getTransitionPoints());
                }
            };
            capturedEdit.addEdit(edit);
        }
        undoRedoBehavior.stopHistoryCapture();
    }

    private void selectPastedElements(List<INode> nodesPasted, List<IEdge> edgesPasted)
    {
        IEditorPartSelectionHandler selectionHandler = this.editorPart.getSelectionHandler();
        selectionHandler.clearSelection();
        for (final INode aNode : nodesPasted)
        {
            selectionHandler.addSelectedElement(aNode);
        }
        for (final IEdge anEdge : edgesPasted)
        {
            selectionHandler.addSelectedElement(anEdge);
        }
    }

    /**
     * As we can copy/paste on many diagrams, we ensure that we paste only node_old types acceptable for the current diagram
     * 
     * @param nodes from clipboard
     * @return nodes acceptable for the current diagram
     */
    private List<INode> filterOnNodePrototypes(Collection<INode> nodes)
    {
        IGraph currentGraph = this.editorPart.getGraph();
        List<INode> nodePrototypes = currentGraph.getNodePrototypes();
        List<Class<? extends INode>> classPrototypes = new ArrayList<Class<? extends INode>>();
        for (INode aNodePrototype : nodePrototypes)
        {
            classPrototypes.add(aNodePrototype.getClass());
        }

        List<INode> result = new ArrayList<INode>();
        for (INode aNode : nodes)
        {
            Class<? extends INode> nodeClass = aNode.getClass();
            if (classPrototypes.contains(nodeClass))
            {
                result.add(aNode);
            }
        }
        return result;
    }

    /**
     * As we can copy/paste on many diagrams, we ensure that we paste only edge types acceptable for the current diagram
     * 
     * @param edges from clipboard
     * @return edges acceptable for the current diagram
     */
    private List<IEdge> filterOnEdgePrototypes(Collection<IEdge> edges)
    {
        IGraph currentGraph = this.editorPart.getGraph();
        List<IEdge> edgePrototypes = currentGraph.getEdgePrototypes();
        List<Class<? extends IEdge>> classPrototypes = new ArrayList<Class<? extends IEdge>>();
        for (IEdge aEdgePrototype : edgePrototypes)
        {
            classPrototypes.add(aEdgePrototype.getClass());
        }

        List<IEdge> result = new ArrayList<IEdge>();
        for (IEdge aEdge : edges)
        {
            Class<? extends IEdge> nodeClass = aEdge.getClass();
            if (classPrototypes.contains(nodeClass))
            {
                result.add(aEdge);
            }
        }
        return result;
    }

    /**
     * Moves all the nodes of a graph to a location
     * 
     * @param graph
     * @param mouseLocation
     * @return the modified graph
     */
    private IGraph translateToMouseLocation(IGraph graph, Point2D mouseLocation)
    {
        Rectangle2D clipBounds = graph.getClipBounds();
        double dx = mouseLocation.getX() - clipBounds.getX();
        double dy = mouseLocation.getY() - clipBounds.getY();
        Collection<INode> nodes = graph.getAllNodes();
        for (INode aNode : nodes)
        {
            boolean hasParent = (aNode.getParent() != null);
            if (!hasParent)
            {
                aNode.translate(dx, dy);
            }
        }
        return graph;
    }

    /**
     * Deals with system wide clipboard
     * 
     * @param content
     */
    private void pushContentToSystemClipboard(String content)
    {
        StringSelection dataToClip = new StringSelection(content);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(dataToClip, dataToClip);
    }

    /**
     * Deals with system wide clipboard
     * 
     * @return
     */
    private String getContentFromSystemClipboard()
    {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable clipData = clipboard.getContents(clipboard);
        if (clipData != null)
        {
            try
            {
                if (clipData.isDataFlavorSupported(DataFlavor.stringFlavor))
                {
                    String s = (String) (clipData.getTransferData(DataFlavor.stringFlavor));
                    return s;
                }
            }
            catch (UnsupportedFlavorException ufe)
            {
                return null;
            }
            catch (IOException ioe)
            {
                return null;
            }
        }
        return null;
    }

    /**
     * Converts a string into a bytebuffer
     * 
     * @param msg
     * @return
     */
    private ByteBuffer convertToByteBuffer(String msg)
    {
        return ByteBuffer.wrap(msg.getBytes());
    }

    /**
     * Converts a bytebuffer into a string
     * 
     * @param bytebuffer
     * @return
     */
    private String convertToString(ByteBuffer bytebuffer)
    {
        byte[] bytearray = new byte[bytebuffer.remaining()];
        bytebuffer.get(bytearray);
        String s = new String(bytearray);
        return s;
    }

    /**
     * Checks if the given list contains an ancestor of the given node_old
     * 
     * @param childNode
     * @param ancestorList
     * @return b
     */
    private boolean isAncestorInCollection(INode childNode, Collection<INode> ancestorList)
    {
        for (INode anAncestorNode : ancestorList)
        {
            boolean ancestorRelationship = isAncestorRelationship(childNode, anAncestorNode);
            if (ancestorRelationship) return true;
        }
        return false;
    }

    /**
     * Checks if ancestorNode is a parent node_old of child node_old
     * 
     * @param childNode
     * @param ancestorNode
     * @return b
     */
    private boolean isAncestorRelationship(INode childNode, INode ancestorNode)
    {
        INode parent = childNode.getParent();
        if (parent == null)
        {
            return false;
        }
        List<INode> fifo = new ArrayList<INode>();
        fifo.add(parent);
        while (!fifo.isEmpty())
        {
            INode aParentNode = fifo.get(0);
            fifo.remove(0);
            if (aParentNode.equals(ancestorNode))
            {
                return true;
            }
            INode aGranParent = aParentNode.getParent();
            if (aGranParent != null)
            {
                fifo.add(aGranParent);
            }
        }
        return false;
    }
    
    private List<INode> getFamily(INode aParentNode) {
    	List<INode> family = new ArrayList<INode>();
    	List<INode> fifo = new ArrayList<INode>();
    	fifo.addAll(aParentNode.getChildren());
    	while (!fifo.isEmpty()) {
    		INode aFamilyMember = fifo.get(0);
    		family.add(aFamilyMember);
    		fifo.addAll(aFamilyMember.getChildren());
    		fifo.remove(0);
    	}
    	return family;
    }

}
