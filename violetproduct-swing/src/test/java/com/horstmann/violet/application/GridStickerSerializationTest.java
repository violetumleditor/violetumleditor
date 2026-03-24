package com.horstmann.violet.application;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.horstmann.violet.framework.file.persistence.IFilePersistenceService;
import com.horstmann.violet.framework.file.persistence.XMLPersistenceService;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.IGridSticker;
import com.horstmann.violet.product.diagram.classes.ClassDiagramGraph;

class GridStickerSerializationTest
{

    @Test
    void shouldSaveGraphWithAnonymousGridStickerWithoutEncoderInstantiationErrors() throws IOException
    {
        IGraph graph = new ClassDiagramGraph();
        graph.setGridSticker(new IGridSticker()
        {
            @Override
            public Rectangle2D snap(Rectangle2D r)
            {
                return r;
            }

            @Override
            public Point2D snap(Point2D p)
            {
                return p;
            }
        });

        IFilePersistenceService persistenceService = new XMLPersistenceService();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        persistenceService.write(graph, out);

        assertNotNull(out.toByteArray(), "Serialized bytes should not be null");
    }
}
