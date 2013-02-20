package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;

public class ShowMenuOnRightClickBehavior extends AbstractEditorPartBehavior
{

    public ShowMenuOnRightClickBehavior(IEditorPart editorPart)
    {
        ResourceBundleInjector.getInjector().inject(this);
        this.editorPart = editorPart;
    }
    
    @Override
    public void onMouseClicked(MouseEvent event)
    {
        boolean isButton3Clicked = (event.getButton() == MouseEvent.BUTTON3);
        if (event.getClickCount() == 1 && isButton3Clicked)
        {
            getPopupMenu().show(this.editorPart.getSwingComponent(), event.getX(), event.getY());
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
        
        return aPopupMenu;
    }
    
    private JPopupMenu popupMenu; 
    
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
    
    private IEditorPart editorPart;

  
}
