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

package com.horstmann.violet.product.diagram.propertyeditor.customeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditorSupport;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.chooser.IFileChooserService;
import com.horstmann.violet.framework.file.naming.ExtensionFilter;
import com.horstmann.violet.framework.file.naming.FileNamingService;
import com.horstmann.violet.framework.file.persistence.IFileReader;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;

/**
 * A PropertyEditor for Image objects that lets the browse and select an image
 * file.
 * 
 * @author Alexandre de Pellegrin
 */
public class ImageIconEditor extends PropertyEditorSupport {

    /** The file chooser to use with with menu */
    @InjectedBean
    private IFileChooserService fileChooserService;
    
    /** File services */
    @InjectedBean
    private FileNamingService fileNamingService;

    private ImageIcon source;

    public ImageIconEditor() {
	super();
	BeanInjector.getInjector().inject(this);
    }

    /**
     * Returns true because we do support a custom editor.
     * 
     * @return true
     */
    public boolean supportsCustomEditor() {
	return true;
    }

    /**
     * Gets the custom editor component.
     * 
     * @return a value of type 'java.awt.Component'
     */
    public java.awt.Component getCustomEditor() {
	this.source = (ImageIcon) getValue();

	JLabel imageLabel = new JLabel();
	imageLabel.setIcon(this.source);

	JButton chooseButton = new JButton();
	chooseButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		try {
		    ExtensionFilter[] exportFilters = fileNamingService.getImageExtensionFilters();
		    IFileReader fileOpener = fileChooserService.chooseAndGetFileReader(exportFilters);
		    IFile selectedFile = fileOpener.getFileDefinition();
		    if (selectedFile == null)
			return;
		    String fullPath = selectedFile.getDirectory() + File.separator + selectedFile.getFilename();
		    ImageIcon imageIcon = new ImageIcon(fullPath);
		    ImageIconEditor.this.source.setImage(imageIcon.getImage());
		    firePropertyChange();
		} catch (Exception ex) {
		    throw new RuntimeException(ex);
		}
	    }
	});

	JPanel panel = new JPanel();
	panel.add(imageLabel);
	panel.add(chooseButton);
	return panel;
    }

}
