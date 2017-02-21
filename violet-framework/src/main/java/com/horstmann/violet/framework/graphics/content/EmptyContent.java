package com.horstmann.violet.framework.graphics.content;

import java.awt.Graphics2D;

/**
 * @see Content
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 21.12.2015
 */
public class EmptyContent extends Content
{
    /**
     * @see Content#draw(Graphics2D)
     */
    @Override
    public void draw(Graphics2D graphics)
    {}

    /**
     * @see Content#setWidth(double)
     */
    @Override
    protected void setWidth(double width)
    {}

    /**
     * @see Content#setHeight(double)
     */
    @Override
    protected void setHeight(double height) {}

    /**
     * @see Content#setMinWidth(double)
     */
    @Override
    public void setMinWidth(double minWidth){
        super.setMinWidth(minWidth);
        super.setWidth(minWidth);
        refreshUp();
    }

    /**
     * @see Content#setMinHeight(double)
     */
    @Override
    public void setMinHeight(double minHeight){
        super.setMinHeight(minHeight);
        super.setHeight(minHeight);
        refreshUp();
    }
}
