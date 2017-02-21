package com.horstmann.violet.workspace.editorpart.behavior;

import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by Bartosz Śledź on 22.01.2017.
 */
public class FindBehaviorTest {

    private final FindBehavior findBehavior = new FindBehavior();

    private INode exampleNode;
    private IEdge exampleEdge;

    private final List<INode> allNodes = new ArrayList<>();
    private final List<IEdge> allEdges = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        exampleNode = mock(INode.class);
        when(exampleNode.getName()).thenReturn(convertToLineText("Customer"));
        when(exampleNode.getAttributes()).thenReturn(convertToLineText("-customerId : int"));
        when(exampleNode.getMethods()).thenReturn(convertToLineText("+getCustomerId()"));

        exampleEdge = mock(IEdge.class);
        when(exampleEdge.getStartLabel()).thenReturn(convertToLineText("0..*"));
        when(exampleEdge.getCenterLabel()).thenReturn(convertToLineText("has"));
        when(exampleEdge.getEndLabel()).thenReturn(convertToLineText("1"));
    }


    @Test
    public void should_find_search_phrases_in_node() {
        // given
        allNodes.add(exampleNode);
        final String searchText = "cust";

        // when
        final List<LineText> foundText = findBehavior.findPhrases(allNodes, allEdges, searchText);

        // then
        assertThat(foundText.size()).isEqualTo(3);
    }

    @Test
    public void should_not_find_search_phrases_in_node() {
        // given
        allNodes.add(exampleNode);
        final String searchText = "test";

        // when
        final List<LineText> foundText = findBehavior.findPhrases(allNodes, allEdges, searchText);

        // then
        assertThat(foundText).isEmpty();
    }

    @Test
    public void should_find_search_phrases_in_edge() {
        // given
        allEdges.add(exampleEdge);
        final String searchText = "has";

        // when
        final List<LineText> foundText = findBehavior.findPhrases(allNodes, allEdges, searchText);

        // then
        assertThat(foundText.size()).isEqualTo(1);
    }

    @Test
    public void should_not_find_search_phrases_in_edge() {
        // given
        allEdges.add(exampleEdge);
        final String searchText = "test";

        // when
        final List<LineText> foundText = findBehavior.findPhrases(allNodes, allEdges, searchText);

        // then
        assertThat(foundText).isEmpty();
    }

    @Test
    public void should_not_find_search_phrases_when_search_phrases_is_empty() {
        // given
        allEdges.add(exampleEdge);
        final String searchText = "";

        // when
        final List<LineText> foundText = findBehavior.findPhrases(allNodes, allEdges, searchText);

        // then
        assertThat(foundText).isEmpty();
    }

    @Test
    public void should_not_find_search_phrases_when_node_or_edge_is_null() {
        // given
        final List<INode> allNodes = null;
        final List<IEdge> allEdges = null;
        final String searchText = "test";

        // when
        Throwable thrown = catchThrowable(() -> findBehavior.findPhrases(allNodes, allEdges, searchText));

        // then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nodes or edges cannot be null.");
    }

    private LineText convertToLineText(final String text) {

        final SingleLineText lineText = new SingleLineText();
        lineText.setText(text);
        return lineText;
    }
}