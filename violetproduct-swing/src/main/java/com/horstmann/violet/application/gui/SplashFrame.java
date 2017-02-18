package com.horstmann.violet.application.gui;


import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.file.GraphFile;
import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.IGraphFile;
import com.horstmann.violet.framework.file.chooser.IFileChooserService;
import com.horstmann.violet.framework.file.naming.ExtensionFilter;
import com.horstmann.violet.framework.file.naming.FileNamingService;
import com.horstmann.violet.framework.file.persistence.IFileReader;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.framework.plugin.IDiagramPlugin;
import com.horstmann.violet.framework.plugin.PluginRegistry;
import com.horstmann.violet.framework.userpreferences.UserPreferencesService;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.Workspace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

/**
 * This desktop frame contains panes that show menu to create/open projects.
 */
public class SplashFrame {

    private JButton btnNewProject;
    private JButton btnOpenProject;
    private JButton btnOpenRecent;
    private JButton btnRecentProject;

    private JButton btnClassDiagram;
    private JButton btnComunicationDiagram;
    private JButton btnSequenceDiagram;
    private JButton btnStateDiagram;
    private JButton btnUseCaseDiagram;
    private JButton btnObjectDiagram;
    private JButton btnActivityDiagram;

    private JPanel welcomePanel = new JPanel();
    private JPanel openRecentPanel = new JPanel();
    private JPanel mainPanel = new JPanel();
    private JPanel viewsPanel = new JPanel();

    private CardLayout cardLayout = new CardLayout();
    private JFrame frame = new JFrame();
    private List<IDiagramPlugin> diagramPlugins;

