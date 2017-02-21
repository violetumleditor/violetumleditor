package com.horstmann.violet.workspace.sidebar.colortools;

import java.awt.Cursor;

import com.horstmann.violet.workspace.sidebar.ISideBarElement;

public interface IColorChoiceBar extends ISideBarElement
{

    public static final Cursor CUTSOM_CURSOR = new Cursor(Cursor.CROSSHAIR_CURSOR);
    
    public void addColorChoiceChangeListener(IColorChoiceChangeListener listener);
    
    public void resetSelection();
    
}
