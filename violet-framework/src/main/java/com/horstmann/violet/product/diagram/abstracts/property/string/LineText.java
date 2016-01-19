package com.horstmann.violet.product.diagram.abstracts.property.string;

import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.OneLineString;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 16.12.2015
 */
public abstract class LineText implements Serializable, Cloneable, EditableString
{
    public interface Converter
    {
        OneLineString toLineString(String text);
    }
    public interface ChangeListener
    {
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

    public void deserializeSupport()
    {

    }

    public final Rectangle2D getBounds()
    {
        if(null == bounds)
        {
            bounds = new Rectangle2D.Double(0, 0, 0, 0);
        }
        return bounds;
    }

    public final Color getTextColor()
    {
        return getLabel().getForeground();
    }
    public final void setTextColor(Color color)
    {
        getLabel().setForeground(color);
    }

    public final void setPadding(int padding)
    {
        setPadding(padding, padding);
    }
    public final void setPadding(int vertical, int horizontal)
    {
        setPadding(vertical, horizontal, vertical, horizontal);
    }
    public final void setPadding(int top, int left, int bottom, int right)
    {
        getLabel().setBorder(new EmptyBorder(top, left, bottom, right));
        refresh();
    }

    public final void setAlignment(int flag)
    {
        getLabel().setHorizontalAlignment(flag);
        refresh();
    }
    public final int getAlignment()
    {
        return getLabel().getHorizontalAlignment();
    }

    //todo probowac usunac na rzecz punktu
    public final void draw(Graphics2D g2, Rectangle2D r) {
        getLabel().setBounds(0, 0, (int) r.getWidth(), (int) r.getHeight());
        draw(g2, new Point2D.Double(r.getX(), r.getY()));
    }

    public final void draw(Graphics2D g2, Point2D p) {
        g2.translate(p.getX(), p.getY());
        getLabel().paint(g2);
        g2.translate(-p.getX(), -p.getY());
    }

    public final void draw(Graphics2D g2) {
        getLabel().setBounds(0, 0, (int) getBounds().getWidth(), (int) getBounds().getHeight());
        draw(g2, new Point2D.Double(0, 0));
    }

    public final void addChangeListener(ChangeListener changeListener)
    {
        if(null == changeListener)
        {
            throw new NullPointerException("ChangeListener can't be null");
        }
        getChangeListeners().add(changeListener);
    }
    protected final void notifyAboutChange()
    {
        for (ChangeListener changeListener : getChangeListeners())
        {
            changeListener.onChange();
        }
    }

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

    private void refresh()
    {
        if(getLabel().getText().isEmpty())
        {
            this.bounds = new Rectangle2D.Double(0, 0, 0, 0);
        }
        else
        {
            Dimension dim = getLabel().getPreferredSize();
            this.bounds = new Rectangle2D.Double(0, 0, dim.getWidth(), dim.getHeight());
        }
    }

    private JLabel getLabel()
    {
        if (null == label || null == label.getText())
        {
            label = new JLabel("");
        }
        return label;
    }

    private List<ChangeListener> getChangeListeners()
    {
        if(null == changeListeners)
        {
            changeListeners = new ArrayList<ChangeListener>();
        }
        return changeListeners;
    }

    public static final Converter DEFAULT_CONVERTER = new Converter(){
        @Override
        public OneLineString toLineString(String text)
        {
            return new OneLineString(text);
        }
    };

    public static final int LEFT = SwingConstants.LEFT;
    public static final int CENTER = SwingConstants.CENTER;
    public static final int RIGHT = SwingConstants.RIGHT;

    protected Converter converter;
    private transient JLabel label;
    private transient Rectangle2D bounds;

    private transient List<ChangeListener> changeListeners;
}
