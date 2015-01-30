package com.horstmann.violet.workspace.sidebar.colortools;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.PanelUI;

import com.horstmann.violet.framework.theme.ThemeManager;

public class ColorToolsPanelUI extends PanelUI
{

    /**
     * Default constructor
     * 
     * @param colorToolsPanel
     */
    public ColorToolsPanelUI(ColorToolsPanel colorToolsPanel)
    {
        this.colorToolsPanel = colorToolsPanel;
    }
    
    @Override
    public void installUI(JComponent c)
    {
        c.removeAll();

        this.colorToolsPanel.setBackground(ThemeManager.getInstance().getTheme().getSidebarElementBackgroundColor());


        this.colorToolsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        this.colorToolsPanel.add(getPanel());

    }
    
    
    /**
     * @return the main panel
     */
    private JPanel getPanel()
    {
        if (this.panel == null)
        {
            this.panel = new JPanel();
            this.panel.setOpaque(false);
            this.panel.setBorder(new EmptyBorder(0, 5, 0, 0));
            GridLayout layout = new GridLayout(0, 5, 15, 10);
            this.panel.setLayout(layout);
        }
        return this.panel;
    }

    /**
     * Component(s panel
     */
    private JPanel panel;

    private ColorToolsPanel colorToolsPanel;
}
