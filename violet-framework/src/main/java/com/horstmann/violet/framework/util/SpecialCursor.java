package com.horstmann.violet.framework.util;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

public class SpecialCursor {

	private static Cursor generate(String cursorImagePath) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		//Image image = toolkit.getImage(cursorImagePath);
		ImageIcon icon = new ImageIcon(SpecialCursor.class.getResource(cursorImagePath));
		Cursor c = toolkit.createCustomCursor(icon.getImage(), new Point(0, 0), cursorImagePath);
		return c;
	}
	
	
	public static Cursor BUCKET_FILL_TOOL;
	
	static {
		BUCKET_FILL_TOOL = generate("/cursors/tool-bucket-fill.png");
	}
	
	
}
