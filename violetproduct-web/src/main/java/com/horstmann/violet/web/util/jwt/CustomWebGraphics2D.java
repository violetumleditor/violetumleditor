package com.horstmann.violet.web.util.jwt;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints.Key;
import java.awt.image.BufferedImage;

import eu.webtoolkit.jwt.WFont;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WFont.GenericFamily;
import eu.webtoolkit.jwt.WFont.Size;
import eu.webtoolkit.jwt.WFont.Style;
import eu.webtoolkit.jwt.WFont.Weight;
import eu.webtoolkit.jwt.WLength.Unit;
import eu.webtoolkit.jwt.WPainter;
import eu.webtoolkit.jwt.utils.WebGraphics2D;

public class CustomWebGraphics2D extends WebGraphics2D {

	/**
	 * As the framework doesn't implements all graphics2D features, we delegated
	 * sometimes method calls to a Graphics2D instance specific to the host
	 * platform
	 */
	private Graphics2D hostGraphics2D;
	
	private WPainter painter;

	public CustomWebGraphics2D(WPainter painter) {
		super(painter);
		this.painter = painter;
	}
	
	@Override
	public void drawString(String s, int x, int y) {
		WFont f = new WFont(GenericFamily.SansSerif);
		Font swingFont = getFont();
		int size = swingFont.getSize();
		f.setSize(Size.FixedSize, new WLength(size, Unit.Pixel));
		if (swingFont.isItalic()) {
			f.setStyle(Style.Italic);
		}
		if (swingFont.isBold()) {
			f.setWeight(Weight.Bold);
		}
		this.painter.setFont(f);
		super.drawString(s, x, y);
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
		return getHostGraphics2D().getRenderingHint(arg0);
	}

	private Graphics2D getHostGraphics2D() {
		if (this.hostGraphics2D == null) {
			BufferedImage image = new BufferedImage(1, 1,
					BufferedImage.TYPE_INT_RGB);
			this.hostGraphics2D = (Graphics2D) image.getGraphics();
		}
		return this.hostGraphics2D;
	}

}
