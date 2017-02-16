package com.horstmann.violet.product.diagram.abstracts.node;

import java.awt.Color;

public interface IColorableNode
{

    void setBackgroundColor(Color bgColor);
    
    Color getBackgroundColor();
    
    void setBorderColor(Color borderColor);

    Color getBorderColor();
    
    void setTextColor(Color textColor);
    
    Color getTextColor();
    
}
