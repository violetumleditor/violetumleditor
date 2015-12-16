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

import java.util.ArrayList;
import java.util.List;

import com.horstmann.violet.framework.util.string.MultiLineString;
import com.horstmann.violet.framework.util.string.OneLineString;

/**
 * A string that can extend over multiple lines.
 */
public class MultiLineText extends LineText {
	/**
	 * Constructs an empty, centered, normal size multiline string that is not
	 * underlined.
	 */
	public MultiLineText() {}

    /**
     * Gets the value of the text property.
     *
     * @return the text of the multiline string
     */
    public String getText() {
        return text.toEditor();
    }

	/**
	 * Sets the value of the text property.
	 * 
	 * @param newValue
	 *            the text of the multiline string
	 */
	public void setText(String newValue) {
		text = convertToMultiLineString(newValue);

        this.setLabelText(text.toHTML());
	}

    public String toString() {
        return text.toEditor().replace('\n', '|');
    }

    public MultiLineText clone() {
        MultiLineText cloned = new MultiLineText();
        cloned.text = text; // TODO potrzeban funkcja clone
        return cloned;
    }

    protected MultiLineString convertToMultiLineString(String rawText)
    {
        List<OneLineString> rows = new ArrayList<OneLineString>();
        String[] array = this.explode(rawText, "\n");

        for (String rawRow: array) {
            rows.add(new OneLineString(rawRow));
        }
        return new MultiLineString(rows);
    }

	final protected String[] explode(String text, String separator)
	{
		return text.split(separator, -1);
	}

	protected MultiLineString text = new MultiLineString();
}
