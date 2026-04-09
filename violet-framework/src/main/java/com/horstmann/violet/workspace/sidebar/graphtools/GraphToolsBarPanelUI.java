package com.horstmann.violet.workspace.sidebar.graphtools;

import javax.swing.JComponent;
import javax.swing.plaf.PanelUI;

import com.horstmann.violet.framework.theme.ThemeManager;

public class GraphToolsBarPanelUI extends PanelUI
{

    @Override
    public void installUI(JComponent c)
    {
        GraphToolsBarPanel panel = (GraphToolsBarPanel) c;
        panel.setBackground(ThemeManager.getInstance().getTheme().getSidebarElementBackgroundColor());
        panel.getButtonsPanel().setBackground(ThemeManager.getInstance().getTheme().getSidebarElementBackgroundColor());
        for (GraphToolsBarButton button : panel.getNodeButtons())
        {
            button.setTextVisible(true);
        }
        for (GraphToolsBarButton button : panel.getEdgeButtons())
        {
            button.setTextVisible(true);
        }
    }

}
