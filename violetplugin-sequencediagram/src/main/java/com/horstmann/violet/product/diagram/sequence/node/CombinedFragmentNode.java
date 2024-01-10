package com.horstmann.violet.product.diagram.sequence.node;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import com.horstmann.violet.framework.graphics.content.ContentBackground;
import com.horstmann.violet.framework.graphics.content.ContentBorder;
import com.horstmann.violet.framework.graphics.content.ContentInsideShape;
import com.horstmann.violet.framework.graphics.content.EmptyContent;
import com.horstmann.violet.framework.graphics.content.HorizontalLayout;
import com.horstmann.violet.framework.graphics.content.RelativeLayout;
import com.horstmann.violet.framework.graphics.content.TextContent;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.property.choiceList.ChoiceList;
import com.horstmann.violet.product.diagram.property.choiceList.TextChoiceList;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.MultiLineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;
import com.horstmann.violet.product.diagram.property.text.decorator.LargeSizeDecorator;
import com.horstmann.violet.product.diagram.property.text.decorator.OneLineText;
import com.horstmann.violet.product.diagram.sequence.SequenceDiagramConstant;

/**
 * A Combined fragment node in a UML diagram.
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 21.02.2016
 */
public class CombinedFragmentNode extends AbstractNode // implements IResizableNode
{
    public CombinedFragmentNode()
    {
        operator = new TextChoiceList<String>(TYPE_KEYS, TYPE_VALUE);
        selectedType = operator.getSelectedValue();
        operatorText = new SingleLineText(nameConverter);
        operatorText.setText(operator.getSelectedValue());
        operatorText.setPadding(0,8,0,18);
        frameContent = new MultiLineText();
        createContentStructure();
    }

    protected CombinedFragmentNode(CombinedFragmentNode node) throws CloneNotSupportedException
    {
        super(node);
        operator = node.operator;
        selectedType = operator.getSelectedValue();
        operatorText = node.operatorText.clone();
        frameContent = node.frameContent.clone();
        createContentStructure();
    }

    @Override
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();

        if(null==frameContent)
        {
            frameContent = new MultiLineText();
        }
        if(null==operatorText)
        {
            operatorText = new SingleLineText();
        }

        frameContent.reconstruction();
        operatorText.reconstruction(nameConverter);
        operatorText.setPadding(0,8,0,18);

        operator = new TextChoiceList<String>(TYPE_KEYS, TYPE_VALUE);
        operator.setSelectedValueFromString(selectedType);
    }

    @Override
    protected void afterReconstruction()
    {
        setOperator(getOperator());

        wantedSizeContent.setMinWidth(wantedWeight);
        wantedSizeContent.setMinHeight(wantedHeight);
        super.afterReconstruction();
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new CombinedFragmentNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        wantedSizeContent = new EmptyContent();

        RelativeLayout relativeGroupContent = new RelativeLayout();
        relativeGroupContent.setMinHeight(DEFAULT_HEIGHT);
        relativeGroupContent.setMinWidth(DEFAULT_WIDTH);

        TextContent nameContent = new TextContent(operatorText);
        nameContent.setMinHeight(DEFAULT_TYPE_HEIGHT);
        nameContent.setMinWidth(DEFAULT_TYPE_WIDTH);

        ContentInsideShape nameInsideShape = new ContentInsideCustomShape(nameContent, new ContentInsideCustomShape.ShapeCreator()
        {
            @Override
            public Shape createShape(double contentWidth, double contentHeight)
            {
                GeneralPath path = new GeneralPath();
                path.moveTo(0, 0);
                path.lineTo(contentWidth, 0);
                path.lineTo(contentWidth, contentHeight / 2);
                path.lineTo(contentWidth - contentHeight / 2, contentHeight);
                path.lineTo(0, contentHeight);
                path.closePath();
                return path;
            }
        });

        ContentBackground nameBackground = new ContentBackground(new ContentBorder(nameInsideShape, BORDER_COLOR), BACKGROUND_COLOR);

        EmptyContent nameMarginRight = new EmptyContent();
        nameMarginRight.setMinWidth(NAME_MARGIN_RIGHT);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(nameBackground);
        horizontalLayout.add(nameMarginRight);

        relativeGroupContent.clear();
        relativeGroupContent.add(wantedSizeContent);
        relativeGroupContent.add(horizontalLayout);
        relativeGroupContent.add(new ContentInsideRectangle(new TextContent(frameContent)), new Point2D.Double(0,DEFAULT_TYPE_HEIGHT+5));

        ContentInsideShape contentInsideShape = new ContentInsideRectangle(relativeGroupContent);

        setContent(new ContentBorder(contentInsideShape, BORDER_COLOR));
    }

    @Override
    public String getToolTip()
    {
        return SequenceDiagramConstant.SEQUENCE_DIAGRAM_RESOURCE.getString("tooltip.combined_fragment_node");
    }

    @Override
    public boolean addConnection(IEdge edge)
    {
        return false;
    }

