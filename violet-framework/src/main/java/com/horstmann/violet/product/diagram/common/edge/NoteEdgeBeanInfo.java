package com.horstmann.violet.product.diagram.common.edge;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 23.02.2016
 */
public class NoteEdgeBeanInfo extends LineEdgeBeanInfo
{
    public NoteEdgeBeanInfo()
    {
        super(LineEdge.class);

        displayLineStyle = false;
        displayBentStyle = false;
    }
}
