package com.horstmann.violet.product.diagram.communication.edges;

import com.horstmann.violet.product.diagram.abstracts.edge.SegmentedLineEdge;
import com.horstmann.violet.product.diagram.abstracts.property.ArrowHead;
import com.horstmann.violet.product.diagram.abstracts.property.LineStyle;
import com.horstmann.violet.product.diagram.abstracts.property.string.SingleLineText;
/**
 * 
 * @author Artur Ratajczak
 *
 */
public class DirectionEdge extends SegmentedLineEdge {

	public DirectionEdge() {
		SequenceNumber = new SingleLineText();
		Message = new SingleLineText();
		ConcurrentLoop = false;
		SequentialLoop = new SingleLineText();
	}

	@Override
	public ArrowHead getStartArrowHead() {
		return ArrowHead.NONE;
	}

	@Override
	public ArrowHead getEndArrowHead() {
		return ArrowHead.TRIANGLE;
	}

	@Override
	public LineStyle getLineStyle() {
		return LineStyle.SOLID;
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
			setMiddleLabel(SequenceNumber + " *|| " + SequentialLoop + " : " + Message);
		} else if (!SequentialLoop.toString().isEmpty())
			setMiddleLabel(SequenceNumber + " * " + SequentialLoop + " : " + Message);
		else
			setMiddleLabel(SequenceNumber + " : " + Message);
	}

	private SingleLineText SequenceNumber;
	private SingleLineText Message;
	private SingleLineText SequentialLoop;
	private boolean ConcurrentLoop;

}
