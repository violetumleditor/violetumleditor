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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.border.EmptyBorder;

import com.horstmann.violet.application.menu.FileMenu;
import com.horstmann.violet.application.swingextension.WelcomeButtonUI;
import com.horstmann.violet.framework.file.GraphFile;
import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.LocalFile;
import com.horstmann.violet.framework.file.export.FileExportService;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.framework.plugin.PluginRegistry;
import com.horstmann.violet.framework.swingextension.FadeImage;
import com.horstmann.violet.framework.theme.ITheme;
import com.horstmann.violet.framework.theme.ThemeManager;
import com.horstmann.violet.product.diagram.abstracts.IGraph;

public class WelcomePanelOld extends JPanel
{

    public WelcomePanelOld(FileMenu fileMenu)
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
        shortcutPanel.add(getLeftPanel(), new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        shortcutPanel.add(getRightPanel(), new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
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
                if (isSubMenu)
                {
                    JMenu subMenu = (JMenu) item;
                    String label = subMenu.getText();
                    JLabel title = new JLabel(label.toLowerCase());
                    ITheme cLAF = ThemeManager.getInstance().getTheme();
                    title.setFont(cLAF.getWelcomeBigFont());
                    title.setForeground(cLAF.getWelcomeBackgroundEndColor());
                    title.setBorder(new EmptyBorder(0, 30, 0, 0));
                    title.setAlignmentX(Component.RIGHT_ALIGNMENT);
                    leftPanel.add(title);
                    for (int j = 0; j < subMenu.getItemCount(); j++)
                    {
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
                        Icon sampleDiagramIcon = subItem.getRolloverIcon();
                        if (sampleDiagramIcon != null)
                        {
                            final FadeImage fadeImage = new FadeImage((ImageIcon) sampleDiagramIcon);
                            getRightPanel().add(fadeImage);
                            newDiagramShortcut.addMouseListener(new MouseAdapter()
                            {
                                @Override
                                public void mouseEntered(MouseEvent e)
                                {
                                    getWelcomeDiagramImage().fadeOut();
                                    fadeImage.fadeIn();
                                };

                                @Override
                                public void mouseExited(MouseEvent e)
                                {
                                    fadeImage.fadeOut();
                                    getWelcomeDiagramImage().fadeIn();
                                };
                            });
                        }
                        leftPanel.add(newDiagramShortcut);
                    }
                    JPanel separator = new JPanel();
                    separator.setBorder(new EmptyBorder(0, 0, 0, 0));
                    separator.setPreferredSize(new Dimension(10, 20));
                    separator.setOpaque(false);
                    leftPanel.add(separator);
                }
                if (!isSubMenu)
                {
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
            this.rightPanel.setDoubleBuffered(true);
            LayoutManager overlay = new OverlayLayout(this.rightPanel);
            this.rightPanel.setLayout(overlay);
            this.rightPanel.add(getWelcomeDiagramImage());
            getWelcomeDiagramImage().fadeIn();
        }
        return this.rightPanel;
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

    /**
     * @return an image exported from the welcome diagram file
     */
    private FadeImage getWelcomeDiagramImage()
    {
        if (this.welcomeDiagramImage == null)
        {
            try
            {
                URL resource = getClass().getResource("Welcome.activity.violet.html");
                File file = new File(resource.getFile());
                if (!file.exists())
                {
                    return null;
                }
                if (!file.isFile())
                {
                    return null;
                }
                IFile aFile = new LocalFile(file);
                GraphFile graphFile = new GraphFile(aFile);
                IGraph graph = graphFile.getGraph();
                BufferedImage image = FileExportService.getImage(graph);

                JLabel label = new JLabel();
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setVerticalAlignment(JLabel.CENTER);
                label.setIcon(new ImageIcon(image));
                label.setSize(new Dimension(600, 550));
                label.setBackground(Color.WHITE);
                label.setOpaque(true);
                Dimension size = label.getSize();
                BufferedImage image2 = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = image2.createGraphics();
                label.paint(g2);
                this.welcomeDiagramImage = new FadeImage(new ImageIcon(image2));
            }
            catch (Exception e)
            {
                // Failed to load sample. It doesn"t matter.
            }
        }
        return this.welcomeDiagramImage;
    }

    private JPanel footTextPanel;;

    private JPanel leftPanel;

    private JPanel rightPanel;

    private FileMenu fileMenu;

    private FadeImage welcomeDiagramImage;

    @ResourceBundleBean(key = "welcomepanel.foot_text")
    private String footText;

    /** Plugin registry */
    @InjectedBean
    private PluginRegistry pluginRegistry;

}
