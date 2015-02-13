package com.horstmann.violet.workspace.sidebar.colortools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
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
            ButtonGroup bg = new ButtonGroup();
            for (ColorChoice aChoice : ColorToolsPanel.CHOICE_LIST)
            {
                JButton colorButton = getColorButton(aChoice);
                bg.add(colorButton);
                this.panel.add(colorButton);
            }
        }
        return this.panel;
    }

    private JButton getColorButton(final ColorChoice colorChoice)
    {
        JButton button = new JButton();
        Icon icon = new Icon()
        {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y)
            {
                // SEE : http://swing124.rssing.com/browser.php?indx=20669962&item=17
                int w = c.getWidth();
                int h = c.getHeight();

                Container parent = c.getParent();
                if (parent == null)
                {
                    return;
                }
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setPaint(c.getBackground());
                Color oldColor = g2.getColor();
                g2.setColor(colorChoice.getBackgroundColor());
                g2.fillRect(x, y, w, h);
                g2.setColor(colorChoice.getBorderColor());
                g2.drawRect(x, y, w, h);
                if (c instanceof AbstractButton)
                {
                    ButtonModel m = ((AbstractButton) c).getModel();
                    if (m.isSelected() || m.isRollover())
                    {
                        g2.setColor(Color.BLACK);
                        g2.drawRect(x, y, w, h);
                    }
                }
                g2.setColor(oldColor);
                g2.dispose();
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
        button.setIcon(icon);
        //button.setSize(20, 20);
        button.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                colorToolsPanel.setColorChoice(colorChoice);
                System.out.println(colorChoice.getBackgroundColor());
            }
        });
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setFocusPainted(true);
        button.setForeground(Color.WHITE);
        return button;
    }

    /**
     * Component(s panel
     */
    private JPanel panel;

    private ColorToolsPanel colorToolsPanel;
}
