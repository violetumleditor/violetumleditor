package com.horstmann.violet.workspace.sidebar.colortools;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.sidebar.ISideBarElement;
import com.horstmann.violet.workspace.sidebar.SideBar;

@ResourceBundleBean(resourceReference = SideBar.class)
public class ColorToolsPanel extends JPanel implements ISideBarElement
{

    public ColorToolsPanel()
    {
        ResourceBundleInjector.getInjector().inject(this);
        setUI(new ColorToolsPanelUI(this));
    }

    @Override
    public void install(IWorkspace workspace)
    {
        this.diagramPanel = workspace;
    }

    @Override
    public Component getAWTComponent()
    {
        return this;
    }
    
 
    public void addColorChoiceChangeListener(IColorChoiceChangeListener listener) {
        this.colorChoiceChangeListenersList.add(listener);
    }
    
    public void fireColorChoiceChanged(ColorChoice newColorChoice) {
        for (IColorChoiceChangeListener aListener : this.colorChoiceChangeListenersList) {
            aListener.onColorChoiceChange(newColorChoice);
        }
    }
    

    /**
     * Current diagram panel
     */
    private IWorkspace diagramPanel;


    // Source : http://www.tinygorilla.com/Easter_eggs/pallatehex.html
    private static final ColorChoice PASTEL_WHITE = new ColorChoice(Color.WHITE, Color.BLACK, Color.BLACK);
    private static final ColorChoice PASTEL_RED = new ColorChoice(new Color(246, 150, 121), Color.BLACK, Color.BLACK);
    private static final ColorChoice PASTEL_RED_ORANGE = new ColorChoice(new Color(249, 173, 129), Color.BLACK, Color.BLACK);
    private static final ColorChoice PASTEL_YELLOW_ORANCE = new ColorChoice(new Color(253, 198, 137), Color.BLACK, Color.BLACK);
    private static final ColorChoice PASTEL_YELLOW = new ColorChoice(new Color(255, 247, 153), Color.BLACK, Color.BLACK);
    private static final ColorChoice PASTEL_PEA_GREEN = new ColorChoice(new Color(196, 223, 155), Color.BLACK, Color.BLACK);
    private static final ColorChoice PASTEL_YELLOW_GREEN = new ColorChoice(new Color(163, 211, 156), Color.BLACK, Color.BLACK);
    private static final ColorChoice PASTEL_GREEN = new ColorChoice(new Color(130, 202, 156), Color.BLACK, Color.BLACK);
    private static final ColorChoice PASTEL_GREEN_CYAN = new ColorChoice(new Color(122, 204, 200), Color.BLACK, Color.BLACK);
    private static final ColorChoice PASTEL_CYAN = new ColorChoice(new Color(109, 207, 246), Color.BLACK, Color.BLACK);
    private static final ColorChoice PASTEL_CYAN_BLUE = new ColorChoice(new Color(125, 167, 217), Color.BLACK, Color.BLACK);
    private static final ColorChoice PASTEL_BLUE = new ColorChoice(new Color(131, 147, 202), Color.BLACK, Color.BLACK);
    private static final ColorChoice PASTEL_BLUE_VIOLET = new ColorChoice(new Color(135, 129, 189), Color.BLACK, Color.BLACK);
    private static final ColorChoice PASTEL_VIOLET = new ColorChoice(new Color(161, 134, 190), Color.BLACK, Color.BLACK);
    private static final ColorChoice PASTEL_VIOLET_MAGENTA = new ColorChoice(new Color(189, 140, 191), Color.BLACK, Color.BLACK);
    private static final ColorChoice PASTEL_MAGENTA = new ColorChoice(new Color(244, 154, 193), Color.BLACK, Color.BLACK);
    private static final ColorChoice PASTEL_MAGENTA_RED = new ColorChoice(new Color(245, 152, 157), Color.BLACK, Color.BLACK);

    protected static final List<ColorChoice> CHOICE_LIST = new ArrayList<ColorChoice>();
    private List<IColorChoiceChangeListener> colorChoiceChangeListenersList = new ArrayList<IColorChoiceChangeListener>();

    static
    {
        CHOICE_LIST.add(PASTEL_WHITE);
        CHOICE_LIST.add(PASTEL_RED);
        CHOICE_LIST.add(PASTEL_RED_ORANGE);
        CHOICE_LIST.add(PASTEL_YELLOW_ORANCE);
        CHOICE_LIST.add(PASTEL_YELLOW);
        CHOICE_LIST.add(PASTEL_PEA_GREEN);
        CHOICE_LIST.add(PASTEL_YELLOW_GREEN);
        CHOICE_LIST.add(PASTEL_GREEN);
        CHOICE_LIST.add(PASTEL_GREEN_CYAN);
        CHOICE_LIST.add(PASTEL_CYAN);
        CHOICE_LIST.add(PASTEL_CYAN_BLUE);
        CHOICE_LIST.add(PASTEL_BLUE);
        CHOICE_LIST.add(PASTEL_BLUE_VIOLET);
        CHOICE_LIST.add(PASTEL_VIOLET);
        CHOICE_LIST.add(PASTEL_VIOLET_MAGENTA);
        CHOICE_LIST.add(PASTEL_MAGENTA);
        CHOICE_LIST.add(PASTEL_MAGENTA_RED);
    }

}
