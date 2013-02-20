package com.horstmann.violet.framework.file.persistence;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.ManagedBean;
import com.horstmann.violet.framework.plugin.IDiagramPlugin;
import com.horstmann.violet.framework.plugin.PluginRegistry;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.thoughtworks.xstream.XStream;

@ManagedBean(registeredManually=true)
public class XStreamBasedPersistenceService implements IFilePersistenceService {
	
	@InjectedBean
	private PluginRegistry pluginRegistry;
	
	public XStreamBasedPersistenceService() {
		BeanInjector.getInjector().inject(this);
	}

	@Override
	public IGraph read(InputStream in) throws IOException {
		XStream xStream = new XStream();
		xStream = getConfiguredXStream(xStream);
		Object fromXML = xStream.fromXML(in);
		IGraph graph = (IGraph) fromXML;
		Collection<INode> allNodes = graph.getAllNodes();
		for (INode aNode : allNodes) {
			aNode.setGraph(graph);
		}
		return graph;
	}

	@Override
	public void write(IGraph graph, OutputStream out) {
		XStream xStream = new XStream();
		xStream = getConfiguredXStream(xStream);
		xStream.toXML(graph, out);
	}
	
	private XStream getConfiguredXStream(XStream xStream) {
		xStream.autodetectAnnotations(true);
		xStream.setMode(XStream.ID_REFERENCES);
		xStream.useAttributeFor(Point2D.Double.class, "x");
		xStream.useAttributeFor(Point2D.Double.class, "y");
		xStream.alias("Point2D.Double", Point2D.Double.class);
		List<IDiagramPlugin> diagramPlugins = this.pluginRegistry.getDiagramPlugins();
		for (IDiagramPlugin aPlugin : diagramPlugins) {
			Class<? extends IGraph> graphClass = aPlugin.getGraphClass();
			xStream.alias(graphClass.getSimpleName(), graphClass);
			try {
				IGraph aDummyGraph = graphClass.newInstance();
				List<IEdge> edgePrototypes = aDummyGraph.getEdgePrototypes();
				List<INode> nodePrototypes = aDummyGraph.getNodePrototypes();
				for (IEdge anEdgePrototype : edgePrototypes) {
					Class<? extends IEdge> edgeClass = anEdgePrototype.getClass();
					xStream.alias(edgeClass.getSimpleName(), anEdgePrototype.getClass());
				}
				for (INode aNodePrototype : nodePrototypes) {
					Class<? extends INode> nodeClass = aNodePrototype.getClass();
					xStream.alias(nodeClass.getSimpleName(), aNodePrototype.getClass());
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return xStream;
	}

}
