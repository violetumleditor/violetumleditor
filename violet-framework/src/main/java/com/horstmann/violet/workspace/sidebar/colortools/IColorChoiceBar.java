package com.horstmann.violet.workspace.sidebar.colortools;

import com.horstmann.violet.workspace.sidebar.ISideBarElement;

public interface IColorChoiceBar extends ISideBarElement
{

    public void addColorChoiceChangeListener(IColorChoiceChangeListener listener);
    
}
