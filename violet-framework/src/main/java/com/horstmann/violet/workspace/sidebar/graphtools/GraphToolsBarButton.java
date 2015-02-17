package com.horstmann.violet.workspace.sidebar.graphtools;

import com.horstmann.violet.framework.swingextension.CustomToggleButton;
import com.horstmann.violet.framework.swingextension.CustomToggleButtonGraphToolUI;
import com.horstmann.violet.framework.theme.ThemeManager;

/**
 * Button embedding a graph tool
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class GraphToolsBarButton extends CustomToggleButton
{

    /**
     * Default constructor
     * 
     * @param aTool to be displayed as a button
     */
    public GraphToolsBarButton(GraphTool aTool)
    {
        super(aTool.getLabel(), aTool.getIcon());
        setUI(new CustomToggleButtonGraphToolUI(ThemeManager.getInstance().getTheme().getToggleButtonSelectedColor(), ThemeManager
                .getInstance().getTheme().getToggleButtonSelectedBorderColor(), ThemeManager.getInstance().getTheme()
                .getToggleButtonUnselectedColor()));
        this.tool = aTool;
    }

    /**
     * @return embedded tool
     */
    public GraphTool getTool()
    {
        return tool;
    }

    /**
     * Embedded graph tool
     */
    private GraphTool tool;

}
