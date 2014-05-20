package com.horstmann.violet.vaadin;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Image;

public class NodeWrapperWidget extends Image implements StreamResource.StreamSource {

	private INode wrappedNode;
	private static final String FORMAT = "png";

	public NodeWrapperWidget(INode wrappedNode) {
		super();
		this.wrappedNode = wrappedNode;
		repaint();
	}


	@Override
	public void markAsDirty() {
		repaint();
		super.markAsDirty();
	}
	
	@Override
	public void markAsDirtyRecursive() {
		repaint();
		super.markAsDirtyRecursive();
	}

	public void repaint() {
		setSource(new StreamResource(this, this.wrappedNode.getId() + "." + FORMAT));
	}

	@Override
	public InputStream getStream() {
		try {
			Rectangle2D bounds = this.wrappedNode.getBounds();
			BufferedImage image = new BufferedImage((int) bounds.getWidth() + 1, (int) bounds.getHeight() + 1, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = (Graphics2D) image.getGraphics();
			g2.translate(-bounds.getX(), -bounds.getY());
			g2.setColor(Color.WHITE);
			g2.fill(new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth() + 1, bounds.getHeight() + 1));
			g2.setColor(Color.BLACK);
			g2.setBackground(Color.WHITE);
			this.wrappedNode.draw(g2);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(image, FORMAT, out);
			ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
			return in;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
