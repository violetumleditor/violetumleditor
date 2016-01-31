package com.horstmann.violet.framework.graphics.content;

import java.awt.*;
import java.awt.geom.Point2D;

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
     * @see Content#setWidth(int)
     */
    @Override
    protected void setWidth(int width)
    {}

    /**
     * @see Content#setHeight(int)
     */
    @Override
    protected void setHeight(int height) {}

    /**
     * @see Content#setMinWidth(int)
     */
    @Override
    public void setMinWidth(int minWidth){
        super.setMinWidth(minWidth);
        super.setWidth(minWidth);
        refreshUp();
    }

    /**
     * @see Content#setMinHeight(int)
     */
    @Override
    public void setMinHeight(int minHeight){
        super.setMinHeight(minHeight);
        super.setHeight(minHeight);
        refreshUp();
    }
}
