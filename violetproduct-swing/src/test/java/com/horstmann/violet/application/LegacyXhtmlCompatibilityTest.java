package com.horstmann.violet.application;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.horstmann.violet.framework.file.persistence.XHTMLPersistenceService;
import com.horstmann.violet.product.diagram.abstracts.IGraph;

class LegacyXhtmlCompatibilityTest
{

    private final XHTMLPersistenceService persistenceService = new XHTMLPersistenceService();

    @ParameterizedTest
    @CsvSource({
            "sample.class.violet.html,ClassDiagramGraph",
            "sample.activity.violet.html,ActivityDiagramGraph",
            "sample.seq.violet.html,SequenceDiagramGraph",
            "sample.state.violet.html,StateDiagramGraph",
            "sample.object.violet.html,ObjectDiagramGraph",
            "sample.ucase.violet.html,UseCaseDiagramGraph"
    })
    void shouldReadLegacyXhtmlSamplesWithoutXStream(String resourceName, String graphClassSimpleName) throws IOException
    {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName))
        {
            assertNotNull(inputStream, "Sample resource missing: " + resourceName);

            IGraph graph = this.persistenceService.read(inputStream);

            assertNotNull(graph, "Graph should be deserialized from " + resourceName);
            assertFalse(graph.getAllNodes().isEmpty(), "Graph should contain nodes in " + resourceName);
            assertNotNull(graph.getAllEdges(), "Graph edges collection should not be null in " + resourceName);
            org.junit.jupiter.api.Assertions.assertEquals(graphClassSimpleName, graph.getClass().getSimpleName(),
                    "Unexpected graph type for " + resourceName);
        }
    }
}
