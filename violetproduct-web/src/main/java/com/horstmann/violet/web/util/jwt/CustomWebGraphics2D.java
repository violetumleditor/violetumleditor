package com.horstmann.violet.web.util.jwt;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints.Key;
import java.awt.image.BufferedImage;

import eu.webtoolkit.jwt.WPainter;
import eu.webtoolkit.jwt.utils.WebGraphics2D;

public class CustomWebGraphics2D extends WebGraphics2D {

	/**
	 * As the framework doesn't implements all graphics2D features, we delegated
	 * sometimes method calls to a Graphics2D instance specific to the host
	 * platform
	 */
	private Graphics2D hostGraphics2D;

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
