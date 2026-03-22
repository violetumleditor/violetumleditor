package com.horstmann.violet.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Image;
import java.awt.geom.Rectangle2D;
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
import com.horstmann.violet.product.diagram.classes.nodes.PackageNode;
import com.horstmann.violet.product.diagram.common.ImageNode;

class LegacyXmlWriteCompatibilityTest
{

    @Test
    void shouldWriteLegacyXmlThatTheLegacyReaderCanLoad() throws IOException
    {
        ClassDiagramGraph graph = new ClassDiagramGraph();

        ClassNode sourceNode = new ClassNode();
        ClassNode targetNode = new ClassNode();
        PackageNode packageNode = new PackageNode();
        ClassNode nestedClassNode = new ClassNode();
        ImageNode imageNode = new ImageNode(createImage());
        Rectangle2D preferredSize = new Rectangle2D.Double(0d, 0d, 288.9729729729727d, 577.9459459459454d);
        sourceNode.setPreferredSize(preferredSize);

        graph.addNode(sourceNode, sourceNode.getLocationOnGraph());
        graph.addNode(targetNode, new java.awt.geom.Point2D.Double(220, 120));
        graph.addNode(packageNode, new java.awt.geom.Point2D.Double(340, 20));
        packageNode.addChild(nestedClassNode, new java.awt.geom.Point2D.Double(30, 40));
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
        assertTrue(xml.contains("<startNode id=\""), "Legacy writer should link edge start by node identity id");
        assertTrue(xml.contains("<endNode id=\""), "Legacy writer should link edge end by node identity id");
        assertTrue(xml.contains("<transitionPoints"), "Legacy writer should emit transition point container");
        assertTrue(xml.contains("<transitionPoint "), "Legacy writer should encode transition points as transitionPoint entries");
        assertFalse(xml.matches("(?s).*<children\\s+id=\"\\d+\"\\s*/>.*"),
                "Empty children collections should not be serialized");
        assertFalse(xml.contains("<parent reference="),
                "Nested nodes should not serialize redundant parent references");
        assertFalse(xml.contains("<backgroundColor reference="),
                "backgroundColor should be serialized inline, not by reference");
        assertFalse(xml.contains("<textColor reference="),
                "textColor should be serialized inline, not by reference");
        assertFalse(xml.contains("<borderColor reference="),
                "borderColor should be serialized inline, not by reference");
        assertTrue(xml.matches("(?s).*<backgroundColor red=\"\\d+\" green=\"\\d+\" blue=\"\\d+\" alpha=\"\\d+\"/>.*"),
                "backgroundColor should use attribute-based compact XML representation without id");
        assertTrue(xml.contains("<ressources>"), "Legacy writer should extract images into a root ressources element");
        assertTrue(xml.contains("<image id=\"img-"), "Legacy writer should store image binaries in ressources");
        assertTrue(xml.contains("<image id=\"img-"), "Legacy writer should reference extracted images from nodes");
        assertTrue(xml.matches("(?s).*<image id=\"img-[^\"]+\"\s*/>.*"),
                "Legacy writer should reference extracted images from nodes with self-closing id entries");
        assertTrue(xml.matches("(?s).*<ressources>.*<image id=\"img-[^\"]+\">.*</image>.*</ressources>.*"),
                "Legacy writer should store image payload in ressources using id entries");
        assertFalse(xml.contains(" class=\""), "Legacy writer should not emit class attributes");
        assertFalse(xml.matches("(?s).*<PackageNode id=\"[^\"]+\">.*"), "Legacy writer should not emit redundant node id attributes on objects");
        assertTrue(xml.contains("<location x=\""),
                "Legacy writer should emit compact location elements");
        assertFalse(xml.contains("<location class="),
                "Compact location should not declare a class attribute");
        assertFalse(xml.contains("<location id="),
                "Compact location should not declare an id attribute");
        assertTrue(xml.contains("<preferredSize width=\"288.9729729729727\""),
                "Legacy writer should emit compact preferredSize width");
        assertTrue(xml.contains("height=\"577.9459459459454\"/>"),
                "Legacy writer should emit compact preferredSize height");
        assertFalse(xml.contains("<preferredSize width=\"0.0\" height=\"0.0\"/>"),
                "Legacy writer should not emit default preferredSize values");
        assertFalse(xml.contains("<cropInsets"),
                "Legacy writer should not emit default cropInsets values");
        assertTrue(xml.matches("(?s).*<cropInsets top=\"[\\d.]+\" left=\"[\\d.]+\" bottom=\"[\\d.]+\" right=\"[\\d.]+\"/>.*") || !xml.contains("cropInsets"),
                "Legacy writer should use compact attribute format for cropInsets without id, or omit if default");
        assertFalse(xml.contains("<preferredSize class="),
                "Compact preferredSize should not declare a class attribute");
        assertFalse(xml.contains("<preferredSize id="),
                "Compact preferredSize should not declare an id attribute");
        assertFalse(xml.contains("<preferredSize x="),
                "Compact preferredSize should not serialize x attributes");
        assertFalse(xml.contains("<preferredSize y="),
                "Compact preferredSize should not serialize y attributes");
        assertTrue(xml.matches("(?s).*<startNode id=\"[^\"]+\"\s*/>.*"),
                "Edge start should be serialized as startNode with id attribute");
        assertTrue(xml.matches("(?s).*<endNode id=\"[^\"]+\"\s*/>.*"),
                "Edge end should be serialized as endNode with id attribute");
        assertFalse(xml.contains("<start reference="),
                "Legacy writer should not emit old start/reference edge format");
        assertFalse(xml.contains("<end reference="),
                "Legacy writer should not emit old end/reference edge format");
        assertTrue(xml.indexOf("<ressources>") > xml.indexOf("</edges>"),
                "Ressources should be emitted under the root diagram element after edges");

        IGraph reloaded = persistenceService.read(new ByteArrayInputStream(out.toByteArray()));
        assertNotNull(reloaded, "Reloaded graph should not be null");
        assertEquals(5, reloaded.getAllNodes().size(), "Reloaded graph should contain all serialized nodes");
        assertEquals(1, reloaded.getAllEdges().size(), "Reloaded graph should contain serialized edge");

        ClassNode reloadedNestedClass = (ClassNode) reloaded.getAllNodes().stream()
                .filter(ClassNode.class::isInstance)
                .map(ClassNode.class::cast)
                .filter(node -> node.getParent() != null)
                .findFirst()
                .orElseThrow();
        assertTrue(reloadedNestedClass.getParent() instanceof PackageNode,
                "Reloaded nested node should still point to its package parent");

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
        ClassNode reloadedSizedClassNode = (ClassNode) reloaded.getAllNodes().stream()
                .filter(ClassNode.class::isInstance)
                .map(ClassNode.class::cast)
                .filter(node -> node.getPreferredSize() != null && node.getPreferredSize().getWidth() > 200d)
                .findFirst()
                .orElseThrow();
        assertNotNull(reloadedSizedClassNode.getPreferredSize(), "Reloaded preferred size should be available");
        assertEquals(0d, reloadedSizedClassNode.getPreferredSize().getX(), 0.001d,
                "Reloaded preferred size x should default to zero");
        assertEquals(0d, reloadedSizedClassNode.getPreferredSize().getY(), 0.001d,
                "Reloaded preferred size y should default to zero");
        assertEquals(288.9729729729727d, reloadedSizedClassNode.getPreferredSize().getWidth(), 0.001d,
                "Reloaded preferred size width should match serialized value");
        assertEquals(577.9459459459454d, reloadedSizedClassNode.getPreferredSize().getHeight(), 0.001d,
                "Reloaded preferred size height should match serialized value");
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