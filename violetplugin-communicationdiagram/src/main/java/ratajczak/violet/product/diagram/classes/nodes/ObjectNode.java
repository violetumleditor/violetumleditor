package ratajczak.violet.product.diagram.classes.nodes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.RectangularNode;
import com.horstmann.violet.product.diagram.abstracts.property.MultiLineString;
import com.horstmann.violet.product.diagram.common.PointNode;

public class ObjectNode extends RectangularNode{

	public ObjectNode(){
		name = new MultiLineString();
		name.setSize(MultiLineString.LARGE);
		name.setJustification(MultiLineString.CENTER);
		name.setUnderlined(true);
	}
	
	 private Rectangle2D getTopRectangleBounds() {
	        Rectangle2D globalBounds = new Rectangle2D.Double(0, 0, 0, 0);
	        Rectangle2D nameBounds = name.getBounds();
	    
	        globalBounds.add(nameBounds);
	      
	        double defaultHeight = DEFAULT_HEIGHT;
	        
	        globalBounds.add(new Rectangle2D.Double(0, 0, DEFAULT_WIDTH, defaultHeight));
	        Point2D currentLocation = getLocation();
	        double x = currentLocation.getX();
	        double y = currentLocation.getY();
	        double w = globalBounds.getWidth();
	        double h = globalBounds.getHeight();
	        globalBounds.setFrame(x, y, w, h);
	        Rectangle2D snappedBounds = getGraph().getGridSticker().snap(globalBounds);
	        return snappedBounds;
	    }
	    
	    private Rectangle2D getMiddleRectangleBounds() {
	        Rectangle2D globalBounds = new Rectangle2D.Double(0, 0, 0, 0);	        
	      
	        Rectangle2D topBounds = getTopRectangleBounds();
	        double x = topBounds.getX();
	        double y = topBounds.getMaxY();
	        double w = globalBounds.getWidth();
	        double h = globalBounds.getHeight();
	        globalBounds.setFrame(x, y, w, h);
	        Rectangle2D snappedBounds = getGraph().getGridSticker().snap(globalBounds);
	        return snappedBounds;
	    }
	    
	    private Rectangle2D getBottomRectangleBounds() {
	        Rectangle2D globalBounds = new Rectangle2D.Double(0, 0, 0, 0);
	       
	        Rectangle2D middleBounds = getMiddleRectangleBounds();
	        double x = middleBounds.getX();
	        double y = middleBounds.getMaxY();
	        double w = globalBounds.getWidth();
	        double h = globalBounds.getHeight();
	        globalBounds.setFrame(x, y, w, h);
	        Rectangle2D snappedBounds = getGraph().getGridSticker().snap(globalBounds);
	        return snappedBounds;
	    }

	    @Override
	    public Rectangle2D getBounds()
	    {
	        Rectangle2D top = getTopRectangleBounds();
	        Rectangle2D mid = getMiddleRectangleBounds();
	        Rectangle2D bot = getBottomRectangleBounds();
	        top.add(mid);
	        top.add(bot);
	        Rectangle2D snappedBounds = getGraph().getGridSticker().snap(top);
	        return snappedBounds;
	    }

	    @Override
	    public void draw(Graphics2D g2)
	    {
	        // Backup current color;
	        Color oldColor = g2.getColor();
	        // Translate g2 if node has parent
	        Point2D nodeLocationOnGraph = getLocationOnGraph();
	        Point2D nodeLocation = getLocation();
	        Point2D g2Location = new Point2D.Double(nodeLocationOnGraph.getX() - nodeLocation.getX(), nodeLocationOnGraph.getY() - nodeLocation.getY());
	        g2.translate(g2Location.getX(), g2Location.getY());
	        // Perform drawing
	        super.draw(g2);
	        Rectangle2D currentBounds = getBounds();
	        Rectangle2D topBounds = getTopRectangleBounds();
	        Rectangle2D midBounds = getMiddleRectangleBounds(); 
	        Rectangle2D bottomBounds = getBottomRectangleBounds();
	        if (topBounds.getWidth() < currentBounds.getWidth())
	        {
	        	// We need to re-center the topBounds - only do so if really required to avoid race conditions
	        	topBounds.setRect(topBounds.getX(), topBounds.getY(), currentBounds.getWidth(), topBounds.getHeight());
	        }
	        g2.setColor(getBackgroundColor());
	        g2.fill(currentBounds);
	        g2.setColor(getBorderColor());
	        g2.draw(currentBounds);
	        g2.drawLine((int) topBounds.getX(),(int) topBounds.getMaxY(),(int) currentBounds.getMaxX(),(int) topBounds.getMaxY());
	        g2.drawLine((int) bottomBounds.getX(),(int) bottomBounds.getY(),(int) currentBounds.getMaxX(),(int) bottomBounds.getY());
	        g2.setColor(getTextColor());
	        name.draw(g2, topBounds);
	        
	        // Restore g2 original location
	        g2.translate(-g2Location.getX(), -g2Location.getY());
	        // Restore first color
	        g2.setColor(oldColor);
	    }


	    /**
	     * Sets the name property value.
	     * 
	     * @param newValue the class name
	     */
	    public void setName(MultiLineString newValue)
	    {
	        name = newValue;
	    }

	    /**
	     * Gets the name property value.
	     * 
	     * @return the class name
	     */
	    public MultiLineString getName()
	    {
	        return name;
	    }
	    
	    
	   
	    /*
	     * (non-Javadoc)
	     * 
	     * @see com.horstmann.violet.product.diagram.abstracts.RectangularNode#clone()
	     */
	    public ObjectNode clone()
	    {
	        ObjectNode cloned = (ObjectNode) super.clone();
	        cloned.name = (MultiLineString) name.clone();
	       
	        return cloned;
	    }
	
	
	private MultiLineString name;
    private static int DEFAULT_WIDTH = 100;
    private static int DEFAULT_HEIGHT = 60;
}
