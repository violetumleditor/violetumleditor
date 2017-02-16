package com.horstmann.violet.framework.util.nodeusage;

import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NodeUsagesFinderTest
{
    private final NodeUsagesFinder nodeUsagesFinder = new NodeUsagesFinder();

    private INode firstNode;
    private INode secondNode;
    private INode thirdNode;
    private INode fourthNode;

    private final List<INode> selectedNodes = new ArrayList<>();
    private final List<INode> allNodes = new ArrayList<>();

    @Before
    public void setUp()
    {
        firstNode = mock(INode.class);
        when(firstNode.getName()).thenReturn(asLineText("First"));
        when(firstNode.getAttributes()).thenReturn(asLineText(""));
        when(firstNode.getMethods()).thenReturn(asLineText(""));

        secondNode = mock(INode.class);
        when(secondNode.getName()).thenReturn(asLineText("Second"));
        when(secondNode.getAttributes()).thenReturn(asLineText("first : First \n third : Third"));
        when(secondNode.getMethods()).thenReturn(asLineText(""));

        thirdNode = mock(INode.class);
        when(thirdNode.getName()).thenReturn(asLineText("Third"));
        when(thirdNode.getAttributes()).thenReturn(asLineText(""));
        when(thirdNode.getMethods()).thenReturn(asLineText("doSomething(first : First) : First"));

        fourthNode = mock(INode.class);
        when(fourthNode.getName()).thenReturn(asLineText(null));
        when(fourthNode.getAttributes()).thenReturn(asLineText(null));
        when(fourthNode.getMethods()).thenReturn(asLineText(null));

    }

    @Test
    public void shouldThrowExceptionWhenParametersAreNull()
    {
        // given
        final List<INode> selectedNodes = null;
        final List<INode> allNodes = null;

        // when
        final Throwable result = catchThrowable(() -> nodeUsagesFinder.findNodesUsages(selectedNodes, allNodes));

        // then
        assertThat(result).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldFindNoUsagesWhenThereAreNoSelectedNodes()
    {
        // given
        allNodes.add(firstNode);

        // when
        final List<NodeUsage> nodeUsages = nodeUsagesFinder.findNodesUsages(selectedNodes, allNodes);

        // then
        assertThat(nodeUsages).isEmpty();
    }

    @Test
    public void shouldFindUsagesIfNodeIsUsed()
    {
        // given
        allNodes.add(firstNode);
        allNodes.add(secondNode);
        allNodes.add(thirdNode);
        allNodes.add(fourthNode);

        selectedNodes.add(firstNode);

        // when
        final List<NodeUsage> nodeUsages = nodeUsagesFinder.findNodesUsages(selectedNodes, allNodes);

        // then
        assertThat(nodeUsages.size()).isEqualTo(1);
    }

    @Test
    public void shouldFindNoUsagesIfNodeIsNotUsed()
    {
        // given
        allNodes.add(firstNode);
        allNodes.add(secondNode);
        allNodes.add(thirdNode);
        allNodes.add(fourthNode);

        selectedNodes.add(fourthNode);

        // when
        final List<NodeUsage> nodeUsages = nodeUsagesFinder.findNodesUsages(selectedNodes, allNodes);

        // then
        assertThat(nodeUsages).isEmpty();
    }

    private LineText asLineText(final String text)
    {
        if (text == null)
        {
            return null;
        }

        final SingleLineText lineText = new SingleLineText();
        lineText.setText(text);
        return lineText;
    }

}