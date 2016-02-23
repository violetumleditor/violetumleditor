package com.horstmann.violet.product.diagram.object;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.horstmann.violet.framework.plugin.IDiagramPlugin;
import com.horstmann.violet.framework.plugin.extensionpoint.Violet016FileFilterExtensionPoint;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.common.edge.BasePropertyEdge;
import com.horstmann.violet.product.diagram.object.edge.ObjectReferenceEdge;
import com.horstmann.violet.product.diagram.object.node.FieldNode;
import com.horstmann.violet.product.diagram.object.node.ObjectNode;

/**
 * Describes object diagram graph type
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class ObjectDiagramPlugin implements IDiagramPlugin, Violet016FileFilterExtensionPoint
{

    @Override
    public String getDescription()
    {
        return "Object UML diagram";
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
        return this.rs.getString("menu.object_diagram.name");
    }

    @Override
    public String getCategory()
    {
        return this.rs.getString("menu.object_diagram.category");
    }

    @Override
    public String getFileExtension()
    {
        return this.rs.getString("files.object.extension");
    }

    @Override
    public String getFileExtensionName()
    {
        return this.rs.getString("files.object.name");
    }

    @Override
    public String getSampleFilePath()
    {
        return this.rs.getString("sample.file.path");
    }

    @Override
    public Class<? extends IGraph> getGraphClass()
    {
        return ObjectDiagramGraph.class;
    }

    public Map<String, String> getMappingToKeepViolet016Compatibility()
    {
        Map<String, String> replaceMap = new HashMap<String, String>();
        replaceMap.put("com.horstmann.violet.ObjectDiagramGraph", ObjectDiagramGraph.class.getName());
        replaceMap.put("com.horstmann.violet.FieldNode", FieldNode.class.getName());
        replaceMap.put("com.horstmann.violet.ObjectNode", ObjectNode.class.getName());
        replaceMap.put("com.horstmann.violet.ObjectReferenceEdge", ObjectReferenceEdge.class.getName());
        replaceMap.put("com.horstmann.violet.BasePropertyEdge", BasePropertyEdge.class.getName());
        return replaceMap;
    }

    ResourceBundle rs = ResourceBundle.getBundle(ObjectDiagramConstant.OBJECT_DIAGRAM_STRINGS, Locale.getDefault());

}
