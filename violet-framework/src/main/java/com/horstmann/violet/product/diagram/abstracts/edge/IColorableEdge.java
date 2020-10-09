package com.horstmann.violet.product.diagram.abstracts.edge;

import java.awt.Color;

public interface IColorableEdge
{

    void setBackgroundColor(Color bgColor);
    
    Color getBackgroundColor();
    
    void setBorderColor(Color borderColor);

    Color getBorderColor();
    
    void setTextColor(Color textColor);
    
    Color getTextColor();
    
}
