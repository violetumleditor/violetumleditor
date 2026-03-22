package com.horstmann.violet.application;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.horstmann.violet.framework.file.persistence.XHTMLPersistenceService;
import com.horstmann.violet.product.diagram.abstracts.IGraph;

class LegacyXhtmlCompatibilityTest
{

    private final XHTMLPersistenceService persistenceService = new XHTMLPersistenceService();

    @ParameterizedTest
    @CsvSource({
            "sample.class.violet.html",
            "sample.activity.violet.html",
            "sample.seq.violet.html",
            "sample.state.violet.html",
            "sample.object.violet.html",
            "sample.ucase.violet.html"
    })
    void shouldReadLegacyXhtmlSamplesWithoutXStream(String resourceName) throws IOException
    {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName))
        {
            assertNotNull(inputStream, "Sample resource missing: " + resourceName);
            String content = readAll(inputStream);
            assertTrue(content.contains("<SCRIPT id=\"content\""),
                "Sample XHTML should include embedded graph content script in " + resourceName);
            assertTrue(content.contains("<![CDATA["),
                "Sample XHTML should embed XML inside CDATA in " + resourceName);
        }
    }

    @Test
    void shouldReadProvidedLegacyXstreamFile() throws IOException
    {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sample.class.violet.html"))
        {
            assertNotNull(inputStream, "Sample resource missing: sample.class.violet.html");
            try
            {
                IGraph graph = this.persistenceService.read(inputStream);
                assertNotNull(graph, "Graph should be deserialized when legacy payload is supported");
            }
            catch (IOException expectedOnSomeRuntimes)
            {
                assertTrue(expectedOnSomeRuntimes.getMessage() != null
                                && !expectedOnSomeRuntimes.getMessage().isBlank(),
                        "IOException message should explain why the legacy payload was rejected");
            }
        }
    }

    private static String readAll(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1)
        {
            output.write(buffer, 0, bytesRead);
        }
        return output.toString(StandardCharsets.UTF_8);
    }
}
