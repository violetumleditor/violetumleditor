package com.horstmann.violet.application.gui;

import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import com.horstmann.violet.application.autosave.AutosaveSettings;
import com.horstmann.violet.application.autosave.IAutoSave;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;

public class SettingsDialog extends javax.swing.JDialog
{

	private static final long serialVersionUID = 1L;
	private static final String FORM_NAME = "FormSettings";
	private javax.swing.JButton cancelButton;
    
	
	private javax.swing.JCheckBox enableAutosaveCheckbox;
    private javax.swing.JTextField pathAutosaveTextField ;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel saveDirectoryLabel;
    private javax.swing.JLabel saveIntervalLabel;
    private javax.swing.JTextField saveIntervalTextField;
    private javax.swing.JLabel secondsLabel;
    private javax.swing.JButton selectPathButton;
    private AutosaveSettings autosave;
    
    @ResourceBundleBean(key="dialog.checkbox.enableautosaveText")
    private String enableAutosaveCheckboxText;
    						 
    @ResourceBundleBean(key="dialog.label.saveIntervalText")
    private String saveIntervalLabelText;
    
    @ResourceBundleBean(key="dialog.label.secondsText")
    private String saveIntervalSecondsLabelText;
    
    @ResourceBundleBean(key="dialog.label.saveDirectoryText")
    private String saveDirectoryLabelText;
    
    @ResourceBundleBean(key="dialog.button.selectPathText")
    private String selectPathButtonText;
    
    @ResourceBundleBean(key="dialog.button.okText")
    private String okButtonText;
    
    @ResourceBundleBean(key="dialog.button.cancelText")
    private String cancelButtonText;
    
    @ResourceBundleBean(key="dialog.label.invalidValueText")
    private String invalidValueText;
    
    @ResourceBundleBean(key="dialog.titleText")
    private String titleText;
    
    
	private IAutoSave autosaveListener;
    
    /**
     * Settings dialogue
     * @param parent and modal
     */
    public SettingsDialog(java.awt.Frame parent, boolean modal, IAutoSave autosaveListener)
    {
        super(parent, modal);
        this.autosaveListener = autosaveListener;
        autosave = new AutosaveSettings();
        ResourceBundleInjector.getInjector().inject(this);
        BeanInjector.getInjector().inject(this);
        initComponents();
        loadSettings();
        enableDisableComponents();
    }
    
    /**
     * Enable disable components
     */
    private void enableDisableComponents()
    {
        saveIntervalTextField.setEnabled(enableAutosaveCheckbox.isSelected());
        saveIntervalLabel.setEnabled(enableAutosaveCheckbox.isSelected());
        secondsLabel.setEnabled(enableAutosaveCheckbox.isSelected());
        saveDirectoryLabel.setEnabled(enableAutosaveCheckbox.isSelected());
        pathAutosaveTextField.setEnabled(enableAutosaveCheckbox.isSelected());
        selectPathButton.setEnabled(enableAutosaveCheckbox.isSelected());
    }

