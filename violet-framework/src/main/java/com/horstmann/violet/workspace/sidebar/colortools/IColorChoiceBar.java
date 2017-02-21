package com.horstmann.violet.workspace.sidebar.colortools;

import com.horstmann.violet.workspace.sidebar.ISideBarElement;
import java.awt.Cursor;

public interface IColorChoiceBar extends ISideBarElement
{

    public static final Cursor CUTSOM_CURSOR = new Cursor(Cursor.CROSSHAIR_CURSOR);
    
    public void addColorChoiceChangeListener(IColorChoiceChangeListener listener);
    
    public void resetSelection();
    
}
