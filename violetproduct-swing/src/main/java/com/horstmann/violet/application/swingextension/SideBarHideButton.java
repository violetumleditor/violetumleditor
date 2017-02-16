package com.horstmann.violet.application.swingextension;

import com.horstmann.violet.application.gui.MainFrame;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Button which hides Sidebar
 */
public class SideBarHideButton {
    private static final int FONT_SIZE = 20;
    private static final int PREFERRED_WIDTH = 20;
    private static final int PREFERRED_HEIGHT = 0;
    private static final int BORDER_THICKNESS = 1;
    private static final String FONT_NAME = "Serif";
    private static final Font FONT = new Font(FONT_NAME, Font.BOLD, FONT_SIZE);
    private static final Color BACKGROUND_COLOR = new Color(180, 80, 204);
    private static final Dimension PREFERRED_SIZE = new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);
    private static final LineBorder BORDER = new LineBorder(Color.BLACK, BORDER_THICKNESS);
    private MainFrame mainFrame;

    public SideBarHideButton(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * Creates button which hides the sidebar
     *
     * @return the JButton instance
     */
    public JButton createSideBarHideButton() {
        JButton button = new JButton("<");
        button.setPreferredSize(PREFERRED_SIZE);
        button.setFont(FONT);
        button.setBackground(BACKGROUND_COLOR);
        button.setForeground(Color.BLACK);
        button.setBorder(BORDER);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setVisible(false);
        button.setBorder(createBorder());
        button.addActionListener(new SideBarHideListener(mainFrame,button));
        return button;
    }

    /**
     * creates border
     *
     * @return the CompoundBorder instance
     */
    private CompoundBorder createBorder() {
        final int top = 0;
        final int left = 0;
        final int bottom = 0;
        final int right = 1;
        return new CompoundBorder(
                BorderFactory.createMatteBorder(top, left, bottom, right, Color.darkGray),
                BorderFactory.createMatteBorder(top, left, bottom, right, Color.GRAY));
    }

}
