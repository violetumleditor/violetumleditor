package com.horstmann.violet.workspace.editorpart.behavior;

import com.horstmann.violet.workspace.Workspace;
import com.horstmann.violet.workspace.WorkspacePanel;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import static com.horstmann.violet.framework.injection.resources.ResourceBundleConstant.EDITOR_RESOURCE;

/**
 * Drags editor part accordingly to the movement of dragged, middle button pressed mouse.
 * Behavior can be configured via EditorStrings.properties
 */
public class DragEditorPartBehavior extends AbstractEditorPartBehavior
{
    /**
     * Ratio of editor dragging.
     */
    private static final double RATIO;

    /**
     * Type of the cursor. {@link Cursor}
     */
    private static final int CURSOR_TYPE;

    /**
     * Remembered previous mouse position used as reference point.
     */
    private Point2D prevMousePosition;

    /**
     * Diagram workspace.
     */
    private Workspace workspace;

    public DragEditorPartBehavior(final Workspace workspace)
    {
        this.workspace = workspace;
    }

    static
    {
        RATIO = Double.parseDouble(EDITOR_RESOURCE.getString("behavior.dragEditorPart.ratio"));
        CURSOR_TYPE = Integer.parseInt(EDITOR_RESOURCE.getString("behavior.dragEditorPart.cursorType"));
    }

    /**
     * Initializes drag behavior.
     * {@inheritDoc}
     */
    @Override
    public void onMousePressed(final MouseEvent event)
    {
        if (SwingUtilities.isMiddleMouseButton(event))
        {
            prevMousePosition = event.getLocationOnScreen();
            setEditorPartCursor(CURSOR_TYPE);
        }
    }

    /**
     * Drags editor part according to movement of dragged, middle button pressed mouse.
     * {@inheritDoc}
     */
    @Override
    public void onMouseDragged(final MouseEvent event)
    {
        if (SwingUtilities.isMiddleMouseButton(event))
        {
            final Point2D actualMousePosition = event.getLocationOnScreen();
            final Point translationPoint = calculateMouseTranslationPoint(actualMousePosition);
            translateScrollBars(translationPoint);
            prevMousePosition = event.getLocationOnScreen();

        }
    }

    /**
     * Restore editor to state before dragging.
     * {@inheritDoc}
     */
    @Override
    public void onMouseReleased(final MouseEvent event)
    {
        if (SwingUtilities.isMiddleMouseButton(event))
        {
            setEditorPartCursor(Cursor.DEFAULT_CURSOR);
        }
    }

    /**
     * @param cursorType constant from Cursor class.
     * @see java.awt.Cursor
     */
    private void setEditorPartCursor(final int cursorType)
    {
        final Cursor cursor = new Cursor(cursorType);
        final JComponent editorComponent = workspace.getEditorPart().getSwingComponent();
        editorComponent.setCursor(cursor);
    }

    /**
     * Calculate point which x,y coordinates means mouse translation.
     *
     * @param actualMousePosition actual position of mouse.
     * @return translation point.
     */
    private Point calculateMouseTranslationPoint(final Point2D actualMousePosition)
    {
        final double zoom = workspace.getEditorPart().getZoomFactor();
        final int translationX = calculateMouseTranslationX(zoom, actualMousePosition);
        final int translationY = calculateMouseTranslationY(zoom, actualMousePosition);
        return new Point(translationX, translationY);
    }

    private int calculateMouseTranslationX(final double zoom, final Point2D newMousePosition)
    {
        final double mouseTranslationX = prevMousePosition.getX() - newMousePosition.getX();
        return (int) (RATIO * zoom * mouseTranslationX);
    }

    private int calculateMouseTranslationY(final double zoom, final Point2D newMousePosition)
    {
        final double mouseTranslationY = prevMousePosition.getY() - newMousePosition.getY();
        return (int) (RATIO * zoom * mouseTranslationY);
    }

    /**
     * Translate scroll bars accordingly to x,y coordinates of point.
     *
     * @param translationPoint scroll bars translation point.
     */
    private void translateScrollBars(final Point translationPoint)
    {
        final WorkspacePanel workspacePanel = workspace.getAWTComponent();
        final JScrollPane scrollPane = workspacePanel.getScrollableEditorPart();

        translateHorizontalScrollBar(scrollPane, translationPoint.x);
        translateVerticalScrollBar(scrollPane, translationPoint.y);
    }

    private void translateVerticalScrollBar(final JScrollPane scrollPane, final int translationY)
    {
        final JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getValue() + translationY);
    }

    private void translateHorizontalScrollBar(final JScrollPane scrollPane, final int translationX)
    {
        final JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        horizontalScrollBar.setValue(horizontalScrollBar.getValue() + translationX);
    }
}
