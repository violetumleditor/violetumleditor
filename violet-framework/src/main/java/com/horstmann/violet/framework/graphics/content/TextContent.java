package com.horstmann.violet.framework.graphics.content;

import com.horstmann.violet.product.diagram.abstracts.property.string.LineText;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Adrian Bobrowski on 21.12.2015.
 */
public class TextContent extends Content {
    public TextContent(LineText text)
    {
        this.text = text;
    }

    @Override
    public void draw(Graphics2D g2, Point2D offset) {
        Rectangle2D textBounds = getBounds();
        text.draw(g2, new Rectangle2D.Double(offset.getX(), offset.getY(), textBounds.getWidth(), textBounds.getHeight()));
    }

    @Override
    public Rectangle2D getBounds()
    {
        Rectangle2D textBounds = text.getBounds();
        return new Rectangle2D.Double(0,0,Math.max(textBounds.getWidth(),width),Math.max(textBounds.getHeight(),height));
    }

    protected LineText text;
}
