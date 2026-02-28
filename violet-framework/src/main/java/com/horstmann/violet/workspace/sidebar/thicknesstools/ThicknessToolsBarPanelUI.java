package com.horstmann.violet.workspace.sidebar.thicknesstools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.PanelUI;

import com.horstmann.violet.framework.theme.ThemeManager;

public class ThicknessToolsBarPanelUI extends PanelUI
{

    public ThicknessToolsBarPanelUI(ThicknessToolsBarPanel thicknessToolsPanel)
    {
        this.thicknessToolsPanel = thicknessToolsPanel;
    }

    @Override
    public void installUI(JComponent c)
    {
        c.removeAll();
        this.thicknessToolsPanel.setBackground(ThemeManager.getInstance().getTheme().getSidebarElementBackgroundColor());
        this.thicknessToolsPanel.add(getPanel());
    }

    private JPanel getPanel()
    {
        if (this.panel == null)
        {
            this.panel = new JPanel();
            this.panel.setOpaque(false);
            this.panel.setBorder(new EmptyBorder(0, 0, 0, 0));
            this.panel.setPreferredSize(new Dimension(215, 30));
            FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 6, 5);
            this.panel.setLayout(layout);
            for (ThicknessTool aTool : getThicknessToolList())
            {
                this.panel.add(aTool);
            }
        }
        return this.panel;
    }

    private List<ThicknessTool> getThicknessToolList()
    {
        if (this.thicknessToolList == null)
        {
            this.thicknessToolList = new ArrayList<ThicknessTool>();
            for (int thickness = ThicknessToolsBarPanel.MIN_THICKNESS; thickness <= ThicknessToolsBarPanel.MAX_THICKNESS; thickness++)
            {
                ThicknessTool aTool = getThicknessTool(thickness);
                this.thicknessToolList.add(aTool);
            }
        }
        return this.thicknessToolList;
    }

    private ThicknessTool getThicknessTool(final int thickness)
    {
        final ThicknessTool aTool = new ThicknessTool(thickness);
        aTool.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                aTool.setHovered(true);
                aTool.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                aTool.setHovered(false);
                aTool.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
                thicknessToolsPanel.fireThicknessChanged(thickness);
            }
        });
        return aTool;
    }

    private class ThicknessTool extends JLabel
    {
        private static final int TOOL_HEIGHT = 16;
        private static final int TOOL_MAX_WIDTH = 16;

        public ThicknessTool(int thickness)
        {
            this.thickness = thickness;
            setPreferredSize(new Dimension(TOOL_MAX_WIDTH, TOOL_HEIGHT));
        }

        @Override
        public void paint(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            Color oldColor = g2.getColor();

            // Center the vertical bar horizontally
            int barWidth = this.thickness;
            int barHeight = TOOL_HEIGHT;
            int x = (getWidth() - barWidth) / 2;
            int y = 0;

            // Draw the vertical bar (black)
            g2.setColor(Color.BLACK);
            g2.fillRect(x, y, barWidth, barHeight);

            // Draw hover border
            if (this.isHovered)
            {
                g2.setColor(Color.GRAY);
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }

            g2.setColor(oldColor);
        }

        public void setHovered(boolean isHovered)
        {
            this.isHovered = isHovered;
        }

        private boolean isHovered = false;
        private int thickness;
    }

    private JPanel panel;
    private ThicknessToolsBarPanel thicknessToolsPanel;
    private List<ThicknessTool> thicknessToolList;
}
