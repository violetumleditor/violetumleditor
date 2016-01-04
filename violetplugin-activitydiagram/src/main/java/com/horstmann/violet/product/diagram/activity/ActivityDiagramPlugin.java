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
    public String getVersion() {
        return "1.1.0";
    }

    @Override
    public String getProvider() {
	return "Alexandre de Pellegrin / Cays S. Horstmann";
    }

    @Override
    public String getDescription() {
        return ActivityResource.ACTIVITY.getString("description");
    }

    @Override
    public String getName() {
	return ActivityResource.ACTIVITY.getString("menu.activity_diagram.name");
    }

    @Override
    public String getCategory() {
	    return ActivityResource.ACTIVITY.getString("menu.activity_diagram.category");
    }

    @Override
    public String getFileExtension() {
	return ActivityResource.ACTIVITY.getString("files.activity.extension");
    }

    @Override
    public String getFileExtensionName() {
	return ActivityResource.ACTIVITY.getString("files.activity.name");
    }

    @Override
    public String getSampleFilePath() {
	return ActivityResource.ACTIVITY.getString("sample.file.path");
    }

    @Override
    public Class<? extends IGraph> getGraphClass() {
	return ActivityDiagramGraph.class;
    }
}
