package com.horstmann.violet.web.workspace.sidebar;

import java.awt.Component;

import javax.swing.JOptionPane;

import eu.webtoolkit.jwt.WCompositeWidget;

public class OptionPaneWidget extends WCompositeWidget {

	public OptionPaneWidget(JOptionPane optionPane) {
		super();
		for (Component aComponent : optionPane.getComponents()) {
			aComponent.getClass();
		}
	}

	

}