    /**
     * Constructs frame with 3 buttons to create/open or open recent project.
     *
     * @param mainFrame it's for add workspace to main frame.
     */
    public SplashFrame(final MainFrame mainFrame) {
        mainPanel.setLayout(cardLayout);
        ResourceBundleInjector.getInjector().inject(this);
        BeanInjector.getInjector().inject(this);
        frame.setTitle(titleWindow);
        this.mainFrame = mainFrame;
        diagramPlugins = pluginRegistry.getDiagramPlugins();

        frame.setSize(300, 300);
        frame.setResizable(false);
        btnNewProject = new JButton(btnNameNewProject);
        btnOpenProject = new JButton(btnNameOpenProject);
        btnOpenRecent = new JButton(btnNameOpenRecent);

        btnActivityDiagram = new JButton(btnNameActivityDiagram);
        btnClassDiagram = new JButton(btnNameClassDiagram);
        btnComunicationDiagram = new JButton(btnNameComunicationDiagram);
        btnSequenceDiagram = new JButton(btnNameSequenceDiagram);
        btnStateDiagram = new JButton(btnNameStateDiagram);
        btnUseCaseDiagram = new JButton(btnNameUseCaseDiagram);
        btnObjectDiagram = new JButton(btnNameObjectDiagram);

        viewsPanel.add(btnComunicationDiagram);
        viewsPanel.add(btnSequenceDiagram);
        viewsPanel.add(btnStateDiagram);
        viewsPanel.add(btnUseCaseDiagram);
        viewsPanel.add(btnActivityDiagram);
        viewsPanel.add(btnClassDiagram);
        viewsPanel.add(btnObjectDiagram);
        viewsPanel.setLayout(new GridLayout(7, 1));

        welcomePanel = new JPanel();
        welcomePanel.setLayout(new GridLayout(3, 1));
        welcomePanel.add(btnNewProject);
        welcomePanel.add(btnOpenProject);
        welcomePanel.add(btnOpenRecent);

        mainPanel.add(welcomePanel, "mainPanelView");
        mainPanel.add(openRecentPanel, "recentPanelView");
        mainPanel.add(viewsPanel, "views");

        /**
         * Change the card to Views card.
         */
        btnNewProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                cardLayout.show(mainPanel, "views");
            }
        });

        /**
         * Opens JFileChooserService that allows us to open project.
         */
        btnOpenProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                IFile selectedFile = null;
                try {
                    ExtensionFilter[] filters = fileNamingService.getFileFilters();
                    IFileReader fileOpener = fileChooserService.chooseAndGetFileReader(filters);
                    if (fileOpener == null) {
                        // Action cancelled by user
                        return;
                    }
                    frame.dispose();
                    selectedFile = fileOpener.getFileDefinition();
                    IGraphFile graphFile = new GraphFile(selectedFile);
                    IWorkspace workspace = new Workspace(graphFile);
                    mainFrame.addWorkspace(workspace);

                } catch (IOException exception) {
                    exception.printStackTrace();
                }


            }
        });

        /**
         * Change card to recent files and allows us to pick the recent project.
         */
        btnOpenRecent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                cardLayout.show(mainPanel, "recentPanelView");

                for (final IFile aFile : userPreferencesService.getRecentFiles()) {
                    String name = aFile.getFilename();
                    btnRecentProject = new JButton(name);
                    openRecentPanel.add(btnRecentProject);

                    btnRecentProject.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent event) {
                            try {
                                frame.dispose();
                                IGraphFile graphFile = new GraphFile(aFile);
                                IWorkspace workspace = new Workspace(graphFile);
                                mainFrame.addWorkspace(workspace);
                            } catch (IOException exception) {
                                dialogFactory.showErrorDialog(dialogOpenFileErrorMessage + " : " + exception.getMessage());
                                userPreferencesService.removeOpenedFile(aFile);
                            }
                        }
                    });
                }
            }
        });

        generateButtons();

        cardLayout.show(mainPanel, "mainPanelView");
        frame.add(mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    /**
     * Opens a specific diagram that gets as an argument
     *
     * @param diagramPlugin Specific diagram
     */
    private void openThisDiagram(IDiagramPlugin diagramPlugin) {
        Class<? extends IGraph> graphClass = diagramPlugin.getGraphClass();
        IGraphFile graphFile = new GraphFile(graphClass);
        IWorkspace diagramPanel = new Workspace(graphFile);
        String name = diagramPlugin.getName();
        name = name.replaceFirst("[0-9]*\\.", "");
        name = unsavedPrefix + " " + name.toLowerCase();
        diagramPanel.setTitle(name);
        mainFrame.addWorkspace(diagramPanel);
        frame.dispose();
    }

    /**
     * Generates diagram buttons
     */
    private void generateButtons() {
        btnClassDiagram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                openThisDiagram(diagramPlugins.get(0));
            }
        });
        btnComunicationDiagram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                openThisDiagram(diagramPlugins.get(1));
            }
        });
        btnSequenceDiagram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                openThisDiagram(diagramPlugins.get(2));
            }
        });
        btnStateDiagram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                openThisDiagram(diagramPlugins.get(3));
            }
        });
        btnUseCaseDiagram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                openThisDiagram(diagramPlugins.get(4));
            }
        });
        btnActivityDiagram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                openThisDiagram(diagramPlugins.get(5));
            }
        });
        btnObjectDiagram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                openThisDiagram(diagramPlugins.get(6));
            }
        });
    }

    /**
     * Instance of MainFrame
     */
    private MainFrame mainFrame;

    /**
     * File services
     */
    @InjectedBean
    private FileNamingService fileNamingService;

    /**
     * The file chooser to use with with menu
     */
    @InjectedBean
    private IFileChooserService fileChooserService;

    /**
     * Access to user preferences
     */
    @InjectedBean
    private UserPreferencesService userPreferencesService;

    /**
     * DialogBox handler
     */
    @InjectedBean
    private DialogFactory dialogFactory;

    /**
     * Plugin registry
     */
    @InjectedBean
    private PluginRegistry pluginRegistry;

    /**
     * Open dialog if has error
     */
    @ResourceBundleBean(key = "dialog.open_file_failed.text")
    private String dialogOpenFileErrorMessage;

    /**
     * Prefix to unsaved files
     */
    @ResourceBundleBean(key = "workspace.unsaved_prefix")
    private String unsavedPrefix;

    /**
     * Name of splash window title
     */
    @ResourceBundleBean(key = "windowTitle")
    private String titleWindow;

    /**
     * Name of this buttons from properties file
     */
    @ResourceBundleBean(key = "nameBtnNewProject")
    private String btnNameNewProject;

    @ResourceBundleBean(key = "nameBtnOpenProject")
    private String btnNameOpenProject;

    @ResourceBundleBean(key = "nameBtnOpenRecent")
    private String btnNameOpenRecent;

    @ResourceBundleBean(key = "nameBtnActivityDiagram")
    private String btnNameActivityDiagram;

    @ResourceBundleBean(key = "nameBtnClassDiagram")
    private String btnNameClassDiagram;

    @ResourceBundleBean(key = "nameBtnComunicationDiagram")
    private String btnNameComunicationDiagram;

    @ResourceBundleBean(key = "nameBtnSequenceDiagram")
    private String btnNameSequenceDiagram;

    @ResourceBundleBean(key = "nameBtnStateDiagram")
    private String btnNameStateDiagram;

    @ResourceBundleBean(key = "nameBtnUseCaseDiagram")
    private String btnNameUseCaseDiagram;

    @ResourceBundleBean(key = "nameBtnObjectDiagram")
    private String btnNameObjectDiagram;
}
