package com.horstmann.violet.framework.graphics.content;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 21.12.2015
 */
public class EmptyContent extends Content {
    @Override
    public void draw(Graphics2D g2) {}

    protected void setWidth(int width) {}
    protected void setHeight(int height) {}

    @Override
    public void setMinWidth(int minWidth){
        if(0<=minWidth) {
            super.setMinWidth(minWidth);
            super.setWidth(minWidth);
            refreshUp();
        }
    }
    @Override
    public void setMinHeight(int minHeight){
        if(0<=minHeight) {
            super.setMinHeight(minHeight);
            super.setHeight(minHeight);
            refreshUp();
        }
    }
}
