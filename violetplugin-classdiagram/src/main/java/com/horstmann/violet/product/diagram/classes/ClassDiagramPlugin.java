package com.horstmann.violet.product.diagram.classes;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.horstmann.violet.framework.plugin.IDiagramPlugin;
import com.horstmann.violet.framework.plugin.extensionpoint.Violet016FileFilterExtensionPoint;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.classes.node.ClassNode;
import com.horstmann.violet.product.diagram.classes.node.PackageNode;
import com.horstmann.violet.product.diagram.common.edge.BasePropertyEdge;
import com.horstmann.violet.product.diagram.classes.node.InterfaceNode;

/**
 * Describes class diagram graph type
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class ClassDiagramPlugin implements IDiagramPlugin, Violet016FileFilterExtensionPoint
{

    @Override
    public String getDescription()
    {
        return "Class UML diagram";
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
        return this.rs.getString("menu.class_diagram.name");
    }

    @Override
    public String getCategory()
    {
        return this.rs.getString("menu.class_diagram.category");
    }

    @Override
    public String getFileExtension()
    {
        return this.rs.getString("files.class.extension");
    }

    @Override
    public String getFileExtensionName()
    {
        return this.rs.getString("files.class.name");
    }

    
    @Override
    public String getSampleFilePath()
    {
        return this.rs.getString("sample.file.path");
    }
    
    
    @Override
    public Class<? extends IGraph> getGraphClass()
    {
        return ClassDiagramGraph.class;
    }
    
    
    public Map<String, String> getMappingToKeepViolet016Compatibility()
    {
        Map<String, String> replaceMap = new HashMap<String, String>();
        replaceMap.put("com.horstmann.violet.ClassDiagramGraph", ClassDiagramGraph.class.getName());
        replaceMap.put("com.horstmann.violet.ClassNode", ClassNode.class.getName());
        replaceMap.put("com.horstmann.violet.BasePropertyEdge", BasePropertyEdge.class.getName());
        replaceMap.put("com.horstmann.violet.InterfaceNode", InterfaceNode.class.getName());
        replaceMap.put("com.horstmann.violet.PackageNode", PackageNode.class.getName());
        return replaceMap;
    }

    private ResourceBundle rs = ResourceBundle.getBundle(ClassDiagramConstant.CLASS_DIAGRAM_STRINGS, Locale.getDefault());
}
