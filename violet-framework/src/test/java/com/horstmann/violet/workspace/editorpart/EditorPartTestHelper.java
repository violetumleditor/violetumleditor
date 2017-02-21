package com.horstmann.violet.workspace.editorpart;

import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.workspace.editorpart.EditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPart;

import javax.swing.*;
import java.awt.*;

import static org.mockito.Mockito.when;

/**
 * Contains common editor part test operations, helps with writing shorter tests.
 */
public class EditorPartTestHelper
{

    /**
     * Resets zoom to default value - 1.
     * @param editorPart zoomed editor part
     */
    public static void resetZoom(IEditorPart editorPart)
    {
        double zoom = editorPart.getZoomFactor();
        while (Math.abs(zoom - 1) >= 0.00001)
        {
            if (zoom > 1)
            {
                editorPart.zoomOut();
            }
            else
            {
                editorPart.zoomIn();
            }
            zoom = editorPart.getZoomFactor();
        }
    }

    /**
     * Prepares editor part to test getPreferredSize() method.
     * @param editorPart mocked editor part
     * @param viewPortBounds mocked return value of viewPort
     * @param clipBounds mocked return value of clipBounds
     */
    public static void prepareEditorPart(EditorPart editorPart, Rectangle viewPortBounds, Rectangle clipBounds){
        JPanel parent = new JPanel();
        parent.setBounds(viewPortBounds);
        when(editorPart.getParent()).thenReturn(parent);

        IGraph graph = editorPart.getGraph();
        when(graph.getClipBounds()).thenReturn(clipBounds);
    }

}
