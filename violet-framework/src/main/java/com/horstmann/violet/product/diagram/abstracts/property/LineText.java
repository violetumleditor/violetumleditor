package com.horstmann.violet.product.diagram.abstracts.property;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * Created by Adrian Bobrowski on 16.12.2015.
 */
public abstract class LineText implements Serializable, Cloneable {

    public abstract void setText(String text);
    public abstract String getText();

    final public void setAlignment(int flag) {
        label.setHorizontalAlignment(flag);
        refresh();
    }
    final public int getAlignment(int flag) {
        return label.getHorizontalAlignment();
    }

    final public void setPadding(int padding){
        setPadding(padding, padding);
    }
    final public void setPadding(int vertical, int horizontal){
        setPadding(vertical, horizontal, vertical, horizontal);
    }
    final public void setPadding(int top, int left, int bottom, int right){
        label.setBorder(new EmptyBorder(top, left, bottom, right));
        refresh();
    }

    final public Rectangle2D getBounds() {
        if (null == this.bounds) {
            this.bounds = new Rectangle2D.Double(0, 0, 0, 0);
        }
        return this.bounds;
    }

    final public void draw(Graphics2D g2, Rectangle2D r) {
        label.setBounds(0, 0, (int) r.getWidth(), (int) r.getHeight());
        g2.translate(r.getX(), r.getY());
        label.paint(g2);
        g2.translate(-r.getX(), -r.getY());
    }

    final protected void setLabelText(String text)
    {
        label.setText("<html>"+text+"<html>");
        refresh();
    }

    private void refresh()
    {
        BufferedImage image = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        this.bounds = getTextBounds(g2);
    }

    private Rectangle2D getTextBounds(Graphics2D g2) {
        Dimension dim = label.getPreferredSize();
        return new Rectangle2D.Double(0, 0, dim.getWidth(), dim.getHeight());
    }

    public static final int LEFT = SwingConstants.LEFT;
    public static final int CENTER = SwingConstants.CENTER;
    public static final int RIGHT = SwingConstants.RIGHT;

    private transient JLabel label = new JLabel();
    private transient Rectangle2D bounds = null;
}
