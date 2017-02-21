package com.horstmann.violet.framework.plugin;

import com.horstmann.violet.framework.util.ResourceManager;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import java.util.ResourceBundle;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 06.03.2016
 */
public abstract class AbstractDiagramPlugin implements IDiagramPlugin
{
    protected AbstractDiagramPlugin(Class<? extends IGraph> graphClass, ResourceBundle resourceBundle)
    {
        this(graphClass);
        addResourceBundle(resourceBundle);
    }

    protected AbstractDiagramPlugin(Class<? extends IGraph> graphClass, String resourceBundle)
    {
        this(graphClass);
        addResourceBundle(resourceBundle);
    }

    protected AbstractDiagramPlugin(Class<? extends IGraph> graphClass)
    {
        this.graphClass = graphClass;
        resourceManager = new ResourceManager();
    }

    protected final void addResourceBundle(ResourceBundle resourceBundle)
    {
        this.resourceManager.addResource(resourceBundle);
    }

    protected final void addResourceBundle(String baseName)
    {
        this.resourceManager.addResource(baseName);
    }

    /**
     * @return plugin's provider or authors
     */
    @Override
    public String getProvider()
    {
        return "Alexandre de Pellegrin / Cays S. Horstmann / Adrian Bobrowski";
    }

    /**
     * @return very short plugin description (ex : Class diagram XMI extension)
     */
    @Override
    public final String getShortDescription()
    {
        return resourceManager.getString(DESCRIPTION_SHORT);
    }

    /**
     * @return full description
     */
    @Override
    public String getFullDescription()
    {
        return resourceManager.getString(DESCRIPTION_FULL);
    }

    /**
     * @return diagram type name (ex : Class Diagram)
     */
    @Override
    public final String getName()
    {
        return resourceManager.getString(DIAGRAM_NAME);
    }

    /**
     * @return the category of this diagram (ex : Static diagram)
     */
    @Override
    public final String getCategory()
    {
        return resourceManager.getString(DIAGRAM_CATEGORY);
    }

    /**
     * @return file extension associated to this graph (ex : .class.violet)
     */
    @Override
    public final String getFileExtension()
    {
        return "." + resourceManager.getString(FILES_EXTENSION);
    }

    /**
     * @return file extension textual name (ex : Class Diagram Files)
     */
    @Override
    public final String getFileExtensionName()
    {
        return String.format("%s (%s)", getName().replaceFirst("[0-9]*\\.", ""), getFileExtension());
    }

    /**
     * @return path of a diagram file used to introduce this kind of diagram
     */
    @Override
    public final String getSampleFilePath()
    {
        return resourceManager.getString(SAMPLE);
    }

    /**
     * @return corresponding graph class
     */
    @Override
    public final Class<? extends IGraph> getGraphClass()
    {
        return graphClass;
    }

    private ResourceManager resourceManager;
    private final Class<? extends IGraph> graphClass;

    private static final String DESCRIPTION_SHORT = "description.short";
    private static final String DESCRIPTION_FULL = "description.full";
    private static final String DIAGRAM_NAME = "diagram.name";
    private static final String DIAGRAM_CATEGORY = "diagram.category";
    private static final String FILES_EXTENSION = "files.extension";
    private static final String SAMPLE = "sample";
}
