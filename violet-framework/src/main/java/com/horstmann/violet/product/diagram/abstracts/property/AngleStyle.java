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

import com.horstmann.violet.framework.util.SerializableEnumeration;

/**
 * A style for the corners (angles) of a segmented line edge.
 * <ul>
 *   <li>{@link #RAW} &ndash; sharp corners (default)</li>
 *   <li>{@link #ROUNDED1} &ndash; rounded with a radius of 8 px (16 px arc total)</li>
 *   <li>{@link #ROUNDED2} &ndash; rounded with a radius of 16 px (32 px arc total)</li>
 *   <li>{@link #ROUNDED3} &ndash; rounded with a radius of 32 px (64 px arc total)</li>
 * </ul>
 */
public class AngleStyle extends SerializableEnumeration
{
    private AngleStyle()
    {
    }

    /**
     * Returns the rounding radius in pixels for this style.
     * For {@link #RAW} returns 0 (no rounding).
     */
    public int getRadius()
    {
        if (this == ROUNDED1) return 8;
        if (this == ROUNDED2) return 16;
        if (this == ROUNDED3) return 32;
        return 0;
    }

    /** Sharp corners &ndash; no rounding. */
    public static final AngleStyle RAW = new AngleStyle();
    /** Rounded corners &ndash; 8 px radius (16 px arc). */
    public static final AngleStyle ROUNDED1 = new AngleStyle();
    /** Rounded corners &ndash; 16 px radius (32 px arc). */
    public static final AngleStyle ROUNDED2 = new AngleStyle();
    /** Rounded corners &ndash; 32 px radius (64 px arc). */
    public static final AngleStyle ROUNDED3 = new AngleStyle();
}
