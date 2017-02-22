package com.horstmann.violet.workspace.editorpart.behavior;


import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

@PrepareForTest({IEditorPart.class, INode.class})
@RunWith(PowerMockRunner.class)
public class MoveSelectedWithArrowKeysBehaviorTest {

    @Test
    public void moveShouldInvokeOnce() throws Exception {

        MoveSelectedWithArrowKeysBehavior mockBehavior = mock(MoveSelectedWithArrowKeysBehavior.class);

        List<INode> selectedNodes = new ArrayList<>();
        INode node = mock(INode.class);
        selectedNodes.add(node);
        Whitebox.setInternalState(mockBehavior, "selectedNodes", selectedNodes);

        KeyEvent keyArrow = mock(KeyEvent.class);
        keyArrow.setKeyCode(KeyEvent.VK_DOWN);
        mockBehavior.handleKeyEvent(keyArrow);
        verifyPrivate(mockBehavior, Mockito.times(1))
                .invoke("moveOnProperKeyCode",keyArrow.getKeyCode() );
    }

    @Test
    public void moveShouldNotInvoke() throws Exception{

        MoveSelectedWithArrowKeysBehavior mockBehavior = mock(MoveSelectedWithArrowKeysBehavior.class);

        List<INode> selectedNodes = new ArrayList<>();
        INode node = mock(INode.class);
        selectedNodes.add(node);
        Whitebox.setInternalState(mockBehavior, "selectedNodes", selectedNodes);

        KeyEvent keySpace = mock(KeyEvent.class);
        keySpace.setKeyCode(KeyEvent.VK_SPACE);
        mockBehavior.handleKeyEvent(keySpace);
        verifyPrivate(mockBehavior, Mockito.times(0))
                .invoke("moveOnProperKeyCode",keySpace.getKeyCode() );
    }

}