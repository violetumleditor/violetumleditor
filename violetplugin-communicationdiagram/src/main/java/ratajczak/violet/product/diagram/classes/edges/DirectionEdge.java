package ratajczak.violet.product.diagram.classes.edges;

import com.horstmann.violet.product.diagram.abstracts.edge.SegmentedLineEdge;
import com.horstmann.violet.product.diagram.abstracts.property.ArrowHead;
import com.horstmann.violet.product.diagram.abstracts.property.LineStyle;

public class DirectionEdge extends SegmentedLineEdge {

	public DirectionEdge() {
		SequenceNumber = "";
		Message = "";
		ConcurrentLoop = false;
		SequentialLoop = "";
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

	public void setSequenceNumber(String number) {
		this.SequenceNumber = number;
		margeMessage();
	}

	public String getSequenceNumber() {
		return SequenceNumber;
	}

	public void setMessage(String message) {
		this.Message = message;
		margeMessage();
		;
	}

	public String getMessage() {
		return Message;
	}

	public String getSequentialLoop() {
		return SequentialLoop;
	}

	public void setSequentialLoop(String sequentialLoop) {
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
		if (isConcurrentLoop() && !SequentialLoop.isEmpty()) {
			setMiddleLabel(SequenceNumber + " *|| " + SequentialLoop + " : " + Message);
		} else if (!SequentialLoop.isEmpty())
			setMiddleLabel(SequenceNumber + " * " + SequentialLoop + " : " + Message);
		else
			setMiddleLabel(SequenceNumber + " : " + Message);
	}

	private String SequenceNumber;
	private String Message;
	private String SequentialLoop;
	private boolean ConcurrentLoop;

}
