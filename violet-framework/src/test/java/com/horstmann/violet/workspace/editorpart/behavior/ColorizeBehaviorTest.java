package com.horstmann.violet.workspace.editorpart.behavior;

import com.horstmann.violet.product.diagram.abstracts.IColorable;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.common.node.NoteNode;
import org.junit.Test;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class ColorizeBehaviorTest {
    private MouseEvent event;
    @Test
    public void onMouseClicked() throws Exception {
        final int length = 5;

        Collection<IColorable> tmpcoll = new ArrayList<IColorable>();
        INode[] allNodes = new NoteNode[length];
        for (INode node: allNodes) {
            for (int i = 0; i < length; i++) {
                allNodes[i] = new NoteNode();
                tmpcoll.add((IColorable) allNodes[i]);
            }
        }
        for (IColorable it : tmpcoll){
            it.setBackgroundColor(Color.GREEN);
            assertNotNull(it);
        }
    }
}
