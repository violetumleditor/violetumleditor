package com.horstmann.violet.framework.file.persistence;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.ExceptionListener;
import java.beans.Statement;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.horstmann.violet.framework.injection.bean.ManiocFramework.ManagedBean;
import com.horstmann.violet.framework.property.BentStyleChoiceList;
import com.horstmann.violet.framework.property.LineStyleChoiceList;
import com.horstmann.violet.product.diagram.abstracts.AbstractGraph;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.Id;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.framework.property.ArrowheadChoiceList;
import com.horstmann.violet.product.diagram.common.node.ImageNode;

/**
 * Standard Java implementation of IFilePersistenceService
 * @author alex
 *
 */
@ManagedBean(registeredManually=true)
public class StandardJavaFilePersistenceService implements IFilePersistenceService
{


    @Override
    public void write(IGraph graph, OutputStream out)
    {
        XMLEncoder encoder = getXMLEncoder(Violet016BackportFormatService.convertToViolet016(out));
        encoder.writeObject(graph);
        encoder.close();
    }


    @Override
    public IGraph read(InputStream in) throws IOException
    {
        XMLDecoder reader = new XMLDecoder(Violet016BackportFormatService.convertFromViolet016(in), null, new ExceptionListener()
        {
            public void exceptionThrown(Exception e)
            {
                e.printStackTrace();
            }
        });
        IGraph graph = (IGraph) reader.readObject();
        in.close();
        return graph;
    }
    

    /**
     * Creates a new instance of XML Encoder pre-configured for Violet beans serailization
     * 
     * @param out
     * @return configured encoder
     */
    private XMLEncoder getXMLEncoder(OutputStream out)
    {
        XMLEncoder encoder = new XMLEncoder(out);
    
        encoder.setExceptionListener(new ExceptionListener()
        {
            public void exceptionThrown(Exception ex)
            {
                ex.printStackTrace();
            }
        });
        configure(encoder);
        return encoder;
    }

    /**
     * Configures the given XML encoder by setting custom persistenceDelegate
     * @param encoder
     */
    private void configure(XMLEncoder encoder)
    {
        encoder.setPersistenceDelegate(Point2D.Double.class, new DefaultPersistenceDelegate()
        {
            protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out)
            {
                super.initialize(type, oldInstance, newInstance, out);
                Point2D p = (Point2D) oldInstance;
                out.writeStatement(new Statement(oldInstance, "setLocation", new Object[]
                {
                        new Double(p.getX()),
                        new Double(p.getY())
                }));
            }
        });
        encoder.setPersistenceDelegate(Line2D.class, new DefaultPersistenceDelegate()
        {
            protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out)
            {
                super.initialize(type, oldInstance, newInstance, out);
                Line2D l = (Line2D) oldInstance;
                out.writeStatement(new Statement(oldInstance, "setLine", new Object[]
                {
                        new Double(l.getX1()),
                        new Double(l.getY1()),
                        new Double(l.getX2()),
                        new Double(l.getY2())
                }));
            }
        });
        encoder.setPersistenceDelegate(Rectangle2D.class, new DefaultPersistenceDelegate()
        {
            protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out)
            {
                super.initialize(type, oldInstance, newInstance, out);
                Rectangle2D r = (Rectangle2D) oldInstance;
                out.writeStatement(new Statement(oldInstance, "setRect", new Object[]
                {
                        new Double(r.getX()),
                        new Double(r.getY()),
                        new Double(r.getWidth()),
                        new Double(r.getHeight())
                }));
            }
        });
    
        encoder.setPersistenceDelegate(BentStyleChoiceList.class, new CustomPersistenceDelegate());
        encoder.setPersistenceDelegate(LineStyleChoiceList.class, new CustomPersistenceDelegate());
        encoder.setPersistenceDelegate(ArrowheadChoiceList.class, new CustomPersistenceDelegate());
        encoder.setPersistenceDelegate(URL.class, new DefaultPersistenceDelegate(new String[]
        {
                "protocol",
                "host",
                "port",
                "file"
        }));
        encoder.setPersistenceDelegate(Map.class, new DefaultPersistenceDelegate()
        {
            protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out)
            {
                super.initialize(type, oldInstance, newInstance, out);
                Map<?, ?> map = (Map<?, ?>) oldInstance;
                for (Iterator<?> it = map.keySet().iterator(); it.hasNext();)
                {
                    Object key = it.next();
                    Object value = map.get(key);
                    out.writeStatement(new Statement(oldInstance, "put", new Object[]
                    {
                            key,
                            value
                    }));
                }
            }
        });
        encoder.setPersistenceDelegate(AbstractNode.class, new DefaultPersistenceDelegate()
        {
            protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out)
            {
                super.initialize(type, oldInstance, newInstance, out);
                INode n = (INode) oldInstance;
                List<INode> children = new ArrayList<INode>(n.getChildren());
                for (int i = 0; i < children.size(); i++)
                {
                    INode c = (INode) children.get(i);
                    Point2D p = c.getLocation();
                    out.writeStatement(new Statement(oldInstance, "addChild", new Object[]
                    {
                        c,
                        p
                    }));
                }
                boolean isWriteId = false; // Keep for further refinement
                if (isWriteId)
                {
                    Id id = n.getId();
                    out.writeStatement(new Statement(oldInstance, "setId", new Object[]
                    {
                        id
                    }));
                }
            }
        });
        encoder.setPersistenceDelegate(AbstractGraph.class, new DefaultPersistenceDelegate()
        {
            protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out)
            {
                super.initialize(type, oldInstance, newInstance, out);
                AbstractGraph g = (AbstractGraph) oldInstance;

                for ( INode n : g.getAllNodes())
                {
                    INode parent = n.getParent();
                    if (parent != null) continue;
                    Point2D p = n.getLocation();
                    out.writeStatement(new Statement(oldInstance, "addNode", new Object[]
                    {
                            n,
                            p
                    }));
                }
                for (IEdge e : g.getAllEdges())
                {
                    out.writeStatement(new Statement(oldInstance, "connect", new Object[]
                    {
                            e,
                            e.getStartNode(),
                            e.getStartLocation(),
                            e.getEndNode(),
                            e.getEndLocation()
                    }));
                }
            }
        });
//        encoder.setPersistenceDelegate(AbstractEdge.class, new DefaultPersistenceDelegate()
//        {
//            protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out)
//            {
//                super.initialize(type, oldInstance, newInstance, out);
//                IEdge e = (IEdge) oldInstance;
//                out.writeStatement(new Statement(oldInstance, "connect", new Object[]
//                {
//                        e.getStartNode(),
//                        e.getEndNode()
//                }));
//            }
//        });
        encoder.setPersistenceDelegate(ImageNode.class, new DefaultPersistenceDelegate()
        {
            protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out)
            {
                super.initialize(type, oldInstance, newInstance, out);
                ImageNode n = (ImageNode) oldInstance;
                try
                {
                    String imageContent = n.getImageContent();
                    int width = n.getImageWidth();
                    int height = n.getImageHeight();
                    out.writeStatement(new Statement(oldInstance, "setImageContent", new Object[]
                    {
                            imageContent,
                            width,
                            height
                    }));
                }
                catch (InterruptedException e)
                {
                    throw new RuntimeException("Error while serializing ImageNode", e);
                }
            }
        });
    }
    

}
