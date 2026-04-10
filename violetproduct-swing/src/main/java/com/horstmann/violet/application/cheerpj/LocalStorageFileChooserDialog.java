package com.horstmann.violet.application.cheerpj;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Custom dialog for selecting and deleting files from localStorage in CheerpJ mode.
 * Shows a list of files with delete buttons on each row.
 */
public class LocalStorageFileChooserDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private String selectedFile = null;
    private final String[] filenames;
    private JList<String> fileList;
    private JButton openButton;
    private JButton cancelButton;
    private DefaultListModel<String> listModel;

    public LocalStorageFileChooserDialog(Frame parent, String[] filenames) {
        super(parent, "Open diagram from LocalStorage", true);
        this.filenames = filenames != null ? filenames.clone() : new String[0];
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getOwner());
        setMinimumSize(new Dimension(500, 400));

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Instructions label
        JLabel instructionLabel = new JLabel("Select a file to open or click the trash icon to delete:");
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        mainPanel.add(instructionLabel, BorderLayout.NORTH);

        // File list with custom renderer
        listModel = new DefaultListModel<>();
        for (String filename : filenames) {
            listModel.addElement(filename);
        }

        fileList = new JList<>(listModel);
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileList.setCellRenderer(new FileListCellRenderer());
        fileList.setFixedCellHeight(32);

        JScrollPane scrollPane = new JScrollPane(fileList);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIManager.getColor("controlShadow")));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        openButton = new JButton("Open");
        cancelButton = new JButton("Cancel");

        openButton.addActionListener(e -> openSelected());
        cancelButton.addActionListener(e -> {
            selectedFile = null;
            setVisible(false);
        });

        buttonPanel.add(openButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void openSelected() {
        int selectedIndex = fileList.getSelectedIndex();
        if (selectedIndex >= 0) {
            selectedFile = listModel.getElementAt(selectedIndex);
            setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a file", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    public String getSelectedFile() {
        return selectedFile;
    }

    public void deleteFile(String filename) {
        int index = listModel.indexOf(filename);
        if (index >= 0) {
            listModel.removeElementAt(index);
        }
    }

    /**
     * Custom cell renderer that displays filename with a delete button.
     */
    private class FileListCellRenderer extends JPanel implements ListCellRenderer<String> {
        private static final long serialVersionUID = 1L;

        private JLabel filenameLabel;
        private JButton deleteButton;
        private String currentFilename;

        public FileListCellRenderer() {
            setLayout(new BorderLayout(5, 0));
            setOpaque(true);

            filenameLabel = new JLabel();
            add(filenameLabel, BorderLayout.CENTER);

            deleteButton = new JButton("✕");
            deleteButton.setPreferredSize(new Dimension(32, 24));
            deleteButton.setMargin(new Insets(0, 0, 0, 0));
            deleteButton.setFont(deleteButton.getFont().deriveFont(Font.BOLD, 12f));
            deleteButton.setForeground(Color.RED);
            deleteButton.setToolTipText("Delete this file");
            deleteButton.addActionListener(e -> deleteCurrentFile());
            add(deleteButton, BorderLayout.EAST);

            setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        }

        private void deleteCurrentFile() {
            if (currentFilename != null) {
                int option = JOptionPane.showConfirmDialog(
                    LocalStorageFileChooserDialog.this,
                    "Are you sure you want to delete \"" + currentFilename + "\"?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );

                if (option == JOptionPane.YES_OPTION) {
                    try {
                        boolean deleted = CheerpJInterfaceService.deleteLocalStorageDiagram(currentFilename);
                        if (deleted) {
                            deleteFile(currentFilename);
                            JOptionPane.showMessageDialog(
                                LocalStorageFileChooserDialog.this,
                                "File deleted successfully.",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                        } else {
                            JOptionPane.showMessageDialog(
                                LocalStorageFileChooserDialog.this,
                                "Failed to delete the file.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(
                            LocalStorageFileChooserDialog.this,
                            "Error deleting file: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        }

        @Override
        public Component getListCellRendererComponent(
            JList<? extends String> list,
            String value,
            int index,
            boolean isSelected,
            boolean cellHasFocus
        ) {
            currentFilename = value;
            filenameLabel.setText(value != null ? value : "");

            if (isSelected) {
                setBackground(UIManager.getColor("List.selectionBackground"));
                setForeground(UIManager.getColor("List.selectionForeground"));
                filenameLabel.setForeground(UIManager.getColor("List.selectionForeground"));
            } else {
                setBackground(UIManager.getColor("List.background"));
                setForeground(UIManager.getColor("List.foreground"));
                filenameLabel.setForeground(UIManager.getColor("List.foreground"));
            }

            return this;
        }
    }
}
