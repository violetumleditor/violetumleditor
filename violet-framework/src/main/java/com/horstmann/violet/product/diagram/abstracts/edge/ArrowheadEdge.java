package com.horstmann.violet.product.diagram.abstracts.edge;

import com.horstmann.violet.framework.property.ArrowheadChoiceList;
import com.horstmann.violet.framework.property.BentStyleChoiceList;
import com.horstmann.violet.framework.property.LineStyleChoiceList;
import com.horstmann.violet.framework.property.choiceList.ChoiceList;
import com.horstmann.violet.product.diagram.abstracts.edge.arrowhead.Arrowhead;
import com.horstmann.violet.product.diagram.abstracts.edge.bentstyle.BentStyle;

import java.awt.*;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 21.02.2016
 */
public abstract class ArrowheadEdge extends LineEdge
{
    public ArrowheadEdge()
    {
        super();

        startArrowheadChoiceList = new ArrowheadChoiceList();
        endArrowheadChoiceList = new ArrowheadChoiceList();

        this.selectedStartArrowhead = this.startArrowheadChoiceList.getSelectedPos();
        this.selectedEndArrowhead = this.endArrowheadChoiceList.getSelectedPos();
    }

    protected ArrowheadEdge(ArrowheadEdge arrowheadEdge)
    {
        super(arrowheadEdge);
        this.startArrowheadChoiceList = arrowheadEdge.startArrowheadChoiceList.clone();
        this.endArrowheadChoiceList = arrowheadEdge.endArrowheadChoiceList.clone();
        this.selectedStartArrowhead = this.startArrowheadChoiceList.getSelectedPos();
        this.selectedEndArrowhead = this.endArrowheadChoiceList.getSelectedPos();
    }

    @Override
    public void deserializeSupport()
    {
        super.deserializeSupport();

        startArrowheadChoiceList = new ArrowheadChoiceList();
        endArrowheadChoiceList = new ArrowheadChoiceList();

        startArrowheadChoiceList.setSelectedIndex(selectedStartArrowhead);
        endArrowheadChoiceList.setSelectedIndex(selectedEndArrowhead);
    }

    /**
     * Gets the start arrow head property
     *
     * @return the start arrow head style list
     */
    public final ChoiceList getStartArrowheadChoiceList()
    {
        return startArrowheadChoiceList;
    }

    /**
     * Sets the start arrow head property
     *
     * @param startArrowheadChoiceList the start arrow head list
     */
    public final void setStartArrowheadChoiceList(ChoiceList startArrowheadChoiceList)
    {
        this.startArrowheadChoiceList = (ArrowheadChoiceList)startArrowheadChoiceList;
        this.selectedStartArrowhead = this.startArrowheadChoiceList.getSelectedPos();
    }

    /**
     * Gets the end arrow head property
     *
     * @return the end arrow head style list
     */
    public final ChoiceList getEndArrowheadChoiceList()
    {
        return endArrowheadChoiceList;
    }

    /**
     * Sets the end arrow head property
     *
     * @param endArrowheadChoiceList the end arrow head list
     */
    public final void setEndArrowheadChoiceList(ChoiceList endArrowheadChoiceList)
    {
        this.endArrowheadChoiceList = (ArrowheadChoiceList)endArrowheadChoiceList;
        this.selectedEndArrowhead = this.endArrowheadChoiceList.getSelectedPos();
    }

    /**
     * Gets the start arrowhead style.
     *
     * @return the start arrowhead style
     */
    public final Arrowhead getStartArrowhead()
    {
        return startArrowheadChoiceList.getSelectedValue();
    }

    protected final void setStartArrowhead(Arrowhead startArrowhead)
    {
        if(startArrowheadChoiceList.setSelectedValue(startArrowhead))
        {
            this.selectedStartArrowhead = startArrowheadChoiceList.getSelectedPos();
        }
    }

    /**
     * Gets the end arrowhead style.
     *
     * @return the end arrowhead style
     */
    public final Arrowhead getEndArrowhead()
    {
        return endArrowheadChoiceList.getSelectedValue();
    }

    protected final void setEndArrowhead(Arrowhead endArrowhead)
    {
        if(endArrowheadChoiceList.setSelectedValue(endArrowhead))
        {
            this.selectedEndArrowhead = endArrowheadChoiceList.getSelectedPos();
        }
    }

    private transient ArrowheadChoiceList startArrowheadChoiceList = new ArrowheadChoiceList();
    private transient ArrowheadChoiceList endArrowheadChoiceList = new ArrowheadChoiceList();

    private int selectedStartArrowhead;
    private int selectedEndArrowhead;
}
