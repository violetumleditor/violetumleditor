package com.horstmann.violet.workspace.sidebar.colortools;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
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
            FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 10, 10);
            this.panel.setLayout(layout);
        }
        return this.panel;
    }
    
    private JToggleButton getColorButton(Color color) {
        JToggleButton button = new JToggleButton();
        Icon icon = new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y)
            {
                // SEE : http://swing124.rssing.com/browser.php?indx=20669962&item=17
                
            }
            @Override
            public int getIconWidth()
            {
                return 20;
            }

            @Override
            public int getIconHeight()
            {
                return 20;
            }
        };
        return button;
    }
    

    /**
     * Component(s panel
     */
    private JPanel panel;

    private ColorToolsPanel colorToolsPanel;
}
