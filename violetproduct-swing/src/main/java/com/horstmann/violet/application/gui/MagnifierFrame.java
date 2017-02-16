package com.horstmann.violet.application.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class MagnifierFrame {

	private static final int ZOOM_LEVEL_1 = 2;
	private static final int ZOOM_LEVEL_2 = 4;
	private static final int ZOOM_LEVEL_3 = 8;
	
	private static final int MAGNIFIER_WIDTH_HEIGHT = 128;
	private static final int SCREEN_CAPTURE_TIMEOUT = 4;
	
	public static final int PADDING_RIGHT = 350;
	public static final int PADDING_TOP = 100;
	
	private final BufferedImage zoomImage = new BufferedImage(MAGNIFIER_WIDTH_HEIGHT, MAGNIFIER_WIDTH_HEIGHT, BufferedImage.TYPE_INT_RGB);
	private final JLabel zoomLabel = new JLabel(new ImageIcon(zoomImage));
	
	private Robot mouseRobot;
	private int zoomFactor = ZOOM_LEVEL_1;
	private PointerInfo mousePointerInfo;
	private JPanel zoomPanel;
	private Timer mouseMoveTimer;

	/**
	 * Constructor magnifier frame
	 * @throws AWTException
	 */
	public MagnifierFrame() throws AWTException {
		mouseRobot = new Robot();
		zoomPanel = new JPanel(new BorderLayout(2, 2));	
		zoomPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		zoomPanel.add(zoomLabel, BorderLayout.CENTER);
		zoomLabel.addMouseListener(createFactorListener());		
		mouseMoveTimer = new Timer(SCREEN_CAPTURE_TIMEOUT, createZoomListener());
	    mouseMoveTimer.start();
	}

	/**
	 * Create factor listener
	 * @return mouse listener
	 */
	private MouseListener createFactorListener() {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (SwingUtilities.isLeftMouseButton(mouseEvent)) 
				{
					increaseZoomLevel();
				} else if (SwingUtilities.isRightMouseButton(mouseEvent)) 
				{
					decreaseZoomLevel();
				}			
			}		
		};
	}

	/**
	 * Create zoom listener
	 * @return action listener
	 */
	private ActionListener createZoomListener() {
		 return new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					mousePointerInfo = MouseInfo.getPointerInfo();
					Point p = mousePointerInfo.getLocation();
					Rectangle r = new Rectangle(p.x - (MAGNIFIER_WIDTH_HEIGHT / (2 * zoomFactor)), p.y - (MAGNIFIER_WIDTH_HEIGHT / (2 * zoomFactor)),
							(MAGNIFIER_WIDTH_HEIGHT / zoomFactor), (MAGNIFIER_WIDTH_HEIGHT / zoomFactor));
					BufferedImage temp = mouseRobot.createScreenCapture(r);
					Graphics g = zoomImage.getGraphics();
					g.drawImage(temp, 0, 0, MAGNIFIER_WIDTH_HEIGHT, MAGNIFIER_WIDTH_HEIGHT, null);
					g.setColor(new Color(255, 0, 0, 128));
					int x = (MAGNIFIER_WIDTH_HEIGHT / 2) - 1;
					int y = (MAGNIFIER_WIDTH_HEIGHT / 2) - 1;
					g.drawLine(0, y, MAGNIFIER_WIDTH_HEIGHT, y);
					g.drawLine(x, 0, x, MAGNIFIER_WIDTH_HEIGHT);
					g.dispose();
					zoomLabel.repaint();
				}
			};
	}

	/**
	 * Get zoom panel
	 * @return zoom panel
	 */
	public Component getZoomPanel() {
		return zoomPanel;
	}
	
	/**
	 * Get mouse move timer
	 * @return mouse move timer
	 */
	public Timer getMouseMoveTimer() {
		return mouseMoveTimer;
	}

	/**
	 * Increase zoom level
	 */
	private void increaseZoomLevel() {
		if (zoomFactor == ZOOM_LEVEL_1) 
		{
			zoomFactor = ZOOM_LEVEL_2;
		} 
			else if (zoomFactor == ZOOM_LEVEL_2) 
		{
			zoomFactor = ZOOM_LEVEL_3;
		} 
			else if (zoomFactor == ZOOM_LEVEL_3) 
		{
			zoomFactor = ZOOM_LEVEL_1;
		}
	}

	/**
	 * Decrease zoom level
	 */
	private void decreaseZoomLevel() {
		if (zoomFactor == ZOOM_LEVEL_3) 
		{
			zoomFactor = ZOOM_LEVEL_2;
		} else if (zoomFactor == ZOOM_LEVEL_2) 
		{
			zoomFactor = ZOOM_LEVEL_1;
		} else if (zoomFactor == ZOOM_LEVEL_1) 
		{
			zoomFactor = ZOOM_LEVEL_3;
		}		
	}

}
