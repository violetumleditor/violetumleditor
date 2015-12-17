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

import com.horstmann.violet.framework.util.string.MultiLineString;

/**
 * A string that can extend over multiple lines.
 */
public class MultiLineText extends LineText {
	/**
	 * Constructs an empty, centered, normal size multiline string that is not
	 * underlined.
	 */
    public MultiLineText() {
        multiLineString = new MultiLineString();
    }

    public MultiLineText(MultiLineString multiLineString) {
        this.multiLineString = multiLineString;
    }

    /**
     * Gets the value of the text property.
     *
     * @return the text of the multiline string
     */
    public String getText() {
        return multiLineString.getText();
    }

	/**
	 * Sets the value of the text property.
	 * 
	 * @param newValue
	 *            the text of the multiline string
	 */
	public void setText(String newValue) {
        multiLineString.setText(newValue);

        this.setLabelText(multiLineString.getHTML());
	}

    public String toString() {
        return multiLineString.getText().replace('\n', '|');
    }

    public MultiLineText clone() {
        MultiLineText cloned = new MultiLineText();
        cloned.multiLineString = multiLineString; // TODO potrzeban funkcja clone
        return cloned;
    }

	private MultiLineString multiLineString = null;
}
