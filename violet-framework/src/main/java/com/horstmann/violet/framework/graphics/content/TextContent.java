package com.horstmann.violet.framework.graphics.content;

import com.horstmann.violet.product.diagram.abstracts.property.string.LineText;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by Adrian Bobrowski on 21.12.2015.
 */
public class TextContent extends Content implements LineText.ChangeListener
{
    public TextContent(LineText text)
    {
        this.text = text;
        this.text.addChangeListener(this);
    }

    @Override
    public void onChange() {
        refresh();
    }

    @Override
    public void draw(Graphics2D g2) {
        text.draw(g2);
    }

    @Override
    public void refreshUp()
    {
        Rectangle2D textBounds = text.getBounds();
        setWidth((int)textBounds.getWidth());
        setHeight((int)textBounds.getHeight());
        super.refreshUp();
    }

    private LineText text;
}
