package com.horstmann.violet.workspace.sidebar.alignedtools;

import com.horstmann.violet.framework.swingextension.IconButtonUI;
import com.horstmann.violet.framework.theme.ThemeManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.PanelUI;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class AlignedToolsPanelUI extends PanelUI {

    public AlignedToolsPanelUI(AlignedToolsPanel alignedToolsPanel) {
        if(alignedToolsPanel !=null){
            this.alignedToolsPanel = alignedToolsPanel;
        }else {
            this.alignedToolsPanel = new AlignedToolsPanel();
        }
    }

    private JPanel getPanel()
    {
        if (this.panel == null)
        {
            this.panel = new JPanel();
            this.panel.setBorder(new EmptyBorder(UP_BORDER, LEFT_BORDER, DOWN_BORDER,RIGHT_BORDER));
            this.panel.setOpaque(false);
            GridLayout layout = new GridLayout(ROWS_LAYOUT, COLS_LAYOUT, HORIZONTAL_LAYOUT, VERTICAL_LAYOUT);
            this.panel.setLayout(layout);
        }
        return this.panel;
    }

    private void addButtons(ArrayList<JButton> buttons)
    {
        for (JButton button:buttons) {
            if(button == null) continue;
            button.setUI(new IconButtonUI());
            getPanel().add(button);
        }
    }

    @Override
    public void installUI(JComponent c) {
        c.removeAll();
        this.alignedToolsPanel.setBackground(ThemeManager.getInstance().getTheme().getSidebarElementBackgroundColor());
        JButton alignLeftButton = this.alignedToolsPanel.getAlignLeftButton();
        JButton alignUpButton = this.alignedToolsPanel.getAlignUpButton();
        JButton alignDownButton = this.alignedToolsPanel.getAlignDownButton();
        JButton alignRightButton = this.alignedToolsPanel.getAlignRightButton();

        listButtons.addAll(Arrays.asList(alignLeftButton,alignUpButton,alignDownButton,alignRightButton));
        addButtons(listButtons);

        this.alignedToolsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, HORIZONTAL_FLOWLAYOUT, VERTICAL_FLOWLAYOUT));
        this.alignedToolsPanel.add(getPanel());
    }

    //region finals
    private static final int UP_BORDER = 0;
    private static final int DOWN_BORDER = 0;
    private static final int LEFT_BORDER = 5;
    private static final int RIGHT_BORDER = 0;
    private static final int ROWS_LAYOUT = 0;
    private static final int COLS_LAYOUT = 5;
    private static final int HORIZONTAL_LAYOUT = 15;
    private static final int VERTICAL_LAYOUT = 10;
    private static final int VERTICAL_FLOWLAYOUT = 0;
    private static final int HORIZONTAL_FLOWLAYOUT = 0;
    //endregion

    private AlignedToolsPanel alignedToolsPanel;
    private JPanel panel;
    private ArrayList<JButton> listButtons = new ArrayList<>();
}
