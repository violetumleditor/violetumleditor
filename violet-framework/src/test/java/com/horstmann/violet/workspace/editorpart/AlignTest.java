package com.horstmann.violet.workspace.editorpart;

import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.common.node.NoteNode;
import com.horstmann.violet.product.diagram.common.node.PointNode;
import com.horstmann.violet.workspace.editorpart.enums.Direction;
import com.horstmann.violet.workspace.editorpart.helpers.Helpers;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.Assert.*;


public class AlignTest {

    private ArrayList<INode> selectedNodes;
    private final static double DELTA =  1e-15;

    @Before
    public void createNewNodes(){

        final int length = 5;
        Random generator = new Random();
        INode []nodes = new NoteNode[length];

        for (int i=0; i<length;i++){
            nodes[i] = new NoteNode();
        }

        for (INode node: nodes){
            node.setLocation(new Point2D.Double(generator.nextDouble(),generator.nextDouble()) {
            });
        }

        selectedNodes = new ArrayList<>(Arrays.asList(nodes));
    }

    @Test
    public void alignElementsUpTest() {
        Align align = new Align();
        ArrayList<Double> positions = selectedNodes.stream().map(element -> element.getLocation().getY()).collect(Collectors.toCollection(ArrayList::new));
        align.alignElements(selectedNodes,Direction.UP);
        assertEquals(selectedNodes.get(0).getLocation().getY(),Helpers.findTheSmallestElement(positions),DELTA);
    }

    @Test
    public void alignElementsDownTest() {
        Align align = new Align();
        ArrayList<Double> positions = selectedNodes.stream().map(element -> element.getLocation().getY()).collect(Collectors.toCollection(ArrayList::new));
        align.alignElements(selectedNodes,Direction.DOWN);
        assertEquals(selectedNodes.get(0).getLocation().getY(),Helpers.findTheBiggestElement(positions),DELTA);
    }

    @Test
    public void alignElementsLeftTest() {
        Align align = new Align();
        ArrayList<Double> positions = selectedNodes.stream().map(element -> element.getLocation().getX()).collect(Collectors.toCollection(ArrayList::new));
        align.alignElements(selectedNodes,Direction.LEFT);
        assertEquals(selectedNodes.get(0).getLocation().getX(),Helpers.findTheSmallestElement(positions),DELTA);
    }

    @Test
    public void alignElementsRightTest() {
        Align align = new Align();
        ArrayList<Double> positions = selectedNodes.stream().map(element -> element.getLocation().getX()).collect(Collectors.toCollection(ArrayList::new));
        align.alignElements(selectedNodes,Direction.RIGHT);
        assertEquals(selectedNodes.get(0).getLocation().getX(),Helpers.findTheBiggestElement(positions),DELTA);
    }
}