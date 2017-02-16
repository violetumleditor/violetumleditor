package com.horstmann.violet.workspace.editorpart;

import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.editorpart.enums.Direction;
import com.horstmann.violet.workspace.editorpart.helpers.Helpers;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Align {

    private ArrayList<Double> positions = new ArrayList<>();

    /**
     * The function align elements from selected Nodes
     */
    public void alignElements(List<INode> elementsLists,final Direction direction){

        if(direction == Direction.RIGHT || direction == Direction.LEFT) {
            positions.addAll(elementsLists.stream().map(element -> element.getLocation().getX()).collect(Collectors.toList()));

            for (final INode element : elementsLists) {
                final java.lang.Double tempYLocation = element.getLocation().getY();
                element.setLocation(new Point2D() {

                    @Override
                    public double getX() {
                        switch (direction) {
                            case LEFT:
                                return Helpers.findTheSmallestElement(positions);
                            case RIGHT:
                                return Helpers.findTheBiggestElement(positions);
                        }
                        return 0;
                    }

                    public double getY() {
                        return tempYLocation;
                    }

                    @Override
                    public void setLocation(double x, double y) {
                        x = getX();
                        y = getY();
                    }
                });
            }
            return;
        }

        if(direction == Direction.UP || direction == Direction.DOWN){
            positions.addAll(elementsLists.stream().map(element -> element.getLocation().getY()).collect(Collectors.toList()));

            for (final INode element:elementsLists) {
                final java.lang.Double tempXLocation = element.getLocation().getX();
                element.setLocation(new Point2D() {

                    @Override
                    public double getX() {
                        return tempXLocation;
                    }

                    @Override
                    public double getY() {
                        switch (direction){
                            case UP:
                                return Helpers.findTheSmallestElement(positions);
                            case DOWN:
                                return Helpers.findTheBiggestElement(positions);
                        }
                        return 0;
                    }

                    @Override
                    public void setLocation(double x, double y) {
                        x = getX();
                        y = getY();
                    }
                });
            }
        }
    }
}
