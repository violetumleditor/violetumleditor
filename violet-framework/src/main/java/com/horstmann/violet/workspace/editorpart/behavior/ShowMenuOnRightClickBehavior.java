package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;
import com.seanregan.javaimport.IJavaParseable;
import com.seanregan.javaimport.ImportClassHandler;

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
    public void onMouseClicked(MouseEvent event)
    {
        boolean isButton3Clicked = (event.getButton() == MouseEvent.BUTTON3);
        if (event.getClickCount() == 1 && isButton3Clicked)
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
        return this.popupMenu;
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
                found.get(0).editSelected();
            }
        });
        aPopupMenu.add(properties);

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

        delete.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                ShowMenuOnRightClickBehavior.this.editorPart.removeSelected();
            }
        });
        aPopupMenu.add(delete);
        
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
		
		mImportFromFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<INode> selNodes = ShowMenuOnRightClickBehavior.this.editorPart.getSelectedNodes();
				IEditorPartBehaviorManager behaviorManager = ShowMenuOnRightClickBehavior.this.editorPart.getBehaviorManager();
                if (selNodes.size() != 1) {
                    return;
                }
				
				//Check if selected node is parseable
                if (selNodes.get(0) instanceof IJavaParseable) {
					//Present user with import dialog
					new ImportClassHandler((IJavaParseable)selNodes.get(0)).show();
					
					//Force update
					INode edited = selNodes.get(0);
					behaviorManager.fireAfterEditingNode(edited);
					editorPart.getSwingComponent().invalidate();
				}
			}
		});
		aPopupMenu.add(mImportFromFile);
		
		mRefreshImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<INode> selNodes = ShowMenuOnRightClickBehavior.this.editorPart.getSelectedNodes();
				IEditorPartBehaviorManager behaviorManager = ShowMenuOnRightClickBehavior.this.editorPart.getBehaviorManager();
                if (selNodes.size() != 1) {
                    return;
                }
				
				//Check if selected node is parseable
                if (selNodes.get(0) instanceof IJavaParseable) {
					INode edited		 = selNodes.get(0);
					IJavaParseable jEdit = (IJavaParseable)selNodes.get(0);
					
					//Check if it has a file reference
					if (jEdit.getFileReference() != null) {
						//It does so reparse the file
						jEdit.parseAndPopulate();
						
						//Force update
						behaviorManager.fireAfterEditingNode(edited);
						editorPart.getSwingComponent().invalidate();
					}
				}
			}
		});
		aPopupMenu.add(mRefreshImport);
		
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

    @ResourceBundleBean(key = "edit.cut")
    private JMenuItem cut;

    @ResourceBundleBean(key = "edit.copy")
    private JMenuItem copy;

    @ResourceBundleBean(key = "edit.paste")
    private JMenuItem paste;

    @ResourceBundleBean(key = "edit.delete")
    private JMenuItem delete;

    @ResourceBundleBean(key = "edit.select_all")
    private JMenuItem selectAll;
	
    @ResourceBundleBean(key = "edit.file_import")
    private JMenuItem mImportFromFile;
	
    @ResourceBundleBean(key = "edit.refresh_import")
    private JMenuItem mRefreshImport;
    
    private IEditorPart editorPart;

  
}
