package com.horstmann.violet;

import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WPaintDevice;
import eu.webtoolkit.jwt.WPaintedWidget;
import eu.webtoolkit.jwt.WPainter;

public class OptionPaneWidget extends WPaintedWidget {

	private JPanel panelContainer;
	
	public OptionPaneWidget(JOptionPane optionPane) {
		super();
		this.panelContainer = new JPanel() {
			@Override
			public void repaint() {
				OptionPaneWidget.this.update();
			}
		};
		this.panelContainer.add(optionPane);
		this.panelContainer.add(new JLabel("blabla"));
	}

	@Override
    public void resize(WLength width, WLength height) {
            super.resize(width, height);
            panelContainer.setSize((int)width.toPixels(), (int)height.toPixels());
    }
    

    @Override
    protected void layoutSizeChanged(int width, int height) {
            super.layoutSizeChanged(width, height);
            panelContainer.setSize(width, height);
    }       
	

	@Override
	protected void paintEvent(WPaintDevice paintDevice) {
		WPainter painter = new WPainter(paintDevice);
		painter.setClipping(true);
		paintDevice.init();
		Graphics2D graphics = new CustomWebGraphics2D(painter);
        panelContainer.paint(graphics);
        paintDevice.done();
	}

}
