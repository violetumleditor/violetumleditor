package com.horstmann.violet.workspace.editorpart;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.plaf.PanelUI;

import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.workspace.editorpart.behavior.IEditorPartBehavior;

public class EditorPartUI extends PanelUI
{
    
    
    @Override
    public void installUI(JComponent c)
    {
        super.installUI(c);
        c.setBackground(Color.WHITE);
    }

    
    
    
    @Override
    public void paint(Graphics g, JComponent c)
    {
        IEditorPart editor = (IEditorPart) c;
        IEditorPartSelectionHandler selectionHandler = editor.getSelectionHandler();
        IGraph graph = editor.getGraph();
        double zoom = editor.getZoomFactor();
        IGrid grid = editor.getGrid();
        super.paint(g, c);
        Graphics2D g2 = (Graphics2D) g;
        grid.paint(g2);
        g2.scale(zoom, zoom);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graph.draw(g2);
        for (IEditorPartBehavior paintableBehaviour : editor.getBehaviorManager().getBehaviors()) {
            paintableBehaviour.onPaint(g2);
        }
    }

    

    
}
