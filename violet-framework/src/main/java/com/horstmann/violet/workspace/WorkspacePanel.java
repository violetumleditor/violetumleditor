package com.horstmann.violet.workspace;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.RepaintManager;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import com.horstmann.violet.framework.swingextension.TinyScrollBarUI;
import com.horstmann.violet.framework.theme.ThemeManager;
import com.horstmann.violet.workspace.editorpart.EditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.sidebar.ISideBar;

public class WorkspacePanel extends JPanel
{

    public WorkspacePanel(IWorkspace workspace)
    {
        this.workspace = workspace;
    }

    public void prepareLayout()
    {
        LayoutManager layout = new BorderLayout();
        setLayout(layout);
        JScrollPane scrollGPanel = getScrollableEditorPart();
        add(scrollGPanel, BorderLayout.CENTER);
        JScrollPane scrollSideBarPanel = getScrollableSideBar();
        add(scrollSideBarPanel, BorderLayout.WEST);
//        JScrollPane scrollStatusBarPanel = getScrollableStatusBar();
//        add(scrollStatusBarPanel, BorderLayout.SOUTH);
        refreshDisplay();
    }

    
    

    /**
     * @return the scrollable panel containing the editor
     */
    public JScrollPane getScrollableEditorPart()
    {
        if (this.scrollableEditorPart == null)
        {
            final IEditorPart editorPart = this.workspace.getEditorPart();
            final Component panel = editorPart.getSwingComponent();
            
            this.scrollableEditorPart = new JScrollPane() {
                @Override
                public void paint(Graphics g)
                {
                	editorPart.getSwingComponent().invalidate();
                    super.paint(g);
                }
                
            };
            this.scrollableEditorPart.getViewport().setView(panel);
            this.scrollableEditorPart.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener()
            {
                
                @Override
                public void adjustmentValueChanged(AdjustmentEvent e)
                {
                	editorPart.getSwingComponent().invalidate();
                    editorPart.getSwingComponent().repaint();
                }
            });
            this.scrollableEditorPart.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener()
            {
                
                @Override
                public void adjustmentValueChanged(AdjustmentEvent e)
                {
                    editorPart.getSwingComponent().invalidate();
                    editorPart.getSwingComponent().repaint();
                }
            });
            this.scrollableEditorPart.setBackground(ThemeManager.getInstance().getTheme().getWhiteColor());
            this.scrollableEditorPart.setBorder(new EmptyBorder(0, 0, 0, 0));
            this.scrollableEditorPart.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        }
        return this.scrollableEditorPart;
    }

    /**
     * @param workspace TODO
     * @return scrollpane containing sidebar
     */
    public JScrollPane getScrollableSideBar()
    {
        if (this.scrollableSideBar == null)
        {
            ISideBar sideBar = this.workspace.getSideBar();
            this.scrollableSideBar = new JScrollPane(sideBar.getAWTComponent());
            this.scrollableSideBar.setAlignmentY(Component.TOP_ALIGNMENT);
            this.scrollableSideBar.getHorizontalScrollBar().setUI(new TinyScrollBarUI());
            this.scrollableSideBar.getVerticalScrollBar().setUI(new TinyScrollBarUI());
//            this.scrollableSideBar.setBorder(new MatteBorder(0, 1, 0, 0, ThemeManager.getInstance().getTheme()
//                    .getSidebarBorderColor()));
            this.scrollableSideBar.setBorder(new EmptyBorder(0, 0, 0, 0));
            this.scrollableSideBar.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            this.scrollableSideBar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        }
        return this.scrollableSideBar;
    }



    public void refreshDisplay()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                WorkspacePanel.this.revalidate();
                WorkspacePanel.this.repaint();
            }
        });
    }



    private IWorkspace workspace;
    private JScrollPane scrollableSideBar;
    private JScrollPane scrollableEditorPart;
    private JScrollPane scrollableStatusBar;


}
