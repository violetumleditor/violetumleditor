package com.horstmann.violet.framework.swingextension;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

public class FadeImage extends JLabel implements ActionListener {

    // Up to 1f
    private float opacity = 0f;
    private Timer fadeTimer;
    private int fadeIndicator = 1;

    public FadeImage(ImageIcon anImage) {
	super();
	setIcon(anImage);
	initializeTimer();
    }

    private void initializeTimer() {
	fadeTimer = new javax.swing.Timer(25, this);
	fadeTimer.setInitialDelay(0);
    }

    public void fadeIn() {
	fadeIndicator = 1;
	fadeTimer.restart();
    }

    public void fadeOut() {
	fadeIndicator = -1;
	fadeTimer.restart();
    }

    public void actionPerformed(ActionEvent e) {
	opacity = opacity + (fadeIndicator * 0.1f);
	if (opacity > 1) {
	    opacity = 1;
	    fadeTimer.stop();
	}
	if (opacity < 0) {
	    opacity = 0;
	    fadeTimer.stop();
	}
	repaint();
    }

    @Override
    public void paint(Graphics g) {
	((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
	super.paint(g);
    }

}
