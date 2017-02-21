package com.horstmann.violet.framework.graphics.content;

import java.awt.Graphics2D;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 12.01.2016
 */
public class TestingRefreshContent extends Content
{
    @Override
    public void draw(Graphics2D graphics) {}

    @Override
    protected void refreshUp()
    {
        ++refreshUpCount;
        super.refreshUp();
    }
    @Override
    protected void refreshDown()
    {
        ++refreshDownCount;
        super.refreshDown();
    }

    public void setAsParent(Content child)
    {
        child.addParent(this);
    }

    public void setAsChildren(Content parent)
    {
        this.addParent(parent);
    }

    public int refreshUpCount = 0;
    public int refreshDownCount = 0;
}
