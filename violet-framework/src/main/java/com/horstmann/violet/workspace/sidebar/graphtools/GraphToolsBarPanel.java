package com.horstmann.violet.workspace.sidebar.graphtools;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class GraphToolsBarPanel extends JPanel
{

    
    

    public GraphToolsBarPanel(GraphToolsBar umlToolsPanel)
    {
        super();
        this.graphToolsPanel = umlToolsPanel;
        this.nodeButtons = getToggleButtons(this.graphToolsPanel.getNodeTools());
        this.edgeButtons = getToggleButtons(this.graphToolsPanel.getEdgeTools());
        this.graphToolsPanel.addListener(getGraphToolsPanelListener());
    }
    
    
    

    

    private IGraphToolsBarListener getGraphToolsPanelListener() {
        if (this.listener == null) {
            this.listener = new IGraphToolsBarListener() {
                public void toolSelectionChanged(GraphTool selectedTool)
                {
                    for (GraphToolsBarButton aButton : nodeButtons) {
                        if (aButton.getTool().equals(selectedTool)) {
                            setSelectedButton(aButton);
                            return;
                        }
                    }
                    for (GraphToolsBarButton aButton : edgeButtons) {
                        if (aButton.getTool().equals(selectedTool)) {
                            setSelectedButton(aButton);
                            return;
                        }
                    }
                }
            };
        }
        return this.listener;
    }
    
    /**
     * @param diagram tools
     * @return buttons representing tools
     */
    private List<GraphToolsBarButton> getToggleButtons(List<GraphTool> tools)
    {
        List<GraphToolsBarButton> buttons = new ArrayList<GraphToolsBarButton>();
        for (GraphTool aTool : tools)
        {
            final GraphToolsBarButton button = new GraphToolsBarButton(aTool);
            buttons.add(button);
        }
        return buttons;
    }
    
    /**
     * @return curretly selected button
     */
    private GraphToolsBarButton getSelectedButton()
    {
        for (GraphToolsBarButton button : this.nodeButtons)
        {
            if (button.isSelected())
            {
                return button;
            }
        }
        for (GraphToolsBarButton button : this.edgeButtons)
        {
            if (button.isSelected())
            {
                return button;
            }
        }            

        return this.nodeButtons.get(0);
    }
    
    /**
     * @return all node_old buttons
     */
    public List<GraphToolsBarButton> getNodeButtons() {
        return this.nodeButtons;
    }






    /**
     * @return all edge buttons
     */
    public List<GraphToolsBarButton> getEdgeButtons() {
        return this.edgeButtons;
    }






    public void selectNextButton()
    {
        int nextPos = 0;
        GraphToolsBarButton selectedButton = getSelectedButton();
        int posForNodes = this.nodeButtons.indexOf(selectedButton);
        if (posForNodes >= 0)
        {
            nextPos = posForNodes + 1;
            if (nextPos < this.nodeButtons.size())
            {
                setSelectedButton(this.nodeButtons.get(nextPos));
            }
            if (nextPos >= this.nodeButtons.size() && this.edgeButtons.size() > 0)
            {
                setSelectedButton(this.edgeButtons.get(0));
            }
            return;
        }
        int posForEdges = this.edgeButtons.indexOf(selectedButton);
        if (posForEdges >= 0)
        {
            nextPos = posForEdges + 1;
            if (nextPos < this.edgeButtons.size())
            {
                setSelectedButton(this.edgeButtons.get(nextPos));
            }
            return;
        }
    }






    public void selectPreviousButton()
    {
        int previousPos = 0;
        GraphToolsBarButton selectedButton = getSelectedButton();
        int posForNodes = this.nodeButtons.indexOf(selectedButton);
        if (posForNodes >= 0)
        {
            previousPos = posForNodes - 1;
            if (previousPos >= 0)
            {
                setSelectedButton(this.nodeButtons.get(previousPos));
            }
            return;
        }
        int posForEdges = this.edgeButtons.indexOf(selectedButton);
        if (posForEdges >= 0)
        {
            previousPos = posForEdges - 1;
            if (previousPos >= 0)
            {
                setSelectedButton(this.edgeButtons.get(previousPos));
            }
            if (previousPos < 0 && this.nodeButtons.size() > 0)
            {
                setSelectedButton(this.nodeButtons.get(this.nodeButtons.size() - 1));
            }
            return;
        }
    }






    /**
     * Performs button select
     * 
     * @param selectedButton to be considered as selected
     */
    private void setSelectedButton(GraphToolsBarButton selectedButton)
    {
        for (GraphToolsBarButton button : this.nodeButtons)
        {
            if (button != selectedButton)
            {
                button.setSelected(false);
            }
            if (button == selectedButton)
            {
                button.setSelected(true);
                int pos = this.nodeButtons.indexOf(button);
                this.graphToolsPanel.setSelectedTool(this.graphToolsPanel.getNodeTools().get(pos));
            }
        }
        for (GraphToolsBarButton button : this.edgeButtons)
        {
            if (button != selectedButton)
            {
                button.setSelected(false);
            }
            if (button == selectedButton)
            {
                button.setSelected(true);
                int pos = this.edgeButtons.indexOf(button);
                this.graphToolsPanel.setSelectedTool(this.graphToolsPanel.getEdgeTools().get(pos));
            }
        }
    }

    private void notifyMouseEvent(GraphToolsBarButton selectedButton, MouseEvent event)
    {
        for (GraphToolsBarButton button : this.nodeButtons)
        {
            if (button == selectedButton)
            {
                int pos = this.nodeButtons.indexOf(button);
                this.graphToolsPanel.notifyMouseEvent(this.graphToolsPanel.getNodeTools().get(pos), event);
            }
        }
        for (GraphToolsBarButton button : this.edgeButtons)
        {
            if (button == selectedButton)
            {
                int pos = this.edgeButtons.indexOf(button);
                this.graphToolsPanel.notifyMouseEvent(this.graphToolsPanel.getEdgeTools().get(pos), event);
            }
        }
    }





    /**
     * @return panel containing node_old buttons
     */
    public JPanel getNodeButtonsPanel() {
        if (this.nodeButtonsPanel == null) {
            this.nodeButtonsPanel = getButtonPanel(this.nodeButtons);
        }
        return this.nodeButtonsPanel;
    }
    
    /**
     * @return panel containing edge buttons
     */
    public JPanel getEdgeButtonsPanel() {
        if (this.edgeButtonsPanel == null) {
            this.edgeButtonsPanel = getButtonPanel(this.edgeButtons);
        }
        return this.edgeButtonsPanel;
    }
    
    /**
     * Creates a panel that contains custom toggle buttons. Also sets mouse listeners.
     * 
     * @param buttons to be added to this panel
     * @return JPanel
     */
    private JPanel getButtonPanel(List<GraphToolsBarButton> buttons)
    {
        JPanel buttonPanel = new JPanel();
        for (final GraphToolsBarButton button : buttons)
        {
            button.addMouseListener(new MouseAdapter()
            {
            	public void mousePressed(MouseEvent arg0) 
            	{
            		notifyMouseEvent(button, arg0);
            	}
            	
            	public void mouseReleased(MouseEvent arg0) 
            	{
            		notifyMouseEvent(button, arg0);
            	}

                public void mouseClicked(MouseEvent arg0)
                {
                    setSelectedButton(button);
                }
            });
            button.addMouseMotionListener(new MouseAdapter() {
            	public void mouseDragged(MouseEvent arg0) 
            	{
            		notifyMouseEvent(button, arg0);
            	}
            });            
            buttonPanel.add(button);
        }

        buttonPanel.setLayout(new GridLayout(0, 1));
//        buttonPanel.addMouseWheelListener(new MouseWheelListener()
//        {
//
//            public void mouseWheelMoved(MouseWheelEvent e)
//            {
//                boolean isCtrl = (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0;
//                if (isCtrl) {
//                    return;
//                }
//                int scroll = e.getUnitsToScroll();
//                if (scroll > 0)
//                {
//                    selectNextButton();
//                }
//                if (scroll < 0)
//                {
//                    selectPreviousButton();
//                }
//            }
//
//        });
        return buttonPanel;
    }
    

    private IGraphToolsBarListener listener;
    private GraphToolsBar graphToolsPanel;
    private List<GraphToolsBarButton> nodeButtons;
    private List<GraphToolsBarButton> edgeButtons;
    private JPanel nodeButtonsPanel;
    private JPanel edgeButtonsPanel;
}
