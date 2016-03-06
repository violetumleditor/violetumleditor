package com.horstmann.violet.product.diagram.state;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.horstmann.violet.framework.plugin.IDiagramPlugin;
import com.horstmann.violet.framework.plugin.extensionpoint.Violet016FileFilterExtensionPoint;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.state.edge.StateTransitionEdge;
import com.horstmann.violet.product.diagram.state.node.CircularInitialStateNode;
import com.horstmann.violet.product.diagram.state.node.StateNode;

/**
 * Describes state diagram graph type
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class StateDiagramPlugin implements IDiagramPlugin, Violet016FileFilterExtensionPoint
{

    @Override
    public String getShortDescription()
    {
        return "State UML diagram";
    }

    /**
     * @return full description
     */
    @Override
    public String getFullDescription() {
        return null;
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
        return this.rs.getString("menu.state_diagram.name");
    }

    @Override
    public String getCategory()
    {
        return this.rs.getString("menu.state_diagram.category");
    }

    @Override
    public String getFileExtension()
    {
        return this.rs.getString("files.state.extension");
    }

    @Override
    public String getFileExtensionName()
    {
        return this.rs.getString("files.state.name");
    }

    @Override
    public String getSampleFilePath()
    {
        return this.rs.getString("sample.file.path");
    }

    @Override
    public Class<? extends IGraph> getGraphClass()
    {
        return StateDiagramGraph.class;
    }

    public Map<String, String> getMappingToKeepViolet016Compatibility()
    {
        Map<String, String> replaceMap = new HashMap<String, String>();
        replaceMap.put("com.horstmann.violet.CircularStateNode", CircularInitialStateNode.class.getName());
        replaceMap.put("com.horstmann.violet.StateDiagramGraph", StateDiagramGraph.class.getName());
        replaceMap.put("com.horstmann.violet.StateNode", StateNode.class.getName());
        replaceMap.put("com.horstmann.violet.StateTransitionEdge", StateTransitionEdge.class.getName());
        return replaceMap;
    }

    private ResourceBundle rs = ResourceBundle.getBundle(StateDiagramConstant.STATE_DIAGRAM_STRINGS, Locale.getDefault());

}
