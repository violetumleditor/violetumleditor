package com.horstmann.violet.workspace.sidebar.colortools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.PanelUI;

import com.horstmann.violet.framework.swingextension.CustomToggleButton;
import com.horstmann.violet.framework.swingextension.CustomToggleButtonColorToolUI;
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
            for (CustomToggleButton aColorButton : getColorButtons())
            {
                this.panel.add(aColorButton);
            }
        }
        return this.panel;
    }
    
    
    private List<CustomToggleButton> getColorButtons() {
        if (this.colorButtons == null) {
            this.colorButtons = new ArrayList<CustomToggleButton>();
            for (ColorChoice aChoice : ColorToolsPanel.CHOICE_LIST)
            {
                CustomToggleButton colorButton = getColorButton(aChoice);
                this.colorButtons.add(colorButton);
            }
        }
        return this.colorButtons;
    }
    

    private CustomToggleButton getColorButton(final ColorChoice colorChoice)
    {
        final CustomToggleButton button = new CustomToggleButton();
        button.setPreferredSize(new Dimension(20, 20));
        button.setUI(new CustomToggleButtonColorToolUI(colorChoice.getBackgroundColor(), Color.BLACK, colorChoice.getBackgroundColor(), colorChoice.getBackgroundColor()));
        button.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                colorToolsPanel.setColorChoice(colorChoice);
                setSelectedButton(button);
                System.out.println(colorChoice.getBackgroundColor());
            }
        });
        return button;
    }
    
    
    /**
     * Performs button select
     * 
     * @param selectedButton to be considered as selected
     */
    private void setSelectedButton(CustomToggleButton selectedButton)
    {
        for (CustomToggleButton button : getColorButtons())
        {
            if (button != selectedButton)
            {
                button.setSelected(false);
            }
            if (button == selectedButton)
            {
                button.setSelected(true);
            }
        }
    }

    /**
     * Component(s panel
     */
    private JPanel panel;

    private ColorToolsPanel colorToolsPanel;
    
    private List<CustomToggleButton> colorButtons;
}
