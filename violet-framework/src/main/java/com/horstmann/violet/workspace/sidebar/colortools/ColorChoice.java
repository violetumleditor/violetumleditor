package com.horstmann.violet.workspace.sidebar.colortools;

import java.awt.Color;

public class ColorChoice
{

    private Color backgroundColor;

    private Color borderColor;

    private Color textColor;

    public ColorChoice(Color backgroundColor, Color borderColor, Color textColor)
    {
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.textColor = textColor;
    }

    public Color getBackgroundColor()
    {
        return backgroundColor;
    }

    public Color getBorderColor()
    {
        return borderColor;
    }

    public Color getTextColor()
    {
        return textColor;
    }

}
