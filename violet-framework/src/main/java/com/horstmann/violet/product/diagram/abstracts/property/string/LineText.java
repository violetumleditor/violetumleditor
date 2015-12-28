package com.horstmann.violet.product.diagram.abstracts.property.string;

import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.OneLineString;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/**
 * Created by Adrian Bobrowski on 16.12.2015.
 */
public abstract class LineText implements Serializable, Cloneable {
    public interface Converter extends Cloneable {
        OneLineString toLineString(String text);
    }

    public LineText() {
        this.converter = new Converter() {
            @Override
            public OneLineString toLineString(String text)
            {
                return new OneLineString(text);
            }
        };
    }
    public LineText(Converter converter) {
        this.converter = converter;
    }

    public abstract void setText(String text);
    public abstract String getText();
    public abstract String getHTML();

    final public void setPadding(int padding){
        setPadding(padding, padding);
    }
    final public void setPadding(int vertical, int horizontal){
        setPadding(vertical, horizontal, vertical, horizontal);
    }
    final public void setPadding(int top, int left, int bottom, int right){
        label.setBorder(new EmptyBorder(top, left, bottom, right));
        label.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        refresh();
    }

    final public void setAlignment(int flag) {
        label.setHorizontalAlignment(flag);
        refresh();
    }
    final public int getAlignment() {
        return label.getHorizontalAlignment();
    }

    final public void setMinimumSize(Dimension dimension) {
        label.setMinimumSize(dimension);
        refresh();
    }



    final public Rectangle2D getBounds() {
        if (null == this.bounds) {
            this.bounds = new Rectangle2D.Double(0, 0, 0, 0);
        }
        return this.bounds;
    }

    //todo probowac usunac na rzecz punktu
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
        this.bounds = getTextBounds(getText());
    }

    private Rectangle2D getTextBounds(String text) {
        if (null == text || text.isEmpty())
        {
            return new Rectangle2D.Double(0, 0, 0, 0);
        }

        Dimension dim = label.getPreferredSize();
        return new Rectangle2D.Double(0, 0, dim.getWidth(), dim.getHeight());
    }

    protected final void copyLabelProperty(LineText cloned)
    {
        cloned.label.setHorizontalAlignment(this.label.getHorizontalAlignment());
        cloned.label.setVerticalAlignment(this.label.getVerticalAlignment());
        cloned.label.setBorder(this.label.getBorder());
        cloned.label.setMinimumSize(this.label.getMinimumSize());
        cloned.label.setText(this.label.getText());
    }

    public static final int LEFT = SwingConstants.LEFT;
    public static final int CENTER = SwingConstants.CENTER;
    public static final int RIGHT = SwingConstants.RIGHT;

    protected transient Converter converter;
    private transient JLabel label = new JLabel();
    private transient Rectangle2D bounds = null;
}
