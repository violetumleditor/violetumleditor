package com.horstmann.violet.product.diagram.abstracts.node;

import java.awt.geom.Rectangle2D;

public interface IResizableNode
{
    void setWantedSize(Rectangle2D size);

    /**
     * Methods returns point which allow to change size of node
     *
     * @return point which allow to change size of node
     */
    Rectangle2D getResizablePoint();

}