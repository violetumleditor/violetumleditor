package com.horstmann.violet.framework.graphics.content;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 06.03.2016
 */
public class PaddingContent extends Content
{
    public PaddingContent(Content content, double padding)
    {
        this(content, padding, padding);
    }

    public PaddingContent(Content content, double vertical, double horizontal)
    {
        this(content,vertical, horizontal, vertical, horizontal);
    }

    public PaddingContent(Content content, double top, double left, double bottom, double right)
    {
        this.content = content;
        this.top = new EmptyContent();
        this.left = new EmptyContent();
        this.bottom = new EmptyContent();
        this.right = new EmptyContent();

        setTopPadding(top);
        setLeftPadding(left);
        setBottomPadding(bottom);
        setRightPadding(right);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(this.left);
        horizontalLayout.add(this.content);
        horizontalLayout.add(this.right);

        layout = new VerticalLayout();
        layout.add(this.top);
        layout.add(horizontalLayout);
        layout.add(this.bottom);

        this.content.addParent(this);
    }

    /**
     * Defines how to draw element
     *
     * @param graphics
     */
    @Override
    public final void draw(Graphics2D graphics)
    {
        layout.draw(graphics);
    }

    /**
     * @see Content#refreshDown()
     */
    @Override
    protected void refreshDown()
    {
        content.refreshDown();
        super.refreshDown();
    }

    /**
     * @return minimal bounds of this element
     */
    public Rectangle2D getMinimalBounds()
    {
        return layout.getMinimalBounds();
    }

    public final void setTopPadding(double top)
    {
        if(0>top)
        {
            throw new IllegalArgumentException("top padding can't be negative");
        }
        this.top.setMinHeight(top);
        refreshUp();
    }

    public final void setLeftPadding(double left)
    {
        if(0>left)
        {
            throw new IllegalArgumentException("left padding can't be negative");
        }
        this.left.setMinWidth(left);
        refreshUp();
    }

    public final void setBottomPadding(double bottom)
    {
        if(0>bottom)
        {
            throw new IllegalArgumentException("bottom padding can't be negative");
        }
        this.bottom.setMinHeight(bottom);
        refreshUp();
    }

    public final void setRightPadding(double right)
    {
        if(0>right)
        {
            throw new IllegalArgumentException("right padding can't be negative");
        }
        this.right.setMinWidth(right);
        refreshUp();
    }

    private Content content;
    private Layout layout;
    private EmptyContent top;
    private EmptyContent left;
    private EmptyContent bottom;
    private EmptyContent right;
}
