package com.horstmann.violet.product.diagram.abstracts.node;

/**
 * The Interface SwitchableNode.
 * 
 * Interface for switchable nodes, that can convert to different type.
 */
public interface ISwitchableNode {
	/**
	 * Converts INodes between different types.
	 * 
	 * @return INode
	 */
    public INode switchNode();
}
