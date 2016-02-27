package com.horstmann.violet.product.diagram.property.text;

import com.horstmann.violet.product.diagram.property.text.decorator.OneLineText;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * This class is a container for text
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 16.12.2015
 */
public abstract class LineText implements Serializable, Cloneable, EditableText
{
    public interface Converter
    {
        /**
         * converts plain text to one that may contain decorators
         * @param text
         * @return
         */
        OneLineText toLineString(String text);
    }
    public interface ChangeListener
    {
        /**
         * This function is called when the text is changed
         */
        void onChange();
    }

    public LineText()
    {
        this(DEFAULT_CONVERTER);
    }
    public LineText(Converter converter)
    {
        this.converter = converter;
    }

    protected LineText(LineText lineText) throws CloneNotSupportedException
    {
        getLabel().setHorizontalAlignment(lineText.getLabel().getHorizontalAlignment());
        getLabel().setVerticalAlignment(lineText.getLabel().getVerticalAlignment());
        getLabel().setForeground(lineText.getLabel().getForeground());
        getLabel().setBorder(lineText.getLabel().getBorder());
        getLabel().setText(lineText.getLabel().getText());
        converter = lineText.converter;
    }

    public final void deserializeSupport()
    {
        this.deserializeSupport(DEFAULT_CONVERTER);
    }
    public void deserializeSupport(Converter converter)
    {
        this.converter = converter;
    }

    @Override
    public LineText clone()
    {
        try {
            return copy();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    protected LineText copy() throws CloneNotSupportedException
    {
        return null;
    }

    /**
     * set converter
     * @param converter
     * @see Converter
     */
    public final void setConverter(Converter converter)
    {
        this.converter = converter;
    }

    /**
     * @return bounds of text in label
     */
    public final Rectangle2D getBounds()
    {
        if(null == bounds)
        {
            bounds = new Rectangle2D.Double(0, 0, 0, 0);
        }
        return bounds;
    }

    /**
     * @return label color
     */
    public final Color getTextColor()
    {
        return getLabel().getForeground();
    }

    /**
     * sets label color
     * @param color
     */
    public final void setTextColor(Color color)
    {
        getLabel().setForeground(color);
    }

    /**
     * sets label padding
     * @param padding
     */
    public final void setPadding(int padding)
    {
        setPadding(padding, padding);
    }

    /**
     * sets label padding
     * @param vertical
     * @param horizontal
     */
    public final void setPadding(int vertical, int horizontal)
    {
        setPadding(vertical, horizontal, vertical, horizontal);
    }

    /**
     * sets label padding
     * @param top
     * @param left
     * @param bottom
     * @param right
     */
    public final void setPadding(int top, int left, int bottom, int right)
    {
        getLabel().setBorder(new EmptyBorder(top, left, bottom, right));
        refresh();
    }

    /**
     * sets label alignments
     * @param flag
     *              LEFT
     *              CENTER
     *              RIGHT
     */
    public final void setAlignment(int flag)
    {
        getLabel().setHorizontalAlignment(flag);
        refresh();
    }

    /**
     * @return label alignments
     */
    public final int getAlignment()
    {
        return getLabel().getHorizontalAlignment();
    }

    /**
     * Draws text with specific size
     * @param graphics
     * @param rect
     */
    public final void draw(Graphics2D graphics, Rectangle2D rect)
    {
        getLabel().setBounds(0, 0, (int) rect.getWidth(), (int) rect.getHeight());
        draw(graphics, new Point2D.Double(rect.getX(), rect.getY()));
    }

    /**
     * Draws text shifted by offset
     * @param graphics
     * @param point
     */
    public final void draw(Graphics2D graphics, Point2D point)
    {
        graphics.translate(point.getX(), point.getY());
        getLabel().paint(graphics);
        graphics.translate(-point.getX(), -point.getY());
    }

    /**
     * Draws text
     * @param graphics
     */
    public final void draw(Graphics2D graphics)
    {
        getLabel().setBounds(0, 0, (int) getBounds().getWidth(), (int) getBounds().getHeight());
        draw(graphics, new Point2D.Double(0, 0));
    }

    /**
     * add listener
     * @param changeListener
     */
    public final void addChangeListener(ChangeListener changeListener)
    {
        if(null == changeListener)
        {
            throw new NullPointerException("ChangeListener can't be null");
        }
        getChangeListeners().add(changeListener);
    }

    /**
     * notify all listeners that there is a change
     */
    protected final void notifyAboutChange()
    {
        for (ChangeListener changeListener : getChangeListeners())
        {
            changeListener.onChange();
        }
    }

    /**
     * set text to label
     * @param text
     */
    protected final void setLabelText(String text)
    {
        if(text.isEmpty())
        {
            getLabel().setText("");
        }
        else
        {
            getLabel().setText("<html>"+text+"<html>");
        }

        refresh();
    }

    /**
     * Recalculate preferred size for text
     */
    private void refresh()
    {
        if(getLabel().getText().isEmpty())
        {
            this.bounds = new Rectangle2D.Double(0, 0, 0, 0);
        }
        else
        {
            Dimension dimension = getLabel().getPreferredSize();
            this.bounds = new Rectangle2D.Double(0, 0, dimension.getWidth(), dimension.getHeight());
        }
    }

    /**
     * @return label
     * @see JLabel
     */
    private JLabel getLabel()
    {
        if (null == label || null == label.getText())
        {
            label = new JLabel("");
        }
        return label;
    }

    /**
     * @return list of listeners
     */
    private List<ChangeListener> getChangeListeners()
    {
        if(null == changeListeners)
        {
            changeListeners = new ArrayList<ChangeListener>();
        }
        return changeListeners;
    }

    public static final Converter DEFAULT_CONVERTER = new Converter()
    {
        @Override
        public OneLineText toLineString(String text)
        {
            return new OneLineText(text);
        }
    };

    public static final int LEFT = SwingConstants.LEFT;
    public static final int CENTER = SwingConstants.CENTER;
    public static final int RIGHT = SwingConstants.RIGHT;

    protected transient Converter converter;
    private transient JLabel label;
    private transient Rectangle2D bounds;

    private transient List<ChangeListener> changeListeners;
}