//    @Override
//    public void setWantedSize(Rectangle2D size)
//    {
//        wantedWeight = size.getWidth();
//        wantedHeight = size.getHeight();
//        wantedSizeContent.setMinWidth(wantedWeight);
//        wantedSizeContent.setMinHeight(wantedHeight);
//    }
//
//    @Override
//    public Rectangle2D getResizablePoint()
//    {
//        Rectangle2D nodeBounds = getBounds();
//
//        double x = nodeBounds.getMaxX() - RESIZABLE_POINT_SIZE;
//        double y = nodeBounds.getMaxY() - RESIZABLE_POINT_SIZE;
//
//        return new Rectangle2D.Double(x, y, RESIZABLE_POINT_SIZE, RESIZABLE_POINT_SIZE);
//    }

    /**
     * Sets the contents property value.
     *
     * @param newValue the contents of this class
     */
    public void setFrameContent(LineText newValue)
    {
        frameContent.setText(newValue.toEdit());
    }

    /**
     * Gets the contents property value.
     *
     * @return the contents of this class
     */
    public LineText getFrameContent()
    {
        return frameContent;
    }

    /**
     * Gets the type property value.
     *
     * @return the type of this frame
     */
    public ChoiceList getOperator()
    {
        return operator;
    }

    /**
     * Sets the type property value.
     *
     * @param type the type of this frame
     */
    public void setOperator(ChoiceList type)
    {
        this.operator = (TextChoiceList<String>) type;
        selectedType = this.operator.getSelectedValue();
        operatorText.setText(this.operator.getSelectedValue());
    }

    private SingleLineText operatorText;
    private MultiLineText frameContent;

    private String selectedType;
    private transient TextChoiceList<String> operator;

    private double wantedWeight;
    private double wantedHeight;
    private transient EmptyContent wantedSizeContent;

    private static final int DEFAULT_TYPE_WIDTH = 60;
    private static final int DEFAULT_TYPE_HEIGHT = 20;
    private static final int DEFAULT_WIDTH = 80;
    private static final int DEFAULT_HEIGHT = 50;
    private static final int NAME_MARGIN_RIGHT = 10;
    private static final int RESIZABLE_POINT_SIZE = 5;

    private static final Color BORDER_COLOR = new Color(191, 191, 191, 255);
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 153, 255);

    private static final LineText.Converter nameConverter = new LineText.Converter()
    {
        @Override
        public OneLineText toLineString(String text)
        {
            return new LargeSizeDecorator(new OneLineText(text));
        }
    };

    private static final String[] TYPE_VALUE = new String[]{
            "alt",
            "opt",
            "loop",
            "break",
            "par",
            "strict",
            "seq",
            "critical",
            "ignore",
            "consider",
            "assert",
            "neg"
    };

    public static String[] TYPE_KEYS = new String[TYPE_VALUE.length];

    static
    {
        for(int i=0; i< TYPE_KEYS.length; ++i)
        {
            try
            {
                TYPE_KEYS[i] = SequenceDiagramConstant.SEQUENCE_DIAGRAM_RESOURCE.getString(
                        ("operator." + TYPE_VALUE[i]).toLowerCase()
                );
            }catch (Exception ignored){}
        }
    }
}