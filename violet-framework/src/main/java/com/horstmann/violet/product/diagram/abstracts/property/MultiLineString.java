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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Map;
import com.horstmann.violet.framework.swingextension.MultiLineLabel;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * A string that can extend over multiple lines.
 */
public class MultiLineString implements Serializable, Cloneable {
	private static final long serialVersionUID = -1577465027960103788L;
	private String text;
	@XStreamAsAttribute
	private Alignment alignment;
	@XStreamAsAttribute
	private FontStyle fontStyle;
	@XStreamAsAttribute
	private boolean underlined;
	private transient MultiLineLabel label;
	private transient boolean isBoundsDirty = true;
	private transient Rectangle2D bounds;
	
	public static enum Alignment {
		CENTER, LEFT, RIGHT;
	}
	public static enum FontStyle {
		ITALIC, NORMAL, BOLD;
		
		public static final int MARGIN_WIDTH = 2;
		public static final int SIZE = 12;
	}
	
	
	/**
	 * Constructs an empty, centered, normal size multiline string that is not
	 * underlined.
	 */
	public MultiLineString() {
		this.initialize( "" );
	}
	/**
	 * Constructs with text, centered, normal size multiline string that is not
	 * underlined.
	 * @param text
	 */
	public MultiLineString( String text ) {
		this.initialize( text );
	}
	
	
	/**
	 * Build basic multi line string.
	 * @param text
	 */
	private void initialize( String text ) {
		this.text = text;
		this.alignment = Alignment.CENTER;
		this.fontStyle = FontStyle.NORMAL;
		this.underlined = false;
	}
	/**
	 * Build font of label text;
	 * @param fontStyle
	 */
	private void buildFontStyle( FontStyle fontStyle ) {
		switch( fontStyle ) {
			case BOLD:
				this.label.setFont( new Font( Font.SANS_SERIF, Font.BOLD, FontStyle.SIZE ) );
				break;
			case ITALIC:
				this.label.setFont( new Font( Font.SANS_SERIF, Font.ITALIC, FontStyle.SIZE ) );
				break;
			case NORMAL:
				this.label.setFont( new Font( Font.SANS_SERIF, Font.PLAIN, FontStyle.SIZE ) );
				break;
			default:
				this.label.setFont( new Font( Font.SANS_SERIF, Font.PLAIN, FontStyle.SIZE ) );
				break;
		}
		this.label.setMarginWidth( FontStyle.MARGIN_WIDTH );
	}
	/**
	 * Build alignment of label text.
	 * @param alignment
	 */
	private void buildAlignment( Alignment alignment ) {
		switch( alignment ) {
			case CENTER:
				this.label.setAlignment( MultiLineLabel.CENTER );
				break;
			case LEFT:
				this.label.setAlignment( MultiLineLabel.LEFT );
				break;
			case RIGHT:
				this.label.setAlignment( MultiLineLabel.RIGHT );
				break;
			default:
				this.label.setAlignment( MultiLineLabel.CENTER );	
		}
	}
	/**
	 * Build underline of label text.
	 * @param underlined
	 */
	@SuppressWarnings("unchecked")
	private void buildUnderline( boolean underlined ) {
		if( underlined ) {
			@SuppressWarnings("rawtypes")
			Map labelAttributes = this.label.getFont().getAttributes();
			labelAttributes.put( TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON );
			this.label.setFont( this.label.getFont().deriveFont( labelAttributes ) );
		}
	}
	/**
	 * Build label.
	 * @return MultiLineLabel
	 */
	private MultiLineLabel buildLabel() {
		if( this.label == null ) {
			this.label = new MultiLineLabel( "" );
		}
		buildFontStyle( fontStyle );
		buildAlignment( alignment );
		buildUnderline( underlined );
		return this.label;
	}
	/**
	 * Build label with new functionality.
	 */
	private void buildLabelText() {
		this.buildLabel().setLabel( this.text );
		this.isBoundsDirty = true;
	}

	/**
	 * Sets the value of the text property.
	 * 
	 * @param text
	 *            the text of the multiline string
	 */
	public MultiLineString useText( String text ) {
		this.text = text;
		this.buildLabelText();
		return this;
	}
	/**
	 * Gets the value of the text property.
	 * 
	 * @return the text of the multiline string
	 */
	public String getText() {
		return text;
	}
	/**
	 * Sets the value of the justification property.
	 * 
	 * @param alignment
	 *            the justification, one of LEFT, CENTER, RIGHT
	 */
	public MultiLineString useAlignment( Alignment alignment ) {
		this.alignment = alignment;
		this.buildLabelText();
		return this;
	}
	/**
	 * Gets the value of the justification property.
	 * 
	 * @return the justification, one of LEFT, CENTER, RIGHT
	 */
	public Alignment getAlignment() {
		return alignment;
	}
	/**
	 * Gets the value of the underlined property.
	 * 
	 * @return true if the text is underlined
	 */
	public boolean isUnderlined() {
		return underlined;
	}
	/**
	 * Sets the value of the underlined property.
	 * 
	 * @param underline
	 *            true to underline the text
	 */
	public MultiLineString useUnderline( boolean underline ) {
		this.underlined = underline;	
		this.buildLabelText();
		return this;
	}
	/**
	 * Sets the value of the size property.
	 * 
	 * @param fontStyle
	 *            the size, one of SMALL, NORMAL, LARGE
	 */
	public MultiLineString useFontStyle( FontStyle fontStyle ) {
		this.fontStyle = fontStyle;
		this.buildLabelText();
		return this;
	}
	/**
	 * Gets the value of the size property.
	 * 
	 * @return the size, one of SMALL, NORMAL, LARGE
	 */
	public FontStyle getFontStyle() {
		return fontStyle;
	}
	public String toString() {
		return text.replace('\n', '|');
	}
	/**
	 * Gets the bounding rectangle for this multiline string.
	 * 
	 * @param g2
	 *            the graphics context
	 * @return the bounding rectangle (with top left corner (0,0))
	 */
	private Rectangle2D getBounds(Graphics2D g2) {
		buildLabelText();
		buildLabel().validate();
		if (text.length() == 0)
			return new Rectangle2D.Double(0, 0, 0, 0);
		Dimension dim = buildLabel().getPreferredSize();
		return new Rectangle2D.Double(0, 0, dim.getWidth(), dim.getHeight());
	}

	/**
	 * Gets the bounding rectangle for this multiline string.
	 * 
	 * @return the bounding rectangle (with top left corner (0,0))
	 */
	public Rectangle2D getBounds() {
		if (this.isBoundsDirty || this.bounds == null) {
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
	 * @param g2
	 *            the graphics context
	 * @param r
	 *            the rectangle into which to place this multiline string
	 */
	public void draw(Graphics2D g2, Rectangle2D r) {
		buildLabel().setBounds(0, 0, (int) r.getWidth(), (int) r.getHeight());
		g2.translate(r.getX(), r.getY());
		buildLabel().paint(g2);
		g2.translate(-r.getX(), -r.getY());
	}
	public MultiLineString clone() {
		MultiLineString cloned = new MultiLineString();
		cloned.text = text;
		cloned.alignment = alignment;
		cloned.fontStyle = fontStyle;
		cloned.underlined = underlined;
		cloned.buildLabelText();
		return cloned;
	}
}