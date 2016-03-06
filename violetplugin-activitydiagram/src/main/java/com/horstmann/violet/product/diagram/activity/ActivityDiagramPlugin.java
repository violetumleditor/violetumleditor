package com.horstmann.violet.product.diagram.activity;

import com.horstmann.violet.framework.plugin.IDiagramPlugin;
import com.horstmann.violet.product.diagram.abstracts.IGraph;

/**
 * Describes activity diagram graph type
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class ActivityDiagramPlugin implements IDiagramPlugin {

    @Override
    public String getVersion()
    {
        return "1.1.0";
    }

    @Override
    public String getProvider()
    {
        return "Alexandre de Pellegrin / Cays S. Horstmann";
    }

    @Override
    public String getShortDescription()
    {
        return ActivityDiagramConstant.ACTIVITY_DIAGRAM_RESOURCE.getString("description");
    }

    /**
     * @return full description
     */
    @Override
    public String getFullDescription() {
        return null;
    }

    @Override
    public String getName()
    {
        return ActivityDiagramConstant.ACTIVITY_DIAGRAM_RESOURCE.getString("menu.activity_diagram.name");
    }

    @Override
    public String getCategory()
    {
	    return ActivityDiagramConstant.ACTIVITY_DIAGRAM_RESOURCE.getString("menu.activity_diagram.category");
    }

    @Override
    public String getFileExtension()
    {
	    return ActivityDiagramConstant.ACTIVITY_DIAGRAM_RESOURCE.getString("files.activity.extension");
    }

    @Override
    public String getFileExtensionName()
    {
        return ActivityDiagramConstant.ACTIVITY_DIAGRAM_RESOURCE.getString("files.activity.name");
    }

    @Override
    public String getSampleFilePath()
    {
        return ActivityDiagramConstant.ACTIVITY_DIAGRAM_RESOURCE.getString("sample.file.path");
    }

    @Override
    public Class<? extends IGraph> getGraphClass()
    {
        return ActivityDiagramGraph.class;
    }
}
