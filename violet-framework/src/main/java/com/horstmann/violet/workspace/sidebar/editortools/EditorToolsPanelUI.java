package com.horstmann.violet.workspace.sidebar.editortools;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.PanelUI;

import com.horstmann.violet.framework.swingextension.IconButtonUI;
import com.horstmann.violet.framework.theme.ThemeManager;

/**
 * UI for displaying a large EditorToolsPanel
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class EditorToolsPanelUI extends PanelUI
{

    /**
     * Default constructor
     * 
     * @param editorToolsPanel
     */
    public EditorToolsPanelUI(EditorToolsPanel editorToolsPanel)
    {
        this.editorToolsPanel = editorToolsPanel;
    }

    @Override
    public void installUI(JComponent c)
    {
        c.removeAll();
        c.setBackground(ThemeManager.getInstance().getTheme().getSidebarElementBackgroundColor());

        this.editorToolsPanel.getZoomInButton().setUI(new IconButtonUI(FULLSIZE_SCALING_FACTOR));
        this.editorToolsPanel.getZoomOutButton().setUI(new IconButtonUI(FULLSIZE_SCALING_FACTOR));
        this.editorToolsPanel.getDeleteButton().setUI(new IconButtonUI(FULLSIZE_SCALING_FACTOR));
        this.editorToolsPanel.getUndoButton().setUI(new IconButtonUI(FULLSIZE_SCALING_FACTOR));
        this.editorToolsPanel.getRedoButton().setUI(new IconButtonUI(FULLSIZE_SCALING_FACTOR));
        this.editorToolsPanel.getCutButton().setUI(new IconButtonUI(FULLSIZE_SCALING_FACTOR));
        this.editorToolsPanel.getCopyButton().setUI(new IconButtonUI(FULLSIZE_SCALING_FACTOR));
        this.editorToolsPanel.getPasteButton().setUI(new IconButtonUI(FULLSIZE_SCALING_FACTOR));

        c.setLayout(new FlowLayout(FlowLayout.CENTER));
        c.add(getToolsPanel());
    }

    /**
     * @return the main panel
     */
    private JPanel getToolsPanel()
    {
        if (this.toolsPanel == null)
        {
            this.toolsPanel = new JPanel();
            this.toolsPanel.setOpaque(false);
            this.toolsPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
            this.toolsPanel.add(this.editorToolsPanel.getUndoButton());
            this.toolsPanel.add(this.editorToolsPanel.getZoomInButton());
            this.toolsPanel.add(this.editorToolsPanel.getZoomOutButton());
            this.toolsPanel.add(this.editorToolsPanel.getDeleteButton());
            this.toolsPanel.add(this.editorToolsPanel.getRedoButton());
            this.toolsPanel.add(this.editorToolsPanel.getCutButton());
            this.toolsPanel.add(this.editorToolsPanel.getCopyButton());
            this.toolsPanel.add(this.editorToolsPanel.getPasteButton());

            GridBagLayout layout = new GridBagLayout();
            this.toolsPanel.setLayout(layout);

            GridBagConstraints c1 = new GridBagConstraints();
            c1.anchor = GridBagConstraints.CENTER;
            c1.insets = new Insets(0, 0, 5, 15);
            c1.gridx = 0;
            c1.gridy = 0;
            layout.setConstraints(this.editorToolsPanel.getUndoButton(), c1);

            GridBagConstraints c2 = new GridBagConstraints();
            c2.anchor = GridBagConstraints.CENTER;
            c2.insets = new Insets(0, 0, 5, 15);
            c2.gridx = 1;
            c2.gridy = 0;
            layout.setConstraints(this.editorToolsPanel.getZoomInButton(), c2);

            GridBagConstraints c3 = new GridBagConstraints();
            c3.anchor = GridBagConstraints.CENTER;
            c3.insets = new Insets(0, 0, 5, 15);
            c3.gridx = 2;
            c3.gridy = 0;
            layout.setConstraints(this.editorToolsPanel.getZoomOutButton(), c3);

            GridBagConstraints c4 = new GridBagConstraints();
            c4.anchor = GridBagConstraints.CENTER;
            c4.insets = new Insets(0, 0, 5, 15);
            c4.gridx = 3;
            c4.gridy = 0;
            layout.setConstraints(this.editorToolsPanel.getDeleteButton(), c4);

            GridBagConstraints c5 = new GridBagConstraints();
            c5.anchor = GridBagConstraints.CENTER;
            c5.insets = new Insets(0, 0, 5, 0);
            c5.gridx = 4;
            c5.gridy = 0;
            layout.setConstraints(this.editorToolsPanel.getRedoButton(), c5);

            GridBagConstraints c6 = new GridBagConstraints();
            c6.anchor = GridBagConstraints.CENTER;
            c6.insets = new Insets(0, 0, 0, 15);
            c6.gridx = 1;
            c6.gridy = 1;
            layout.setConstraints(this.editorToolsPanel.getCutButton(), c6);

            GridBagConstraints c7 = new GridBagConstraints();
            c7.insets = new Insets(0, 0, 0, 15);
            c7.weightx = 1;
            c7.gridx = 2;
            c7.gridy = 1;
            layout.setConstraints(this.editorToolsPanel.getCopyButton(), c7);

            GridBagConstraints c8 = new GridBagConstraints();
            c8.insets = new Insets(0, 0, 0, 15);
            c8.weightx = 1;
            c8.gridx = 3;
            c8.gridy = 1;
            layout.setConstraints(this.editorToolsPanel.getPasteButton(), c8);
        }
        return this.toolsPanel;
    }

    /**
     * Full size icon scaling factor
     */
    private static final double FULLSIZE_SCALING_FACTOR = 1;

    /**
     * Panel containing tools
     */
    private JPanel toolsPanel;

    /**
     * Main panel
     */
    private EditorToolsPanel editorToolsPanel;

}
