package com.horstmann.violet.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.horstmann.violet.framework.file.persistence.LegacyVioletXmlPersistenceService;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.EdgeTransitionPoint;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.classes.ClassDiagramGraph;
import com.horstmann.violet.product.diagram.classes.edges.AssociationEdge;
import com.horstmann.violet.product.diagram.classes.nodes.ClassNode;
import com.horstmann.violet.product.diagram.common.ImageNode;

class LegacyXmlWriteCompatibilityTest
{

    @Test
    void shouldWriteLegacyXmlThatTheLegacyReaderCanLoad() throws IOException
    {
        ClassDiagramGraph graph = new ClassDiagramGraph();

        ClassNode sourceNode = new ClassNode();
        ClassNode targetNode = new ClassNode();
        ImageNode imageNode = new ImageNode(createImage());

        graph.addNode(sourceNode, sourceNode.getLocationOnGraph());
        graph.addNode(targetNode, new java.awt.geom.Point2D.Double(220, 120));
        graph.addNode(imageNode, new java.awt.geom.Point2D.Double(40, 200));

        AssociationEdge edge = new AssociationEdge();
        graph.connect(edge, sourceNode, new java.awt.geom.Point2D.Double(40, 20), targetNode,
                new java.awt.geom.Point2D.Double(40, 20));
        edge.setTransitionPoints(new EdgeTransitionPoint[] {
                new EdgeTransitionPoint(120, 70)
        });

        LegacyVioletXmlPersistenceService persistenceService = new LegacyVioletXmlPersistenceService();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        persistenceService.write(graph, out);

        String xml = out.toString(StandardCharsets.UTF_8);
        assertFalse(xml.startsWith("<java"), "Legacy writer should not emit Java XMLEncoder format");
        assertTrue(xml.contains("<ClassDiagramGraph"), "Legacy writer should emit graph root element");
        assertTrue(xml.contains("reference=\""), "Legacy writer should reuse ids for shared node references");
        assertTrue(xml.contains("<transitionPoints"), "Legacy writer should emit transition point container");
        assertTrue(xml.contains("<Point2D.Double"), "Legacy writer should encode transition points as Point2D entries");
        assertTrue(xml.contains("<image class=\"Image\""), "Legacy writer should encode embedded images in legacy form");

        IGraph reloaded = persistenceService.read(new ByteArrayInputStream(out.toByteArray()));
        assertNotNull(reloaded, "Reloaded graph should not be null");
        assertEquals(3, reloaded.getAllNodes().size(), "Reloaded graph should contain all serialized nodes");
        assertEquals(1, reloaded.getAllEdges().size(), "Reloaded graph should contain serialized edge");

        IEdge reloadedEdge = reloaded.getAllEdges().iterator().next();
        assertEquals(1, reloadedEdge.getTransitionPoints().length,
                "Reloaded edge should preserve legacy transition points");
        assertEquals(120d, reloadedEdge.getTransitionPoints()[0].getX(), 0.001d,
                "Reloaded transition point X should match serialized value");
        assertEquals(70d, reloadedEdge.getTransitionPoints()[0].getY(), 0.001d,
                "Reloaded transition point Y should match serialized value");

        ImageNode reloadedImageNode = (ImageNode) reloaded.getAllNodes().stream()
                .filter(ImageNode.class::isInstance)
                .findFirst()
                .orElseThrow();
        assertEquals(0xFF3366CC, toBufferedImage(reloadedImageNode.getImage()).getRGB(1, 1),
                "Reloaded image should preserve pixel content");
    }

    private static BufferedImage createImage()
    {
        BufferedImage image = new BufferedImage(4, 4, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(1, 1, 0xFF3366CC);
        return image;
    }

    private static BufferedImage toBufferedImage(Image image)
    {
        if (image instanceof BufferedImage)
        {
            return (BufferedImage) image;
        }
        BufferedImage converted = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        converted.getGraphics().drawImage(image, 0, 0, null);
        return converted;
    }
}