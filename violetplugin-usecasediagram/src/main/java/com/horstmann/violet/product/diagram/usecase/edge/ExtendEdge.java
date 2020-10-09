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

package com.horstmann.violet.product.diagram.usecase.edge;

import com.horstmann.violet.product.diagram.abstracts.edge.bentstyle.BentStyle;
import com.horstmann.violet.product.diagram.common.edge.LabeledLineEdge;
import com.horstmann.violet.product.diagram.property.ArrowheadChoiceList;
import com.horstmann.violet.product.diagram.property.LineStyleChoiceList;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.decorator.OneLineText;
import com.horstmann.violet.product.diagram.property.text.decorator.PrefixDecorator;
import com.horstmann.violet.product.diagram.usecase.UseCaseDiagramConstant;

/**
 * An edge that is shaped like a line with up to three segments with an arrowhead
 */
public class ExtendEdge extends LabeledLineEdge
{
    public ExtendEdge()
    {
        super();
        setBentStyle(BentStyle.STRAIGHT);
        setEndArrowhead(ArrowheadChoiceList.V);
        setLineStyle(LineStyleChoiceList.DOTTED);
        getCenterLabel().setConverter(EXTEND_CONVERTER);
    }

    protected ExtendEdge(ExtendEdge clone)
    {
        super(clone);
        setCenterLabel(getCenterLabel());
    }

    @Override
    public ExtendEdge copy()
    {
        return new ExtendEdge(this);
    }

    @Override
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();
        setBentStyle(BentStyle.STRAIGHT);
        setEndArrowhead(ArrowheadChoiceList.V);
        setLineStyle(LineStyleChoiceList.DOTTED);
        getCenterLabel().setConverter(EXTEND_CONVERTER);
        setCenterLabel(getCenterLabel());
    }

    @Override
    public String getToolTip()
    {
        return UseCaseDiagramConstant.USE_CASE_DIAGRAM_RESOURCE.getString("tooltip.extend");
    }

    private static final String EXTEND = "&laquo;extend&raquo;";
    private static final LineText.Converter EXTEND_CONVERTER = new LineText.Converter()
    {
        @Override
        public OneLineText toLineString(String text)
        {
            return new PrefixDecorator(new OneLineText(text), EXTEND);
        }
    };
}
