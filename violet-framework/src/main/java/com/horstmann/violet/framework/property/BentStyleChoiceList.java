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

package com.horstmann.violet.framework.property;

import com.horstmann.violet.framework.property.choiceList.TextChoiceList;
import com.horstmann.violet.product.diagram.abstracts.edge.bentstyle.BentStyle;

/**
 * A style for a segmented line that indicates the number and sequence of bends.
 */
public class BentStyleChoiceList extends TextChoiceList<BentStyle>
{
    /**
     * Default constructor
     */
    public BentStyleChoiceList()
    {
        super(BENT_STYLE_KEYS, BENT_STYLE);
    }

    /**
     * Copy constructor
     *
     * @param copyElement
     */
    protected BentStyleChoiceList(BentStyleChoiceList copyElement)
    {
        super(copyElement);
    }

    @Override
    public BentStyleChoiceList clone()
    {
        return new BentStyleChoiceList(this);
    }

    public static final BentStyle STRAIGHT = BentStyle.STRAIGHT;
    public static final BentStyle FREE = BentStyle.FREE;
    public static final BentStyle HV = BentStyle.HV;
    public static final BentStyle VH = BentStyle.VH;
    public static final BentStyle HVH = BentStyle.HVH;
    public static final BentStyle VHV = BentStyle.VHV;
    public static final BentStyle AUTO = BentStyle.AUTO;

    private static BentStyle[] BENT_STYLE;
    private static String[] BENT_STYLE_KEYS;

    static
    {
        BENT_STYLE = new BentStyle[]{
                AUTO,
                STRAIGHT,
                FREE,
                HV,
                VH,
                HVH,
                VHV
        };

        BENT_STYLE_KEYS = new String[]{
                "AUTO",
                "STRAIGHT",
                "FREE",
                "HV",
                "VH",
                "HVH",
                "VHV"
        };
    }
}
