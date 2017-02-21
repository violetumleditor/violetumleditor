package com.horstmann.violet.workspace.sidebar.graphtools;

import com.horstmann.violet.framework.theme.ThemeManager;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.plaf.PanelUI;

public class GraphToolsBarPanelUI extends PanelUI
{

    @Override
    public void installUI(JComponent c)
    {
        GraphToolsBarPanel panel = (GraphToolsBarPanel) c;
        panel.removeAll();
        panel.setBackground(ThemeManager.getInstance().getTheme().getSidebarElementBackgroundColor());
        for (GraphToolsBarButton button : panel.getNodeButtons()) {
            button.setTextVisible(true);
        }
        for (GraphToolsBarButton button : panel.getEdgeButtons()) {
            button.setTextVisible(true);
        }
        JPanel nodeButtonsPanel = panel.getNodeButtonsPanel();
        JPanel edgeButtonsPanel = panel.getEdgeButtonsPanel();
        panel.setLayout(new BorderLayout());
        panel.add(nodeButtonsPanel, BorderLayout.NORTH);
        panel.add(edgeButtonsPanel, BorderLayout.SOUTH);
    }
    
}
