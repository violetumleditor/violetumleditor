package com.horstmann.violet.product.diagram.abstracts.edge;

import com.horstmann.violet.framework.property.BentStyleChoiceList;
import com.horstmann.violet.framework.property.LineStyleChoiceList;
import com.horstmann.violet.framework.property.choiceList.ChoiceList;
import com.horstmann.violet.product.diagram.abstracts.edge.bentstyle.BentStyle;

import java.awt.*;

/**
 * An edge that is composed of multiple line segments
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 21.02.2016
 */
public abstract class LineEdge extends ShapeEdge
{
    public LineEdge()
    {
        super();
        lineStyleChoiceList = new LineStyleChoiceList();
        bentStyleChoiceList = new BentStyleChoiceList();

        this.selectedBentStyle = this.bentStyleChoiceList.getSelectedPos();
        this.selectedLineStyle = this.lineStyleChoiceList.getSelectedPos();
    }

    protected LineEdge(LineEdge lineEdge)
    {
        this.bentStyleChoiceList = lineEdge.bentStyleChoiceList.clone();
        this.lineStyleChoiceList = lineEdge.lineStyleChoiceList.clone();
        this.selectedBentStyle = this.bentStyleChoiceList.getSelectedPos();
        this.selectedLineStyle = this.lineStyleChoiceList.getSelectedPos();
    }

    @Override
    public void deserializeSupport()
    {
        super.deserializeSupport();
        lineStyleChoiceList = new LineStyleChoiceList();
        bentStyleChoiceList = new BentStyleChoiceList();

        bentStyleChoiceList.setSelectedIndex(selectedBentStyle);
        lineStyleChoiceList.setSelectedIndex(selectedLineStyle);
    }

    /**
     * Gets the bentStyle property
     *
     * @return the bent style list
     */
    public final ChoiceList getBentStyleChoiceList()
    {
        return bentStyleChoiceList;
    }

    /**
     * Sets the bentStyle property
     *
     * @param bentStyleChoiceList the bent style list
     */
    public final void setBentStyleChoiceList(ChoiceList bentStyleChoiceList)
    {
        this.bentStyleChoiceList = (BentStyleChoiceList)bentStyleChoiceList;
        this.selectedBentStyle = this.bentStyleChoiceList.getSelectedPos();
    }

    /**
     * Gets the line style property.
     *
     * @return the line style list
     */
    public final ChoiceList getLineStyleChoiceList()
    {
        return lineStyleChoiceList;
    }

    /**
     * Sets the line style property.
     *
     * @param lineStyleChoiceList the line style list
     */
    public final void setLineStyleChoiceList(ChoiceList lineStyleChoiceList)
    {
        this.lineStyleChoiceList = (LineStyleChoiceList)lineStyleChoiceList;
        this.selectedLineStyle = this.lineStyleChoiceList.getSelectedPos();
    }

    /**
     * Gets the line style.
     *
     * @return the line style
     */
    public final Stroke getLineStyle()
    {
        return lineStyleChoiceList.getSelectedValue();
    }

    protected final void setLineStyle(Stroke stroke)
    {
        if(lineStyleChoiceList.setSelectedValue(stroke))
        {
            this.selectedLineStyle = lineStyleChoiceList.getSelectedPos();
        }
    }

    /**
     * Gets the bent style.
     *
     * @return the bent style
     */
    public final BentStyle getBentStyle()
    {
        return bentStyleChoiceList.getSelectedValue();
    }

    protected final void setBentStyle(BentStyle bentStyle)
    {
        if(bentStyleChoiceList.setSelectedValue(bentStyle))
        {
            this.selectedBentStyle = bentStyleChoiceList.getSelectedPos();
        }
    }

    private transient LineStyleChoiceList lineStyleChoiceList;
    private transient BentStyleChoiceList bentStyleChoiceList;

    private int selectedBentStyle;
    private int selectedLineStyle;
}
