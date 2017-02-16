package com.horstmann.violet.product.diagram.communication.edge;

import com.horstmann.violet.product.diagram.abstracts.edge.bentstyle.BentStyle;
import com.horstmann.violet.product.diagram.common.edge.LabeledLineEdge;
import com.horstmann.violet.product.diagram.communication.CommunicationDiagramConstant;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;

/**
 * 
 * @author Artur Ratajczak
 *
 */
public class DirectionEdge extends LabeledLineEdge {

	public DirectionEdge() {
        super();
		setBentStyle(BentStyle.STRAIGHT);

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

	public void setSequenceNumber(LineText number) {
		this.SequenceNumber.setText(number);
		margeMessage();
	}

	public LineText getSequenceNumber() {
		return SequenceNumber;
	}

	public void setMessage(LineText message) {
		this.Message.setText(message);
		margeMessage();
	}

	public LineText getMessage() {
		return Message;
	}

	public LineText getSequentialLoop() {
		return SequentialLoop;
	}

	public void setSequentialLoop(LineText sequentialLoop) {
		SequentialLoop.setText(sequentialLoop);
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
		return CommunicationDiagramConstant.COMMUNICATION_DIAGRAM_RESOURCE.getString("tooltip.direction_edge");
	}

	private SingleLineText SequenceNumber;
	private SingleLineText Message;
	private SingleLineText SequentialLoop;
	private boolean ConcurrentLoop;

}