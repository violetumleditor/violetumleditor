/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.application.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.horstmann.violet.application.menu.FileMenu;
import com.horstmann.violet.application.swingextension.WelcomeButtonUI;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.framework.plugin.PluginRegistry;
import com.horstmann.violet.framework.theme.ITheme;
import com.horstmann.violet.framework.theme.ThemeManager;

public class WelcomePanel extends JPanel
{

    public WelcomePanel(FileMenu fileMenu)
    {
        ResourceBundleInjector.getInjector().inject(this);
        BeanInjector.getInjector().inject(this);
        this.fileMenu = fileMenu;
       
        setOpaque(false);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);

        JPanel shortcutPanel = new JPanel();
        shortcutPanel.setOpaque(false);
        shortcutPanel.setLayout(new GridBagLayout());
        shortcutPanel.add(getLeftTitlePanel(), new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        shortcutPanel.add(getRightTitlePanel(), new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        shortcutPanel.add(getLeftPanel(), new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        shortcutPanel.add(getRightPanel(), new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(shortcutPanel, c);

        add(panel, BorderLayout.CENTER);
        add(getFootTextPanel(), BorderLayout.SOUTH);

    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        Paint currentPaint = g2.getPaint();
        ITheme cLAF = ThemeManager.getInstance().getTheme();
        GradientPaint paint = new GradientPaint(getWidth() / 2, -getHeight() / 4, cLAF.getWelcomeBackgroundStartColor(),
                getWidth() / 2, getHeight() + getHeight() / 4, cLAF.getWelcomeBackgroundEndColor());
        g2.setPaint(paint);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setPaint(currentPaint);
        super.paint(g);
    }

    private JPanel getLeftPanel()
    {
        if (this.leftPanel == null)
        {
            leftPanel = new JPanel();
            leftPanel.setOpaque(false);
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            leftPanel.setBorder(new EmptyBorder(0, 0, 0, 45));

            final JMenu newMenu = this.fileMenu.getFileNewMenu();
            for (int i = 0; i < newMenu.getItemCount(); i++)
            {
                final JMenuItem item = newMenu.getItem(i);
                boolean isSubMenu = JMenu.class.isInstance(item);
				if (isSubMenu) {
					JMenu subMenu = (JMenu) item;
					String label = subMenu.getText();
					JLabel title = new JLabel(label.toLowerCase());
		            ITheme cLAF = ThemeManager.getInstance().getTheme();
		            title.setFont(cLAF.getWelcomeBigFont());
		            title.setForeground(cLAF.getWelcomeBackgroundEndColor());
		            title.setBorder(new EmptyBorder(0, 30, 0, 0));
		            title.setAlignmentX(Component.RIGHT_ALIGNMENT);
					leftPanel.add(title);
					for (int j = 0; j < subMenu.getItemCount(); j++) {
						final JMenuItem subItem = subMenu.getItem(j);
						String subLabel = subItem.getText();
						JButton newDiagramShortcut = new JButton(subLabel.toLowerCase());
						newDiagramShortcut.setUI(new WelcomeButtonUI());
						newDiagramShortcut.setAlignmentX(Component.RIGHT_ALIGNMENT);
						newDiagramShortcut.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent e)
							{
								subItem.doClick();
							}
						});
						leftPanel.add(newDiagramShortcut);
					}
					JPanel separator = new JPanel();
					separator.setBorder(new EmptyBorder(0, 0, 0, 0));
					separator.setPreferredSize(new Dimension(10, 20));
					separator.setOpaque(false);
					leftPanel.add(separator);
                }
				if (!isSubMenu) {
					String label = item.getText();
					JButton newDiagramShortcut = new JButton(label.toLowerCase());
					newDiagramShortcut.setUI(new WelcomeButtonUI());
					newDiagramShortcut.setAlignmentX(Component.RIGHT_ALIGNMENT);
					newDiagramShortcut.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							item.doClick();
						}
					});
					leftPanel.add(newDiagramShortcut);
				}
            }
        }
        return this.leftPanel;
    }

    private JPanel getRightPanel()
    {
        if (this.rightPanel == null)
        {
            this.rightPanel = new JPanel();
            this.rightPanel.setOpaque(false);
            this.rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            this.rightPanel.setBorder(new EmptyBorder(0, 45, 0, 45));

            final JMenu recentMenu = this.fileMenu.getFileRecentMenu();
            for (int i = 0; i < recentMenu.getItemCount(); i++)
            {
                final JMenuItem item = recentMenu.getItem(i);
                String label = item.getText();
                JButton fileShortcut = new JButton(label.toLowerCase());
                fileShortcut.setUI(new WelcomeButtonUI());
                fileShortcut.setAlignmentX(Component.LEFT_ALIGNMENT);
                fileShortcut.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        item.doClick();
                    }
                });
                rightPanel.add(fileShortcut);
            }

        }
        return this.rightPanel;
    }

    private JPanel getLeftTitlePanel()
    {
        if (this.leftTitlePanel == null)
        {
            JLabel icon = new JLabel();
            icon.setIcon(this.leftPanelIcon);

            JLabel title = new JLabel(this.fileMenu.getFileNewMenu().getText().toLowerCase());
            ITheme cLAF = ThemeManager.getInstance().getTheme();
            title.setFont(cLAF.getWelcomeBigFont());
            title.setForeground(cLAF.getWelcomeBigForegroundColor());
            title.setBorder(new EmptyBorder(0, 30, 0, 0));

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.add(icon);
            panel.add(title);
            panel.setOpaque(false);

            this.leftTitlePanel = new JPanel();
            this.leftTitlePanel.setOpaque(false);
            this.leftTitlePanel.setLayout(new BorderLayout());
            this.leftTitlePanel.add(panel, BorderLayout.EAST);
            this.leftTitlePanel.setBorder(new EmptyBorder(0, 0, 30, 45));
        }
        return this.leftTitlePanel;
    }

    private JPanel getRightTitlePanel()
    {
        if (this.rightTitlePanel == null)
        {
            JLabel icon = new JLabel();
            icon.setIcon(this.rightPanelIcon);
            icon.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel title = new JLabel(this.fileMenu.getFileRecentMenu().getText().toLowerCase());
            ITheme cLAF = ThemeManager.getInstance().getTheme();
            title.setFont(cLAF.getWelcomeBigFont());
            title.setForeground(cLAF.getWelcomeBigForegroundColor());
            title.setBorder(new EmptyBorder(0, 0, 0, 30));

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.add(title);
            panel.add(icon);
            panel.setOpaque(false);

            this.rightTitlePanel = new JPanel();
            this.rightTitlePanel.setOpaque(false);
            this.rightTitlePanel.setLayout(new BorderLayout());
            this.rightTitlePanel.add(panel, BorderLayout.WEST);
            this.rightTitlePanel.setBorder(new EmptyBorder(0, 45, 30, 0));
        }
        return this.rightTitlePanel;
    }

    private JPanel getFootTextPanel()
    {
        if (this.footTextPanel == null)
        {
            this.footTextPanel = new JPanel();
            this.footTextPanel.setOpaque(false);
            this.footTextPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
            this.footTextPanel.setLayout(new BoxLayout(this.footTextPanel, BoxLayout.Y_AXIS));
            this.footTextPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel text = new JLabel(this.footText);
            ITheme cLAF = ThemeManager.getInstance().getTheme();
            text.setFont(cLAF.getWelcomeSmallFont());
            text.setForeground(cLAF.getWelcomeBigForegroundColor());
            text.setAlignmentX(Component.CENTER_ALIGNMENT);

            this.footTextPanel.add(text);
        }

        return this.footTextPanel;
    }

    private JPanel footTextPanel;;

    private JPanel rightTitlePanel;

    private JPanel leftTitlePanel;

    private JPanel leftPanel;

    private JPanel rightPanel;

    private FileMenu fileMenu;

    @ResourceBundleBean(key="welcomepanel.new_diagram.icon")
    private ImageIcon leftPanelIcon;

    @ResourceBundleBean(key="welcomepanel.recent_files.icon")
    private ImageIcon rightPanelIcon;

    @ResourceBundleBean(key="welcomepanel.foot_text")
    private String footText;
    
    /** Plugin registry */
    @InjectedBean
    private PluginRegistry pluginRegistry;


}
