package com.horstmann.violet.product.diagram.abstracts;

import java.util.List;

/**
 * Can be implemented by SequenceDiagramGraph and ClassModelGraph
 * The methods will be implemented by these two classes
 * And will then be called by Visualization.
 */

public interface StatisticalGraph {
	public void evaluateStatistics();
	public List<String> evaluateViolations();
}
