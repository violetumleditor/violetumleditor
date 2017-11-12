
package com.horstmann.violet.application.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import com.horstmann.violet.application.gui.MainFrame;
import com.horstmann.violet.application.visualization.VisualizationDialog;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.workspace.IWorkspace;

@ResourceBundleBean(resourceReference = MenuFactory.class)
public class VisualizationMenu extends JMenu {

    @ResourceBundleBean(key = "visualization")
    public VisualizationMenu(MainFrame mainFrame){
        ResourceBundleInjector.getInjector().inject(this);
        this.mainFrame = mainFrame;
        this.createMenu();
    }

    private void createMenu(){
       statistics.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VisualizationDialog dialog = new VisualizationDialog(mainFrame);
                dialog.setVisible(true);
            }
        });
        this.add(statistics);

    }


    private JFrame mainFrame;

    @ResourceBundleBean(key = "visualization.statistics")
    private JMenuItem statistics;



}
