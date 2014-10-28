package com.horstmann.violet.product.diagram.activity;

import java.util.Locale;
import java.util.ResourceBundle;

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
    public String getDescription() {
	return "Activity UML diagram";
    }

    @Override
    public String getProvider() {
	return "Alexandre de Pellegrin / Cays S. Horstmann";
    }

    @Override
    public String getVersion() {
	return "1.0.0";
    }

    @Override
    public String getName() {
	return this.rs.getString("menu.activity_diagram.name");
    }

    @Override
    public String getCategory() {
	return this.rs.getString("menu.activity_diagram.category");
    }

    @Override
    public String getFileExtension() {
	return this.rs.getString("files.activity.extension");
    }

    @Override
    public String getFileExtensionName() {
	return this.rs.getString("files.activity.name");
    }

    @Override
    public String getSampleFilePath() {
	return this.rs.getString("sample.file.path");
    }

    @Override
    public Class<? extends IGraph> getGraphClass() {
	return ActivityDiagramGraph.class;
    }

    private ResourceBundle rs = ResourceBundle.getBundle(ActivityDiagramConstant.ACTIVITY_DIAGRAM_STRINGS, Locale.getDefault());

}
