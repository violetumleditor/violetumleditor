package com.horstmann.violet.product.diagram.abstracts;

import java.awt.Color;

public interface IColorable
{

    public void setBackgroundColor(Color bgColor);
    
    public Color getBackgroundColor();
    
    public void setBorderColor(Color borderColor);

    public Color getBorderColor();
    
    public void setTextColor(Color textColor);
    
    public Color getTextColor();
    
}
