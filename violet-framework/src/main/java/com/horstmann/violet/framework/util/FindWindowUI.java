package com.horstmann.violet.framework.util;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Created by Bartosz Śledź on 17.01.2017.
 */
public class FindWindowUI {

    private final ResourceBundle bundle = ResourceBundle.getBundle("properties.OtherStrings", Locale.getDefault());
    private static final int WIDTH = 400;
    private static final int HEIGHT = 70;
    private static final String EMPTY_STRING = "";
    private static final String FIND_BUNDLE = "find.text";
    private static final String START_VALUE = "---/---";
    private static final String PATH_TO_FIND_PNG = "violet-framework/src/main/resources/icons/16x16/violet.png";
    private JFrame findFrame;
    private JPanel panel;
    private JButton findBtn;
    private JTextField inputField;
    private JLabel numberOfFound;

    public FindWindowUI() {
        initialize();
    }

    private void initialize() {
        initializeFrame();
        initializePanel();
        initializeTextFields();
        initializeLabels();
        initializeButtons();
        pack();
    }

    private void initializeLabels() {
        numberOfFound = new JLabel();
        panel.add(numberOfFound);
        setNumberOfFound(START_VALUE);
    }

    private void initializeFrame() {
        findFrame = new JFrame();
        findFrame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        ImageIcon img = new ImageIcon(PATH_TO_FIND_PNG);
        findFrame.setIconImage(img.getImage());
        findFrame.setTitle(bundle.getString(FIND_BUNDLE));
        findFrame.setAlwaysOnTop(true);
        findFrame.setLocationByPlatform(true);
        findFrame.setVisible(true);
        findFrame.setResizable(false);
        findFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initializePanel() {
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setOpaque(true);
        findFrame.add(panel);
    }

    private void initializeTextFields() {
        inputField = new JTextField(20);
        panel.add(inputField);
    }

    private void initializeButtons() {
        findBtn = new JButton(bundle.getString(FIND_BUNDLE));
        panel.add(findBtn);
    }

    private void pack() {
        findFrame.pack();
    }

    public void addFindButtonActionListener(final ActionListener actionListener) {
        findBtn.addActionListener(actionListener);
    }

    public void addInputFindFieldActionListener(final MouseAdapter mouseAdapter) {
        inputField.addMouseListener(mouseAdapter);
    }

    public void addCloseFrameActionListener(final WindowAdapter windowAdapter) {
        findFrame.addWindowListener(windowAdapter);
    }

    public void disposeWindow() {
        findFrame.dispose();
    }

    public String getTextFromInputField() {
        return inputField.getText().toLowerCase();
    }

    public void clearFindField() {
        inputField.setText(EMPTY_STRING);
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(final JPanel panel) {
        this.panel = panel;
    }

    public void setNumberOfFound(final String numberOfFound) {
        this.numberOfFound.setText(numberOfFound);
    }
}
