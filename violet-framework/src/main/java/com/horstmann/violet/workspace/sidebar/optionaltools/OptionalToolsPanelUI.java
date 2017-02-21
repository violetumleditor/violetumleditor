package com.horstmann.violet.workspace.sidebar.optionaltools;

import com.horstmann.violet.framework.swingextension.IconButtonUI;
import com.horstmann.violet.framework.theme.ThemeManager;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.PanelUI;

/**
 * Large UI for OptionalToolsPanel
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class OptionalToolsPanelUI extends PanelUI
{

    /**
     * Default constructor
     * 
     * @param optionalToolsPanel
     */
    public OptionalToolsPanelUI(OptionalToolsPanel optionalToolsPanel)
    {
        this.optionalToolsPanel = optionalToolsPanel;
    }

    @Override
    public void installUI(JComponent c)
    {
        c.removeAll();

        this.optionalToolsPanel.setBackground(ThemeManager.getInstance().getTheme().getSidebarElementBackgroundColor());

        JButton bHelp = this.optionalToolsPanel.getHelpButton();
        // addButton(bHelp);

        JButton bPrint = this.optionalToolsPanel.getPrintButton();
        addButton(bPrint);

        JButton bExportToClipboard = this.optionalToolsPanel.getExportToClipboardButton();
        addButton(bExportToClipboard);

        this.optionalToolsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        this.optionalToolsPanel.add(getPanel());

    }

    /**
     * Adds a button to the main panel
     * 
     * @param aButton
     */
    private void addButton(JButton aButton)
    {
        aButton.setUI(new IconButtonUI());
        getPanel().add(aButton);
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

    /**
     * Panel we want to construct view
     */
    private OptionalToolsPanel optionalToolsPanel;

}
