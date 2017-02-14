/**
 * 
 */
package com.horstmann.violet.product.diagram.abstracts.node;

import com.horstmann.violet.product.diagram.property.text.LineText;

/**
 * Interface for detecting name at node
 * @author Aleksander Orchowski comodsuda@gmail.com
 * @date 28.11.2016
 */
public interface INamedNode extends INode {
	
	/**
	 * @author Aleksander Orchowski comodsuda@gmail.com 
	 * @return LineText object with node name
	 */
	LineText getName();
}