    /**Init components
     */
    private void initComponents()
    {
        enableAutosaveCheckbox = new javax.swing.JCheckBox();
        saveIntervalLabel = new javax.swing.JLabel();
        saveIntervalTextField = new javax.swing.JTextField();
        secondsLabel = new javax.swing.JLabel();
        saveDirectoryLabel = new javax.swing.JLabel();
        pathAutosaveTextField = new javax.swing.JTextField();
        selectPathButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(titleText);
        setAlwaysOnTop(true);
        setModal(true);
        setName(FORM_NAME);
        setResizable(false);

        enableAutosaveCheckbox.setText(enableAutosaveCheckboxText);
        enableAutosaveCheckbox.setName("enableAutosaveCheckbox");
        enableAutosaveCheckbox.setDisplayedMnemonicIndex(0);
        enableAutosaveCheckbox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                enableAutosaveCheckboxItemStateChanged(evt);
            }
        });

        saveIntervalLabel.setText(saveIntervalLabelText);
        saveIntervalLabel.setDisplayedMnemonic(KeyEvent.VK_I);
        saveIntervalLabel.setName("saveIntervalLabel");

        secondsLabel.setText(saveIntervalSecondsLabelText);
        secondsLabel.setDisplayedMnemonic(KeyEvent.VK_N);
        secondsLabel.setName("secondsLabel");

        saveDirectoryLabel.setText(saveDirectoryLabelText);
        saveDirectoryLabel.setDisplayedMnemonic(KeyEvent.VK_A);
        saveDirectoryLabel.setName("saveDirectoryLabel");
        
        selectPathButton.setText(selectPathButtonText);
        selectPathButton.setName("selectPathButton");
        
        selectPathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            JFileChooser chooser;
	
	            chooser = new JFileChooser();
	            chooser.setCurrentDirectory(new java.io.File("."));
	            chooser.setDialogTitle("Select directory");
	            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	            chooser.setAcceptAllFileFilterUsed(false);
	
	            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	                File file = chooser.getSelectedFile();
	                pathAutosaveTextField.setText(file.getAbsolutePath()+ File.separator + "VioletUML");
	            }
            }
        });

        okButton.setMnemonic(KeyEvent.VK_O);
        okButton.setText(okButtonText);
        okButton.setName("okButton");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        cancelButton.setMnemonic(KeyEvent.VK_C);
        cancelButton.setText(cancelButtonText);
        cancelButton.setName("cancelButton");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(enableAutosaveCheckbox, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(saveIntervalLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(saveIntervalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(secondsLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(saveDirectoryLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(pathAutosaveTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(selectPathButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addGap(48, 48, 48))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(enableAutosaveCheckbox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(secondsLabel)
                    .addComponent(saveIntervalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveIntervalLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(pathAutosaveTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveDirectoryLabel)
                    .addComponent(selectPathButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cancelButton, okButton});
        pack();
    }

    /**
     * Enable autosave checkbox ithem state changed
     * @param event
     */
    private void enableAutosaveCheckboxItemStateChanged(java.awt.event.ItemEvent evt)
    {
        if (!autosave.isLoadingDialog())
        {
            enableDisableComponents();
        }
    }

    /**
     * Cancel button action performed
     * @parent event
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    /**
     * Save button action performed
     * @param event
     */
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        if (validationIsOK()) {
        	saveSettings();
            autosave.saveSettings();
            if (this.autosaveListener != null
                    ) {
            	this.autosaveListener.reloadSettings();
            }
            dispose();
        }
    }

    /**
     * Check if validation is corrected
     * @return what is validation
     */
    private boolean validationIsOK()
    {
        if (!enableAutosaveCheckbox.isSelected())
        {
            return true;
        }

        String seconds = saveIntervalTextField.getText();

        try
        {
            Integer.parseInt(seconds);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, invalidValueText);
            saveIntervalTextField.requestFocus();
            return false;
        }

        if (pathAutosaveTextField.getText().trim().isEmpty())
        {
            JOptionPane.showMessageDialog(this, invalidValueText);
            pathAutosaveTextField.requestFocus();
            return false;
        }

        File directory = new File(pathAutosaveTextField.getText());

        return true;
    }
    
    /**
     * Save settings
     */
    private void saveSettings()
    {
    	autosave.setEnableAutosave(enableAutosaveCheckbox.isSelected());
    	autosave.setAutosavePath(pathAutosaveTextField.getText());
    	autosave.setAutosaveInterval(Integer.parseInt(saveIntervalTextField.getText()));
    }

    /**
     * Load settings
     */
    private void loadSettings()
    {
        enableAutosaveCheckbox.setSelected(autosave.isEnableAutosave());
        saveIntervalTextField.setText(String.valueOf(autosave.getAutosaveInterval()));
        pathAutosaveTextField.setText(autosave.getAutosavePath());
    }
}
