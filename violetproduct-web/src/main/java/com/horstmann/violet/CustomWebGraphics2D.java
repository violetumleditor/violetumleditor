package com.horstmann.violet;

import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;

import eu.webtoolkit.jwt.WPainter;
import eu.webtoolkit.jwt.utils.WebGraphics2D;

public class CustomWebGraphics2D extends WebGraphics2D {

	public CustomWebGraphics2D(WPainter painter) {
		super(painter);
	}
	
	@Override
	public Graphics create() {
		return this;
	}
	
	@Override
	public void dispose() {
		return;
	}
	
	@Override
	public Object getRenderingHint(Key arg0) {
		return RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT;
	}
}
