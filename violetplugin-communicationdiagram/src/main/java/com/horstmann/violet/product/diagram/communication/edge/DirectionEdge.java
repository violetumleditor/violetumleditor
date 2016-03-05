package com.horstmann.violet.product.diagram.communication.edge;

import com.horstmann.violet.product.diagram.common.edge.LabeledLineEdge;
import com.horstmann.violet.product.diagram.communication.CommunicationDiagramConstant;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;

/**
 * 
 * @author Artur Ratajczak
 *
 */
public class DirectionEdge extends LabeledLineEdge {

	public DirectionEdge() {
		SequenceNumber = new SingleLineText();
		Message = new SingleLineText();
		ConcurrentLoop = false;
		SequentialLoop = new SingleLineText();
	}

	@Override
	public DirectionEdge copy() {
		return new DirectionEdge(this);
	}

	@Override
	protected void beforeReconstruction()
	{
		super.beforeReconstruction();
		SequenceNumber.reconstruction();
		Message.reconstruction();
		SequentialLoop.reconstruction();
	}

	protected DirectionEdge(DirectionEdge directionEdge)
	{
		super();
		this.ConcurrentLoop = directionEdge.ConcurrentLoop;
		this.SequenceNumber = directionEdge.SequenceNumber.clone();
		this.Message = directionEdge.Message.clone();
		this.SequentialLoop = directionEdge.SequentialLoop.clone();
	}

	public void setSequenceNumber(SingleLineText number) {
		this.SequenceNumber = number;
		margeMessage();
	}

	public SingleLineText getSequenceNumber() {
		return SequenceNumber;
	}

	public void setMessage(SingleLineText message) {
		this.Message = message;
		margeMessage();
		;
	}

	public SingleLineText getMessage() {
		return Message;
	}

	public SingleLineText getSequentialLoop() {
		return SequentialLoop;
	}

	public void setSequentialLoop(SingleLineText sequentialLoop) {
		SequentialLoop = sequentialLoop;
		margeMessage();
	}

	public boolean isConcurrentLoop() {
		return ConcurrentLoop;
	}

	public void setConcurrentLoop(boolean concurrentLoop) {
		ConcurrentLoop = concurrentLoop;
		margeMessage();
	}

	private void margeMessage() {
		if (isConcurrentLoop() && !SequentialLoop.toString().isEmpty()) {
			setCenterLabel(SequenceNumber + " *|| " + SequentialLoop + " : " + Message);
		} else if (!SequentialLoop.toString().isEmpty())
			setCenterLabel(SequenceNumber + " * " + SequentialLoop + " : " + Message);
		else
			setCenterLabel(SequenceNumber + " : " + Message);
	}

	@Override
	public String getToolTip()
	{
		return CommunicationDiagramConstant.COMMUNICATION_DIAGRAM_RESOURCE.getString("direction_edge.tooltip");
	}

	private SingleLineText SequenceNumber;
	private SingleLineText Message;
	private SingleLineText SequentialLoop;
	private boolean ConcurrentLoop;

}