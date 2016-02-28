package com.horstmann.violet.product.diagram.usecase;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.horstmann.violet.framework.plugin.IDiagramPlugin;
import com.horstmann.violet.framework.plugin.extensionpoint.Violet016FileFilterExtensionPoint;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.usecase.edge.UseCaseRelationshipEdge;
import com.horstmann.violet.product.diagram.usecase.node.ActorNode;
import com.horstmann.violet.product.diagram.usecase.node.UseCaseNode;

/**
 * Describes use case diagram graph type
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class UseCaseDiagramPlugin implements IDiagramPlugin, Violet016FileFilterExtensionPoint
{

    @Override
    public String getDescription()
    {
        return "Use case UML diagram";
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
        return this.rs.getString("menu.usecase_diagram.name");
    }

    @Override
    public String getCategory()
    {
        return this.rs.getString("menu.usecase_diagram.category");
    }

    @Override
    public String getFileExtension()
    {
        return this.rs.getString("files.usecase.extension");
    }

    @Override
    public String getFileExtensionName()
    {
        return this.rs.getString("files.usecase.name");
    }

    @Override
    public String getSampleFilePath()
    {
        return this.rs.getString("sample.file.path");
    }

    @Override
    public Class<? extends IGraph> getGraphClass()
    {
        return UseCaseDiagramGraph.class;
    }

    public Map<String, String> getMappingToKeepViolet016Compatibility()
    {
        Map<String, String> replaceMap = new HashMap<String, String>();
        replaceMap.put("com.horstmann.violet.ActorNode", ActorNode.class.getName());
        replaceMap.put("com.horstmann.violet.UseCaseDiagramGraph", UseCaseDiagramGraph.class.getName());
        replaceMap.put("com.horstmann.violet.UseCaseNode", UseCaseNode.class.getName());
        replaceMap.put("com.horstmann.violet.UseCaseRelationshipEdge", UseCaseRelationshipEdge.class.getName());
        return replaceMap;
    }

    private ResourceBundle rs = ResourceBundle.getBundle(UseCaseDiagramConstant.USE_CASE_DIAGRAM_STRINGS, Locale.getDefault());

}
