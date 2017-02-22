package com.horstmann.violet.workspace.editorpart.behavior;

import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.ISwitchableNode;
import com.horstmann.violet.product.diagram.abstracts.node.IVisibleNode;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ShowMenuOnRightClickBehavior extends AbstractEditorPartBehavior
{

    public ShowMenuOnRightClickBehavior(IEditorPart editorPart)
    {
        ResourceBundleInjector.getInjector().inject(this);
        this.editorPart = editorPart;
        this.graph = editorPart.getGraph();
        this.selectionHandler = editorPart.getSelectionHandler();
    }
    
    @Override
    public void onMouseReleased(MouseEvent event)
    {
        boolean isButton3Released = (event.getButton() == MouseEvent.BUTTON3);
        if (isButton3Released)
        {
            double zoom = editorPart.getZoomFactor();
            final Point2D mousePoint = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
            changeSelectedElementIfNeeded(mousePoint);
            getPopupMenu().show(this.editorPart.getSwingComponent(), event.getX(), event.getY());
        }
    }
    
    private void changeSelectedElementIfNeeded(Point2D mouseLocation)
    {
        INode node = this.graph.findNode(mouseLocation);
        IEdge edge = this.graph.findEdge(mouseLocation);
        List<INode> selectedNodes = this.selectionHandler.getSelectedNodes();
        if (node != null && !selectedNodes.contains(node))
        {
            this.selectionHandler.clearSelection();
            this.selectionHandler.addSelectedElement(node);
        }
        List<IEdge> selectedEdges = this.selectionHandler.getSelectedEdges();
        if (edge != null && !selectedEdges.contains(edge))
        {
            this.selectionHandler.clearSelection();
            this.selectionHandler.addSelectedElement(edge);
        }

    }
    
    private JPopupMenu getPopupMenu() {
        if (this.popupMenu == null) {
            this.popupMenu = new JPopupMenu();
            this.popupMenu = fillMenu(this.popupMenu);
        }
        this.popupMenu = updateConvertOptionMenu(this.popupMenu);
        this.popupMenu = updateShowHideContentOptionMenu(this.popupMenu);
        return this.popupMenu;
    }
    
    /**
     * Update convert option menu - show button only when selected node has convert option.
     * @param aPopupMenu the JPopupMenu
     * @return the JPopupMenu
     */
    private JPopupMenu updateConvertOptionMenu(JPopupMenu aPopupMenu){
    	boolean beforeStatus = isConvertVisible;
    	isConvertVisible = false;
        if(selectionHandler.isNodeSelectedAtLeast()){
        	List<INode> selectedNodes = selectionHandler.getSelectedNodes();
        	int switchableNodes = 0;
        	for(INode node : selectedNodes){
        		if(node instanceof ISwitchableNode){
        			switchableNodes++;
        		}
        	}
        	if(switchableNodes==selectedNodes.size()){
        		isConvertVisible = true;
        		if(beforeStatus != isConvertVisible){
                    aPopupMenu.add(convert);
        		}
        	}
        }
        if(!isConvertVisible && beforeStatus!=isConvertVisible){
        	aPopupMenu.remove(convert);
        }
    	return aPopupMenu;
    }
    
    /**
     * Update show/hide content option menu - show button only when selected node has switchVisible option.
     * @param aPopupMenu the JPopupMenu
     * @return the JPopupMenu
     */
    private JPopupMenu updateShowHideContentOptionMenu(JPopupMenu aPopupMenu){
    	boolean beforeStatus = isShowHideVisible;
    	isShowHideVisible = false;
        if(selectionHandler.isNodeSelectedAtLeast()){
        	List<INode> selectedNodes = selectionHandler.getSelectedNodes();
        	int ivisibleNodes = 0;
        	for(INode node : selectedNodes){
        		if(node instanceof IVisibleNode){
        			ivisibleNodes++;
        		}
        	}
        	if(ivisibleNodes==selectedNodes.size()){
        		isShowHideVisible = true;
        		if(beforeStatus != isShowHideVisible){
                    aPopupMenu.add(show);
        		}
        	}
        }
        if(!isShowHideVisible && beforeStatus!=isShowHideVisible){
        	aPopupMenu.remove(show);
        }
    	return aPopupMenu;
    }
    
    /**
     * Initializes menu
     */
    private JPopupMenu fillMenu(JPopupMenu aPopupMenu)
    {
        undo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                IEditorPartBehaviorManager behaviorManager = ShowMenuOnRightClickBehavior.this.editorPart.getBehaviorManager();
                List<UndoRedoCompoundBehavior> found = behaviorManager.getBehaviors(UndoRedoCompoundBehavior.class);
                if (found.size() != 1) {
                    return;
                }
                found.get(0).undo();
            }
        });
        aPopupMenu.add(undo);

        redo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                IEditorPartBehaviorManager behaviorManager = ShowMenuOnRightClickBehavior.this.editorPart.getBehaviorManager();
                List<UndoRedoCompoundBehavior> found = behaviorManager.getBehaviors(UndoRedoCompoundBehavior.class);
                if (found.size() != 1) {
                    return;
                }
                found.get(0).redo();
            }
        });
        aPopupMenu.add(redo);

        properties.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                IEditorPartBehaviorManager behaviorManager = ShowMenuOnRightClickBehavior.this.editorPart.getBehaviorManager();
                List<EditSelectedBehavior> found = behaviorManager.getBehaviors(EditSelectedBehavior.class);
                if (found.size() != 1) {
                    return;
                }
                found.get(0).createSelectedItemEditMenu();
            }
        });
        aPopupMenu.add(properties);
        
        convert.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
            	ShowMenuOnRightClickBehavior.this.editorPart.convertSelected();
            }
        });

        cut.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                IEditorPartBehaviorManager behaviorManager = ShowMenuOnRightClickBehavior.this.editorPart.getBehaviorManager();
                List<CutCopyPasteBehavior> found = behaviorManager.getBehaviors(CutCopyPasteBehavior.class);
                if (found.size() != 1) {
                    return;
                }
                found.get(0).cut();
            }
        });
        aPopupMenu.add(cut);

        copy.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                IEditorPartBehaviorManager behaviorManager = ShowMenuOnRightClickBehavior.this.editorPart.getBehaviorManager();
                List<CutCopyPasteBehavior> found = behaviorManager.getBehaviors(CutCopyPasteBehavior.class);
                if (found.size() != 1) {
                    return;
                }
                found.get(0).copy();
            }
        });
        aPopupMenu.add(copy);

        paste.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                IEditorPartBehaviorManager behaviorManager = ShowMenuOnRightClickBehavior.this.editorPart.getBehaviorManager();
                List<CutCopyPasteBehavior> found = behaviorManager.getBehaviors(CutCopyPasteBehavior.class);
                if (found.size() != 1) {
                    return;
                }
                found.get(0).paste();
            }
        });
        aPopupMenu.add(paste);

        find.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                IEditorPartBehaviorManager behaviorManager = ShowMenuOnRightClickBehavior.this.editorPart.getBehaviorManager();
                List<FindBehavior> found = behaviorManager.getBehaviors(FindBehavior.class);
                if (found.size() != 1) {
                    return;
                }
                found.get(0).find();
            }
        });
        aPopupMenu.add(find);

        delete.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                ShowMenuOnRightClickBehavior.this.editorPart.removeSelected();
            }
        });
        aPopupMenu.add(delete);
        
        show.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                ShowMenuOnRightClickBehavior.this.editorPart.switchVisableOnSelectedNodes();
            }
        });
        
        selectAll.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                IEditorPartBehaviorManager behaviorManager = ShowMenuOnRightClickBehavior.this.editorPart.getBehaviorManager();
                List<SelectAllBehavior> found = behaviorManager.getBehaviors(SelectAllBehavior.class);
                if (found.size() != 1) {
                    return;
                }
                found.get(0).selectAllGraphElements();
            }
        });
        aPopupMenu.add(selectAll);
        
        return aPopupMenu;
    }
    
    private JPopupMenu popupMenu; 
    
    private IGraph graph;

    private IEditorPartSelectionHandler selectionHandler;
    
    @ResourceBundleBean(key = "edit.undo")
    private JMenuItem undo;

    @ResourceBundleBean(key = "edit.redo")
    private JMenuItem redo;

    @ResourceBundleBean(key = "edit.properties")
    private JMenuItem properties;
    
    @ResourceBundleBean(key = "edit.convert")
    private JMenuItem convert;

    @ResourceBundleBean(key = "edit.cut")
    private JMenuItem cut;

    @ResourceBundleBean(key = "edit.copy")
    private JMenuItem copy;

    @ResourceBundleBean(key = "edit.paste")
    private JMenuItem paste;

    @ResourceBundleBean(key = "edit.find")
    private JMenuItem find;

    @ResourceBundleBean(key = "edit.delete")
    private JMenuItem delete;
    
    @ResourceBundleBean(key = "edit.show")
    private JMenuItem show;

    @ResourceBundleBean(key = "edit.select_all")
    private JMenuItem selectAll;
    
    /* Status of convert button in popup menu */
    private boolean isConvertVisible = false;
    
    /* Status of show/hide content button in popup menu */
    private boolean isShowHideVisible = false;
    
    private IEditorPart editorPart;
  
}
