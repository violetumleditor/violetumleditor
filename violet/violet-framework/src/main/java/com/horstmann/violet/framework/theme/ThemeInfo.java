package com.horstmann.violet.framework.theme;

import javax.swing.LookAndFeel;

public class ThemeInfo {

	private String name;

	private Class<? extends ITheme> themeClass;
	
	private Class<? extends LookAndFeel> lafClass;

	public ThemeInfo(String name, Class<? extends ITheme> themeClass, Class<? extends LookAndFeel> lafClass) {
		this.name = name;
		this.themeClass = themeClass;
		this.lafClass = lafClass;
	}

	public String getName() {
		return name;
	}

	public Class<? extends ITheme> getThemeClass() {
		return themeClass;
	}
	
	public Class<? extends LookAndFeel> getLookAndFeelClass() {
		return lafClass;
	}

}
