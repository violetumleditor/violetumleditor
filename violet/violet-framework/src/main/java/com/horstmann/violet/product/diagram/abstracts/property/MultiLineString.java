/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.product.diagram.abstracts.property;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JLabel;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * A string that can extend over multiple lines.
 */
public class MultiLineString implements Serializable, Cloneable
{
    /**
     * Constructs an empty, centered, normal size multiline string that is not underlined.
     */
    public MultiLineString()
    {
        text = "";
        justification = CENTER;
        size = NORMAL;
        underlined = false;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param newValue the text of the multiline string
     */
    public void setText(String newValue)
    {
        text = newValue;
        setLabelText();
        isBoundsDirty = true;
    }

    /**
     * Gets the value of the text property.
     * 
     * @return the text of the multiline string
     */
    public String getText()
    {
        return text;
    }

    /**
     * Sets the value of the justification property.
     * 
     * @param newValue the justification, one of LEFT, CENTER, RIGHT
     */
    public void setJustification(int newValue)
    {
        justification = newValue;
        setLabelText();
        isBoundsDirty = true;
    }

    /**
     * Gets the value of the justification property.
     * 
     * @return the justification, one of LEFT, CENTER, RIGHT
     */
    public int getJustification()
    {
        return justification;
    }

    /**
     * Gets the value of the underlined property.
     * 
     * @return true if the text is underlined
     */
    public boolean isUnderlined()
    {
        return underlined;
    }

    /**
     * Sets the value of the underlined property.
     * 
     * @param newValue true to underline the text
     */
    public void setUnderlined(boolean newValue)
    {
        underlined = newValue;
        setLabelText();
        isBoundsDirty = true;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param newValue the size, one of SMALL, NORMAL, LARGE
     */
    public void setSize(int newValue)
    {
        size = newValue;
        setLabelText();
        isBoundsDirty = true;
    }

    /**
     * Gets the value of the size property.
     * 
     * @return the size, one of SMALL, NORMAL, LARGE
     */
    public int getSize()
    {
        return size;
    }

    public String toString()
    {
        return text.replace('\n', '|');
    }

    private void setLabelText()
    {
        StringBuffer prefix = new StringBuffer();
        StringBuffer suffix = new StringBuffer();
        StringBuffer htmlText = new StringBuffer();
        prefix.append("&nbsp;");
        suffix.insert(0, "&nbsp;");
        if (underlined)
        {
            prefix.append("<u>");
            suffix.insert(0, "</u>");
        }
        if (size == LARGE)
        {
            prefix.append("<font size=\"3\"><b>");
            suffix.insert(0, "</b></font>");
        }
        if (size == SMALL)
        {
            prefix.append("<font size=\"3\">");
            suffix.insert(0, "</font>");
        }
        htmlText.append("<html>");
        StringTokenizer tokenizer = new StringTokenizer(text, "\n");
        boolean first = true;
        while (tokenizer.hasMoreTokens())
        {
            if (first) first = false;
            else htmlText.append("<br>");
            htmlText.append(prefix);
            htmlText.append(tokenizer.nextToken());
            htmlText.append(suffix);
        }
        htmlText.append("</html>");

        // replace any < that are not followed by {u, i, b, tt, font, br} with &lt;

        List<String> dontReplace = Arrays.asList(new String[]
        {
                "u",
                "i",
                "b",
                "tt",
                "font",
                "br"
        });

        int ltpos = 0;
        while (ltpos != -1)
        {
            ltpos = htmlText.indexOf("<", ltpos + 1);
            if (ltpos != -1 && !(ltpos + 1 < htmlText.length() && htmlText.charAt(ltpos + 1) == '/'))
            {
                int end = ltpos + 1;
                while (end < htmlText.length() && Character.isLetter(htmlText.charAt(end)))
                    end++;
                if (!dontReplace.contains(htmlText.substring(ltpos + 1, end))) htmlText.replace(ltpos, ltpos + 1, "&lt;");
            }
        }

        getLabel().setText(htmlText.toString());
        if (justification == LEFT) getLabel().setHorizontalAlignment(JLabel.LEFT);
        else if (justification == CENTER) getLabel().setHorizontalAlignment(JLabel.CENTER);
        else if (justification == RIGHT) getLabel().setHorizontalAlignment(JLabel.RIGHT);

    }


    /**
     * Gets the bounding rectangle for this multiline string.
     * 
     * @param g2 the graphics context
     * @return the bounding rectangle (with top left corner (0,0))
     */
    private Rectangle2D getBounds(Graphics2D g2)
    {
        setLabelText();
    	getLabel().setFont(g2.getFont());
        getLabel().validate();
        if (text.length() == 0) return new Rectangle2D.Double(0, 0, 0, 0);
        Dimension dim = getLabel().getPreferredSize();
        return new Rectangle2D.Double(0, 0, dim.getWidth(), dim.getHeight());
    }

    /**
     * Gets the bounding rectangle for this multiline string.
     * 
     * @return the bounding rectangle (with top left corner (0,0))
     */
    public Rectangle2D getBounds()
    {
        if (this.isBoundsDirty || this.bounds == null)
        {
            BufferedImage image = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = (Graphics2D) image.getGraphics();
            this.bounds = getBounds(g2);
            this.isBoundsDirty = false;
        }
        return this.bounds;
    }

    /**
     * Draws this multiline string inside a given rectangle
     * 
     * @param g2 the graphics context
     * @param r the rectangle into which to place this multiline string
     */
    public void draw(Graphics2D g2, Rectangle2D r)
    {
    	getLabel().setFont(g2.getFont());
    	getLabel().setBounds(0, 0, (int) r.getWidth(), (int) r.getHeight());
        g2.translate(r.getX(), r.getY());
        getLabel().paint(g2);
        g2.translate(-r.getX(), -r.getY());
    }

    public MultiLineString clone()
    {
    	MultiLineString cloned = new MultiLineString();
    	cloned.text = text;
    	cloned.justification = justification;
    	cloned.size = size;
    	cloned.underlined = underlined;
    	cloned.setLabelText();
    	return cloned;
    }
    
    private JLabel getLabel() {
    	if (this.label == null) {
    		this.label = new JLabel();
    	}
    	return this.label;
    }

    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;
    public static final int LARGE = 3;
    public static final int NORMAL = 4;
    public static final int SMALL = 5;

    private String text;
    @XStreamAsAttribute
    private int justification;
    @XStreamAsAttribute
    private int size;
    @XStreamAsAttribute
    private boolean underlined;
    private transient JLabel label;
    private transient boolean isBoundsDirty = true;
    private transient Rectangle2D bounds;
}
