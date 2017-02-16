package com.horstmann.violet.product.diagram.abstracts.node;

public class NodeInUseException extends Exception
{
    public NodeInUseException()
    {
        super("Node in use");
    }
}
