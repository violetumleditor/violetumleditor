package com.horstmann.violet.workspace.sidebar.alignedtools;

import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.editorpart.enums.Direction;
import com.horstmann.violet.workspace.sidebar.ISideBarElement;
import com.horstmann.violet.workspace.sidebar.SideBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

@ResourceBundleBean(resourceReference = SideBar.class)
public class AlignedToolsPanel extends JPanel implements ISideBarElement, ActionListener {

    //region Getters
    public JButton getAlignRightButton(){
        return alignRightButton;
    }

    public JButton getAlignLeftButton() {
        return alignLeftButton;
    }

    public JButton getAlignUpButton() {
        return alignUpButton;
    }

    public JButton getAlignDownButton() {
        return alignDownButton;
    }
    //endregion

    public AlignedToolsPanel(){
        ResourceBundleInjector.getInjector().inject(this);
        this.setUI(new AlignedToolsPanelUI(this));
        ArrayList<JButton> buttons = new ArrayList<JButton>();
        buttons.add(alignRightButton);
        buttons.add(alignLeftButton);
        buttons.add(alignUpButton);
        buttons.add(alignDownButton);
        addListener(buttons);
    }

    private void addListener(ArrayList<JButton> buttons){
        for (JButton button : buttons){
            if(button == null) continue;
            button.addActionListener(this);
        }
    }

    @Override
    public void install(IWorkspace workspace) {
        if(workspace != null){
            this.workspace = workspace;
        }
    }

    @Override
    public Component getAWTComponent() {
        return this;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if(source == alignDownButton){
            workspace.getEditorPart().align(Direction.DOWN);
        }
        else if(source == alignLeftButton) {
            workspace.getEditorPart().align(Direction.LEFT);
        }
        else if(source == alignRightButton){
            workspace.getEditorPart().align(Direction.RIGHT);
        }
        else if(source == alignUpButton){
            workspace.getEditorPart().align(Direction.UP);
        }
    }

    private IWorkspace workspace;

    @ResourceBundleBean(key = "alignRight")
    private JButton alignRightButton;
    @ResourceBundleBean(key = "alignLeft")
    private JButton alignLeftButton;
    @ResourceBundleBean(key = "alignUp")
    private JButton alignUpButton;
    @ResourceBundleBean(key = "alignDown")
    private JButton alignDownButton;
}
