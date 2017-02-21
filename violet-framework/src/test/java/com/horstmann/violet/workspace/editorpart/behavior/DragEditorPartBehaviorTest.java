package com.horstmann.violet.workspace.editorpart.behavior;

import com.horstmann.violet.workspace.Workspace;
import com.horstmann.violet.workspace.WorkspacePanel;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DragEditorPartBehaviorTest
{
    private static IEditorPart editorPart;
    private static JScrollBar horizontalScrollBar;
    private static JScrollBar verticalScrollBar;
    private static IEditorPartBehaviorManager behaviorManager;

    @BeforeClass
    public static void beforeClass()
    {
        final Workspace workspace = DragEditorPartBehaviorTestHelper.initWorkspace();
        final WorkspacePanel workspacePanel = workspace.getAWTComponent();
        final JScrollPane scrollPane = workspacePanel.getScrollableEditorPart();
        final JViewport viewport = new JViewport();
        viewport.setBounds(0, 0, 200, 200);
        scrollPane.setViewport(viewport);
        horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        verticalScrollBar = scrollPane.getVerticalScrollBar();
        editorPart = workspace.getEditorPart();
        behaviorManager = editorPart.getBehaviorManager();
    }

    @Before
    public void before()
    {
        DragEditorPartBehaviorTestHelper.resetScrollBarState(horizontalScrollBar);
        DragEditorPartBehaviorTestHelper.resetScrollBarState(verticalScrollBar);
    }

    @Test
    public void shouldRegisterAsWorkspaceBehavior()
    {
        //given

        //when
        final java.util.List<DragEditorPartBehavior> behaviors = editorPart.getBehaviorManager()
                .getBehaviors(DragEditorPartBehavior.class);

        //then
        assertFalse(behaviors.isEmpty());

    }

    @Test
    public void shouldScrollBarsChangePositionOnMiddleMouseDrag() throws IOException
    {
        //given
        final int initialHorizontalBarValue = horizontalScrollBar.getValue();
        final int initialVerticalBarValue = verticalScrollBar.getValue();

        int mouseX = 200;
        int mouseY = 200;

        final int transition = 20;

        //when
        final MouseEvent mouseEvent = DragEditorPartBehaviorTestHelper.createMiddleMouseEvent(100, 100);

        when(mouseEvent.getLocationOnScreen()).thenReturn(new Point(mouseX, mouseY));
        behaviorManager.fireOnMousePressed(mouseEvent);

        when(mouseEvent.getLocationOnScreen()).thenReturn(new Point(mouseX-=transition, mouseY-=transition));
        behaviorManager.fireOnMouseDragged(mouseEvent);

        //then
        assertNotEquals(initialHorizontalBarValue, horizontalScrollBar.getValue());
        assertNotEquals(initialVerticalBarValue, verticalScrollBar.getValue());

    }

    @Test
    public void shouldMouseCursorChangeOnMiddleMousePress()
    {
        //given
        final int expectedCursorType = Cursor.MOVE_CURSOR;

        //when
        final JComponent component = editorPart.getSwingComponent();

        final MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getModifiers()).thenReturn(MouseEvent.BUTTON2);
        when(mouseEvent.getModifiersEx()).thenReturn(InputEvent.BUTTON2_DOWN_MASK);

        behaviorManager.fireOnMousePressed(mouseEvent);

        //then
        final int actualCursorType = component.getCursor().getType();
        assertTrue(expectedCursorType == actualCursorType);
    }

    @Test
    public void shouldMouseCursorReturnToDefaultOnMiddleMouseRelease()
    {
        //given
        final int expectedCursorType = Cursor.DEFAULT_CURSOR;

        //when
        final JComponent component = editorPart.getSwingComponent();

        final MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getModifiers()).thenReturn(MouseEvent.BUTTON2);
        when(mouseEvent.getModifiersEx()).thenReturn(InputEvent.BUTTON2_DOWN_MASK);

        behaviorManager.fireOnMousePressed(mouseEvent);
        behaviorManager.fireOnMouseReleased(mouseEvent);

        //then
        final int actualCursorType = component.getCursor().getType();
        assertTrue(expectedCursorType == actualCursorType);
    }
}
