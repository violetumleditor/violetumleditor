package com.horstmann.violet.workspace.editorpart;

import com.horstmann.violet.framework.injection.resources.ResourceBundleConstant;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import java.awt.Dimension;
import java.awt.Rectangle;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class EditorPartTest
{

    private static EditorPart editorPartSpy;
    private static double ZOOM_FACTOR;
    private static double MIN_ZOOM;
    private static double MAX_ZOOM;

    @BeforeClass
    public static void beforeClass()
    {
        ZOOM_FACTOR = Double.parseDouble(ResourceBundleConstant.EDITOR_RESOURCE.getString("editorPart.zoom.factor"));
        MIN_ZOOM = Double.parseDouble(ResourceBundleConstant.EDITOR_RESOURCE.getString("editorPart.zoom.min"));
        MAX_ZOOM = Double.parseDouble(ResourceBundleConstant.EDITOR_RESOURCE.getString("editorPart.zoom.max"));

        IGraph graph = mock(IGraph.class);
        EditorPart editorPart = new EditorPart(graph);
        editorPartSpy = spy(editorPart);
    }

    @Before
    public void before()
    {
        EditorPartTestHelper.resetZoom(editorPartSpy);
    }

    @Test
    public void shouldReturnZoomedViewPortBounds()
    {
        //given
        Rectangle viewPortBounds = new Rectangle(200, 200);
        Rectangle clipBounds = new Rectangle(100, 100);
        EditorPartTestHelper.prepareEditorPart(editorPartSpy, viewPortBounds, clipBounds);

        //when
        editorPartSpy.zoomIn();
        Dimension actualDimension = editorPartSpy.getPreferredSize();

        //then
        double zoom = editorPartSpy.getZoomFactor();
        int expectedWidth = (int) (viewPortBounds.getWidth() * zoom);
        int expectedHeight = (int) (viewPortBounds.getHeight() * zoom);
        Dimension expectedDimension = new Dimension(expectedWidth, expectedHeight);

        assertTrue(expectedDimension.equals(actualDimension));
    }

    @Test
    public void shouldReturnZoomedClipBounds()
    {
        //given
        Rectangle viewPortBounds = new Rectangle(200, 200);
        Rectangle clipBounds = new Rectangle(300, 300);
        EditorPartTestHelper.prepareEditorPart(editorPartSpy, viewPortBounds, clipBounds);

        //when
        editorPartSpy.zoomIn();
        Dimension actualDimension = editorPartSpy.getPreferredSize();

        //then
        double zoom = editorPartSpy.getZoomFactor();
        int expectedWidth = (int) (clipBounds.getWidth() * zoom);
        int expectedHeight = (int) (clipBounds.getHeight() * zoom);
        Dimension expectedDimension = new Dimension(expectedWidth, expectedHeight);

        assertTrue(expectedDimension.equals(actualDimension));
    }

    @Test
    public void shouldZoomIn()
    {
        //given

        //when
        double zoomBefore = editorPartSpy.getZoomFactor();
        editorPartSpy.zoomIn();
        double zoomAfter = editorPartSpy.getZoomFactor();

        //then
        assertTrue(zoomAfter > zoomBefore);
    }

    @Test
    public void shouldZoomOut()
    {
        //given

        //when
        double zoomBefore = editorPartSpy.getZoomFactor();
        editorPartSpy.zoomOut();
        double zoomAfter = editorPartSpy.getZoomFactor();

        //then
        assertTrue(zoomAfter < zoomBefore);
    }

    @Test
    public void shouldZoomInByFactorFromProperties()
    {
        //given

        //when
        double zoomBefore = editorPartSpy.getZoomFactor();
        editorPartSpy.zoomIn();
        double zoomAfter = editorPartSpy.getZoomFactor();

        //then
        double calculatedFactor = zoomAfter / zoomBefore;
        assertTrue(Math.abs(ZOOM_FACTOR - calculatedFactor) < 0.00001);
    }

    @Test
    public void shouldZoomOutByFactorFromProperties()
    {
        //given

        //when
        double zoomBefore = editorPartSpy.getZoomFactor();
        editorPartSpy.zoomOut();
        double zoomAfter = editorPartSpy.getZoomFactor();

        //then
        double calculatedFactor = zoomBefore / zoomAfter;
        assertTrue(Math.abs(ZOOM_FACTOR - calculatedFactor) < 0.001);
    }

    @Test
    public void shouldStopZoomingAfterAchievingMaxZoomValue()
    {
        //given

        //when
        double zoom;
        do
        {
            zoom = editorPartSpy.getZoomFactor();
            editorPartSpy.zoomIn();
        } while (MAX_ZOOM / zoom > ZOOM_FACTOR);

        //then
        double differenceToMaxZoom = MAX_ZOOM / zoom;
        assertTrue(differenceToMaxZoom <= ZOOM_FACTOR);

        editorPartSpy.zoomIn();
        editorPartSpy.zoomIn();
        editorPartSpy.zoomIn();
        zoom = editorPartSpy.getZoomFactor();

        differenceToMaxZoom = MAX_ZOOM / zoom;
        assertTrue(differenceToMaxZoom <= ZOOM_FACTOR);
    }

    @Test
    public void shouldStopZoomingAfterAchievingMinZoomValue()
    {
        //given

        //when
        double zoom;
        do
        {
            zoom = editorPartSpy.getZoomFactor();
            editorPartSpy.zoomOut();
        } while (zoom / MIN_ZOOM > ZOOM_FACTOR);

        //then
        double differenceToMaxZoom = zoom / MIN_ZOOM;
        assertTrue(differenceToMaxZoom <= ZOOM_FACTOR);

        editorPartSpy.zoomOut();
        editorPartSpy.zoomOut();
        editorPartSpy.zoomOut();
        zoom = editorPartSpy.getZoomFactor();

        differenceToMaxZoom = zoom / MIN_ZOOM;
        assertTrue(differenceToMaxZoom <= ZOOM_FACTOR);
    }

}
