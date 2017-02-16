package com.horstmann.violet.application.swingextension;

import com.horstmann.violet.application.gui.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listener which hide sidebar
 */
public class SideBarHideListener implements ActionListener {
    private MainFrame mainFrame;
    private JButton button;

    public SideBarHideListener(MainFrame mainFrame, JButton button) {
        this.mainFrame = mainFrame;
        this.button = button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final JScrollPane scrollableSideBar = mainFrame.getActiveWorkspace().getAWTComponent().getScrollableSideBar();
        if (scrollableSideBar.isVisible()) {
            scrollableSideBar.setVisible(false);
            button.setText(">");
        } else {
            scrollableSideBar.setVisible(true);
            button.setText("<");
        }

        mainFrame.repaint();
    }
}
