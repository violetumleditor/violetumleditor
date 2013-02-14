package com.horstmann.violet.workspace.sidebar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.PanelUI;

import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.framework.theme.ThemeManager;
import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;

@ResourceBundleBean(resourceReference=SideBar.class)
public class SideBarUI extends PanelUI
{

    public SideBarUI(SideBar sideBar)
    {
        this.sideBar = sideBar;
        ResourceBundleInjector.getInjector().inject(this);
    }

    @Override
    public void installUI(JComponent c)
    {
        c.removeAll();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        this.taskPane = new JTaskPane();
        addElementToTaskPane(this.sideBar.getEditorToolsBar().getAWTComponent(), standardButtonsTitle);
        addElementToTaskPane(this.sideBar.getGraphToolsBar().getAWTComponent(), diagramToolsTitle);
        addElementToTaskPane(this.sideBar.getOptionalToolsBar().getAWTComponent(), extendedFunctionsTitle);
        for (ISideBarElement anExternalElement : this.sideBar.getExternalContributionElements().keySet()) {
            String externalElementTitle = this.sideBar.getExternalContributionElements().get(anExternalElement);
            addElementToTaskPane(anExternalElement.getAWTComponent(), externalElementTitle);
        }
        c.add(taskPane);
        c.setBorder(new MatteBorder(0, 1, 0, 0, ThemeManager.getInstance().getTheme().getSidebarBorderColor()));
        fixWidth();
        this.sideBar.doLayout();
        JRootPane rootPane = SwingUtilities.getRootPane(this.sideBar);
        if (rootPane != null) {
            rootPane.repaint();
        }
    }

    private void fixWidth()
    {
        JLabel sizer = new JLabel();
        sizer.setPreferredSize(new Dimension(215, 1));
        this.taskPane.add(sizer);
    }

    private void addElementToTaskPane(final Component c, String title)
    {
        JTaskPaneGroup group = new JTaskPaneGroup();
        group.setFont(group.getFont().deriveFont(Font.PLAIN));
        group.setTitle(title);
        group.setLayout(new BorderLayout());
        group.add(c, BorderLayout.CENTER);
        this.taskPane.add(group);
    }

    private SideBar sideBar;
    private JTaskPane taskPane;

    @ResourceBundleBean(key = "title.standardbuttons.text")
    private String standardButtonsTitle;

    @ResourceBundleBean(key = "title.diagramtools.text")
    private String diagramToolsTitle;

    @ResourceBundleBean(key = "title.extendedfunctions.text")
    private String extendedFunctionsTitle;

}
