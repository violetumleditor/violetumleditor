package com.horstmann.violet.product.diagram.sequence;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.horstmann.violet.framework.plugin.IDiagramPlugin;
import com.horstmann.violet.framework.plugin.extensionpoint.Violet016FileFilterExtensionPoint;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.sequence.edge.CallEdge;
import com.horstmann.violet.product.diagram.sequence.edge.ReturnEdge;
import com.horstmann.violet.product.diagram.sequence.node.ActivationBarNode;
import com.horstmann.violet.product.diagram.sequence.node.LifelineNode;

/**
 * Describes sequence diagram graph type
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class SequenceDiagramPlugin implements IDiagramPlugin, Violet016FileFilterExtensionPoint
{

    @Override
    public String getDescription()
    {
        return "Sequence UML diagram";
    }

    @Override
    public String getProvider()
    {
        return "Alexandre de Pellegrin / Cays S. Horstmann";
    }

    @Override
    public String getVersion()
    {
        return "1.0.0";
    }

    @Override
    public String getName()
    {
        return this.rs.getString("menu.sequence_diagram.name");
    }

    @Override
    public String getCategory()
    {
        return this.rs.getString("menu.sequence_diagram.category");
    }

    @Override
    public String getFileExtension()
    {
        return this.rs.getString("files.seq.extension");
    }

    @Override
    public String getFileExtensionName()
    {
        return this.rs.getString("files.seq.name");
    }

    @Override
    public String getSampleFilePath()
    {
        return this.rs.getString("sample.file.path");
    }

    @Override
    public Class<? extends IGraph> getGraphClass()
    {
        return SequenceDiagramGraph.class;
    }

    public Map<String, String> getMappingToKeepViolet016Compatibility()
    {
        Map<String, String> replaceMap = new HashMap<String, String>();
        replaceMap.put("com.horstmann.violet.CallEdge", CallEdge.class.getName());
        replaceMap.put("com.horstmann.violet.CallNode", ActivationBarNode.class.getName());
        replaceMap.put("com.horstmann.violet.ImplicitParameterNode", LifelineNode.class.getName());
        replaceMap.put("com.horstmann.violet.ReturnEdge", ReturnEdge.class.getName());
        replaceMap.put("com.horstmann.violet.SequenceDiagramGraph", SequenceDiagramGraph.class.getName());
        return replaceMap;
    }

    private ResourceBundle rs = ResourceBundle.getBundle(SequenceDiagramConstant.SEQUENCE_DIAGRAM_STRINGS, Locale.getDefault());

}
