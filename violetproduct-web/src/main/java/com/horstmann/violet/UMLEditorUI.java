package com.horstmann.violet;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.dialog.DialogFactoryMode;
import com.horstmann.violet.framework.file.GraphFile;
import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.LocalFile;
import com.horstmann.violet.framework.file.chooser.IFileChooserService;
import com.horstmann.violet.framework.file.chooser.JFileChooserService;
import com.horstmann.violet.framework.file.export.FileExportService;
import com.horstmann.violet.framework.file.persistence.IFilePersistenceService;
import com.horstmann.violet.framework.file.persistence.XHTMLPersistenceService;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanFactory;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.plugin.PluginLoader;
import com.horstmann.violet.framework.theme.ClassicMetalTheme;
import com.horstmann.violet.framework.theme.ITheme;
import com.horstmann.violet.framework.theme.ThemeManager;
import com.horstmann.violet.framework.userpreferences.DefaultUserPreferencesDao;
import com.horstmann.violet.framework.userpreferences.IUserPreferencesDao;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.Workspace;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;

public class UMLEditorUI extends UI
{

    @Override
    protected void init(VaadinRequest request)
    {
        if (!FACTORY_INITIALIZED)
        {
            initBeanFactory();
            FACTORY_INITIALIZED = true;
        }
        BeanInjector.getInjector().inject(this);
        try
        {
            createDefaultWorkspace();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initBeanFactory()
    {
        IUserPreferencesDao userPreferencesDao = new DefaultUserPreferencesDao();
        BeanFactory.getFactory().register(IUserPreferencesDao.class, userPreferencesDao);

        ThemeManager themeManager = new ThemeManager();
        ITheme theme1 = new ClassicMetalTheme();
        List<ITheme> themeList = new ArrayList<ITheme>();
        themeList.add(theme1);
        themeManager.setInstalledThemes(themeList);
        themeManager.switchToTheme(theme1);
        BeanFactory.getFactory().register(ThemeManager.class, themeManager);

        DialogFactory dialogFactory = new DialogFactory(DialogFactoryMode.INTERNAL);
        BeanFactory.getFactory().register(DialogFactory.class, dialogFactory);

        IFilePersistenceService filePersistenceService = new XHTMLPersistenceService();
        BeanFactory.getFactory().register(IFilePersistenceService.class, filePersistenceService);

        IFileChooserService fileChooserService = new JFileChooserService();
        BeanFactory.getFactory().register(IFileChooserService.class, fileChooserService);
    }

    /**
     * Creates workspace
     * 
     * @throws Exception
     */
    private void createDefaultWorkspace() throws Exception
    {
        installPlugins();
        loadDiagram();
    }

    private void loadDiagram() throws Exception
    {

        getUI().setContent(getMainLayout());
    }

    private IGraph getGraph()
    {
        if (this.currentGraph == null)
        {
            try
            {
                URL resource = getClass().getResource("test.class.violet.html");
                IFile aFile = new LocalFile(new File(resource.getFile()));
                GraphFile graphFile = new GraphFile(aFile);
                IWorkspace workspace = new Workspace(graphFile);
                final IEditorPart editorPart = workspace.getEditorPart();
                this.currentGraph = editorPart.getGraph();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        return this.currentGraph;
    }

    private AbsoluteLayout getMainLayout()
    {
        if (this.mainLayout == null)
        {
            this.mainLayout = new AbsoluteLayout();
            int h = getCurrentGraphImage().getHeight();
            int w = getCurrentGraphImage().getWidth();
            this.mainLayout.setHeight(h, Unit.PIXELS);
            this.mainLayout.setWidth(w, Unit.PIXELS);
            this.mainLayout.addComponent(getImage(getCurrentGraphImage(), "graph.png"));
        }
        return this.mainLayout;
    }

    /**
     * Install plugins
     */
    private void installPlugins()
    {

        this.pluginLoader.installPlugins();
    }

    private BufferedImage getCurrentGraphImage()
    {
        if (this.currentGraphImage == null)
        {
            refreshImage();
        }
        return this.currentGraphImage;
    }

    private Image getImage(BufferedImage bufferedImage, String filename)
    {
        StreamSource streamSource = new StreamSource()
        {
            @Override
            public InputStream getStream()
            {
                try
                {
                    ByteArrayOutputStream imagebuffer = new ByteArrayOutputStream();
                    ImageIO.write(getCurrentGraphImage(), "png", imagebuffer);
                    return new ByteArrayInputStream(imagebuffer.toByteArray());
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        };
        StreamResource resource = new StreamResource(streamSource, filename);
        Image anImage = new Image();
        anImage.setSource(resource);
        return anImage;
    }

    private void refreshImage()
    {
        this.currentGraphImage = FileExportService.getImage(getGraph());
    }

    @InjectedBean
    private PluginLoader pluginLoader;

    private BufferedImage currentGraphImage;

    private IGraph currentGraph;

    private AbsoluteLayout mainLayout;

    private static boolean FACTORY_INITIALIZED = false;

}
