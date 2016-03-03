package com.horstmann.violet.product.diagram.abstracts;

import java.awt.Color;

public interface IColorable
{

    void setBackgroundColor(Color bgColor);
    
    Color getBackgroundColor();
    
    void setBorderColor(Color borderColor);

    Color getBorderColor();
    
    void setTextColor(Color textColor);
    
    Color getTextColor();
    
}
