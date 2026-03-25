package com.horstmann.violet.framework.file.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.Test;

class JSONPersistenceServiceTest {

    @Test
    void writeRejectsNullGraph() {
        JSONPersistenceService service = new JSONPersistenceService(createConfiguredXmlService());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        assertThrows(IllegalArgumentException.class, () -> service.write(null, out));
    }

    @Test
    void readRejectsMalformedJson() {
        JSONPersistenceService service = new JSONPersistenceService(createConfiguredXmlService());

        assertThrows(IOException.class, () -> service.read(new ByteArrayInputStream("{\"xml\":\"\\u00zz\"}".getBytes())));
    }

    @Test
    void readRejectsMissingXmlField() {
        JSONPersistenceService service = new JSONPersistenceService(createConfiguredXmlService());

        assertThrows(IOException.class,
                () -> service.read(new ByteArrayInputStream("{\"format\":\"violet-json-v1\"}".getBytes())));
    }

    @Test
    void roundTripUsesJsonEnvelopeAndPreservesGraph() throws Exception {
        JSONPersistenceService service = new JSONPersistenceService(createConfiguredXmlService());

        XMLPersistenceServiceTest.TestGraph graph = new XMLPersistenceServiceTest.TestGraph();
        graph.value = "Graph with \"quotes\" and escaped slash \\ marker";
        graph.location = new Point2D.Double(15.0d, 35.0d);
        graph.preferredSize = new Rectangle2D.Double(0d, 0d, 640d, 480d);
        graph.backgroundColor = new Color(100, 110, 120, 130);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        service.write(graph, out);
        String json = out.toString();

        assertTrue(json.contains("\"format\": \"" + JSONPersistenceService.FORMAT_VERSION + "\""));
        assertTrue(json.contains("\"root\":"));
        assertTrue(json.contains("\"name\": \"testGraph\""));
        assertTrue(!json.contains("<testGraph"));

        XMLPersistenceServiceTest.TestGraph reloaded = (XMLPersistenceServiceTest.TestGraph) service
                .read(new ByteArrayInputStream(out.toByteArray()));

        assertNotNull(reloaded);
        assertEquals(graph.value, reloaded.value);
        assertEquals(graph.location.getX(), reloaded.location.getX(), 0.0001d);
        assertEquals(graph.location.getY(), reloaded.location.getY(), 0.0001d);
        assertEquals(graph.preferredSize.getWidth(), reloaded.preferredSize.getWidth(), 0.0001d);
        assertEquals(graph.preferredSize.getHeight(), reloaded.preferredSize.getHeight(), 0.0001d);
        assertEquals(graph.backgroundColor.getRed(), reloaded.backgroundColor.getRed());
        assertEquals(graph.backgroundColor.getGreen(), reloaded.backgroundColor.getGreen());
        assertEquals(graph.backgroundColor.getBlue(), reloaded.backgroundColor.getBlue());
        assertEquals(graph.backgroundColor.getAlpha(), reloaded.backgroundColor.getAlpha());
    }

    private static XMLPersistenceService createConfiguredXmlService() {
        try {
            XMLPersistenceService service = new XMLPersistenceService();
            Field tagTypesField = XMLPersistenceService.class.getDeclaredField("tagTypes");
            tagTypesField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, Class<?>> tagTypes = (Map<String, Class<?>>) tagTypesField.get(service);
            Class<?> type = XMLPersistenceServiceTest.TestGraph.class;
            String tagName = Character.toLowerCase(type.getSimpleName().charAt(0)) + type.getSimpleName().substring(1);
            tagTypes.put(tagName, type);
            return service;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to prepare XML persistence fixture", e);
        }
    }
}
