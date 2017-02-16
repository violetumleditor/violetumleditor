/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.application.menu;

import com.horstmann.violet.application.ApplicationStopper;
import com.horstmann.violet.application.gui.MainFrame;
import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.file.GraphFile;
import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.IGraphFile;
import com.horstmann.violet.framework.file.chooser.IFileChooserService;
import com.horstmann.violet.framework.file.export.FileExportService;
import com.horstmann.violet.framework.file.naming.ExtensionFilter;
import com.horstmann.violet.framework.file.naming.FileNamingService;
import com.horstmann.violet.framework.file.persistence.IFilePersistenceService;
import com.horstmann.violet.framework.file.persistence.IFileReader;
import com.horstmann.violet.framework.file.persistence.IFileWriter;
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
import com.thoughtworks.xstream.io.StreamException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * Represents the file menu on the editor frame
 *
 * @author Alexandre de Pellegrin
 */
@ResourceBundleBean(resourceReference = MenuFactory.class)
public class FileMenu extends JMenu
{

    /**
     * Default constructor
     *
     * @param mainFrame
     */
    @ResourceBundleBean(key = "file")
    public FileMenu(final MainFrame mainFrame)
    {
        ResourceBundleInjector.getInjector().inject(this);
        BeanInjector.getInjector().inject(this);
        this.mainFrame = mainFrame;
        createMenu();
        addWindowsClosingListener();
    }

    /**
     * @return 'new file' menu
     */
    public JMenu getFileNewMenu()
    {
        return this.fileNewMenu;
    }

    /**
     * @return recently opened file menu
     */
    public JMenu getFileRecentMenu()
    {
        return this.fileRecentMenu;
    }

    /**
     * Initialize the menu
     */
    private void createMenu()
    {
        initFileNewMenu();
        initFileOpenItem();
        initFileCloseItem();
        initFileRecentMenu();
        initFileSaveItem();
        initFileSaveAsItem();
        initFileSaveToGoogleDrive();
        initFileExportMenu();
        initFilePrintItem();
        initFileExitItem();

        this.add(this.fileNewMenu);
        this.add(this.fileOpenItem);
        this.add(this.fileCloseItem);
        this.add(this.fileRecentMenu);
        this.add(this.fileSaveItem);
        this.add(this.fileSaveAsItem);
        this.add(this.fileSaveToGoogleDrive);
        this.add(this.fileExportMenu);
        this.add(this.filePrintItem);
        this.add(this.fileExitItem);

    }

    /**
     * Add frame listener to detect closing request
     */
    private void addWindowsClosingListener()
    {
        this.mainFrame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(final WindowEvent event)
            {
                stopper.exitProgram(mainFrame);
            }
        });
    }

    /**
     * Init exit menu entry
     */
    private void initFileExitItem()
    {
        this.fileExitItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                stopper.exitProgram(mainFrame);
            }
        });
        if (this.fileChooserService == null) this.fileExitItem.setEnabled(false);
    }

    /**
     * Init export submenu
     */
    private void initFileExportMenu()
    {
        initFileExportToImageItem();
        initFileExportToClipboardItem();
        initFileExportToPdfItem();
        initFileExportToJavaItem();
        initFileExportToPythonItem();

        this.fileExportMenu.add(this.fileExportToImageItem);
        this.fileExportMenu.add(this.fileExportToClipBoardItem);
        this.fileExportMenu.add(this.fileExportToPdfItem);
        // this.fileExportMenu.add(this.fileExportToJavaItem);
        // this.fileExportMenu.add(this.fileExportToPythonItem);

        if (this.fileChooserService == null) this.fileExportMenu.setEnabled(false);
    }

    /**
     * Init export to python menu entry
     */
    private void initFileExportToPythonItem()
    {
        this.fileExportToPythonItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                final IWorkspace workspace = (Workspace) mainFrame.getActiveWorkspace();
                if (workspace != null)
                {
                }
            }
        });
    }

    /**
     * Init export to java menu entry
     */
    private void initFileExportToJavaItem()
    {
        this.fileExportToJavaItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                final IWorkspace workspace = (Workspace) mainFrame.getActiveWorkspace();
                if (workspace != null)
                {
                }
            }
        });
    }

    /**
     * Init export to clipboard menu entry
     */
    private void initFileExportToClipboardItem()
    {
        this.fileExportToClipBoardItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                final IWorkspace workspace = (Workspace) mainFrame.getActiveWorkspace();
                if (workspace != null)
                {
                    workspace.getGraphFile().exportToClipboard();
                }
            }
        });
    }

    /**
     * Init export to image menu entry
     */
    private void initFileExportToImageItem()
    {
        this.fileExportToImageItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                final IWorkspace workspace = (Workspace) mainFrame.getActiveWorkspace();
                if (workspace != null)
                {
                    try
                    {
                        final ExtensionFilter[] exportFilters = fileNamingService.getImageExtensionFilters();
                        final IFileWriter fileSaver = fileChooserService.chooseAndGetFileWriter(exportFilters);
                        final OutputStream out = fileSaver.getOutputStream();
                        if (out != null)
                        {
                            final String filename = fileSaver.getFileDefinition().getFilename();
                            for (final ExtensionFilter exportFilter : exportFilters)
                            {
                                final String extension = exportFilter.getExtension();
                                if (filename.toLowerCase().endsWith(extension.toLowerCase()))
                                {
                                    final String format = extension.replace(".", "");
                                    workspace.getGraphFile().exportImage(out, format);
                                    break;
                                }
                            }
                        }
                    }
                    catch (final Exception e1)
                    {
                        throw new RuntimeException(e1);
                    }
                }
            }
        });
    }

    private void initFileExportToPdfItem()
    {
        this.fileExportToPdfItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                final IWorkspace workspace = (Workspace) mainFrame.getActiveWorkspace();
                if (workspace != null)
                {
                    try {
                        final ExtensionFilter extensionFilter = fileNamingService.getPdfExtensionFilter();
                        final IFileWriter fileSaver = fileChooserService.chooseAndGetFileWriter(extensionFilter);
                        final OutputStream out = fileSaver.getOutputStream();
                        if(null == out)
                        {
                            throw new IOException("Unable to get output stream for extension "
                                                    + extensionFilter.getExtension());
                        }
                        final String filename = fileSaver.getFileDefinition().getFilename();
                        workspace.getGraphFile().exportToPdf(out);
                    }
                    catch (final IOException e1)
                    {
                        final String message = MessageFormat.format(fileExportErrorMessage, e1.getMessage());
                        JOptionPane.showMessageDialog(null, message, fileExportError, JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        });
    }

    /**
     * Init 'save as' menu entry
     */
    private void initFileSaveAsItem()
    {
        this.fileSaveAsItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                final IWorkspace workspace = (Workspace) mainFrame.getActiveWorkspace();
                if (workspace != null)
                {
                    final IGraphFile graphFile = workspace.getGraphFile();
                    graphFile.saveToNewLocation();
                    userPreferencesService.addRecentFile(graphFile);
                }
            }
        });
        if (this.fileChooserService == null) this.fileSaveAsItem.setEnabled(false);
    }

    private void initFileSaveToGoogleDrive()
    {
        this.fileSaveToGoogleDrive.addActionListener((event) ->
        {
            if (!mainFrame.isThereAnyDiagramDisplayed())
            {
                dialogFactory.showErrorDialog(dialogSaveNothingToSaveMessage);
                return;
            }

            saveToGoogleDrive();
        });
    }

    private void saveToGoogleDrive()
    {
        try
        {
            final IWorkspace workspace = mainFrame.getActiveWorkspace();
            final IGraphFile graphFile = workspace.getGraphFile();
            graphFile.saveToGoogleDrive();
        }
        catch (GeneralSecurityException | IOException e)
        {
            dialogFactory.showErrorDialog(googleDriveSaveFileErrorMessage);
        }
    }

    /**
     * Init save menu entry
     */
    private void initFileSaveItem()
    {
        this.fileSaveItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                final IWorkspace workspace = mainFrame.getActiveWorkspace();
                if (workspace != null)
                {
                    final IGraphFile graphFile = workspace.getGraphFile();
                    graphFile.save();
                    userPreferencesService.addRecentFile(graphFile);
                }
            }
        });
        if (this.fileChooserService == null || (this.fileChooserService != null && this.fileChooserService.isWebStart()))
        {
            this.fileSaveItem.setEnabled(false);
        }
    }

    /**
     * Init print menu entry
     */
    private void initFilePrintItem()
    {
        this.filePrintItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                final IWorkspace workspace = (Workspace) mainFrame.getActiveWorkspace();
                if (workspace != null)
                {
                    workspace.getGraphFile().exportToPrinter();
                }
            }
        });
        if (this.fileChooserService == null) this.filePrintItem.setEnabled(false);
    }

    /**
     * Init close menu entry
     */
    private void initFileCloseItem()
    {
        this.fileCloseItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent event)
            {
                IWorkspace workspace = null;
                try
                {
                    workspace = (Workspace) mainFrame.getActiveWorkspace();
                }
                catch (final RuntimeException e)
                {
                    // If no diagram is opened, close app
                    stopper.exitProgram(mainFrame);
                }
                if (workspace != null)
                {
                    final IGraphFile graphFile = workspace.getGraphFile();
                    graphFile.removeBackup();
                    if (graphFile.isSaveRequired())
                    {
                        final JOptionPane optionPane = new JOptionPane();
                        optionPane.setMessage(dialogCloseMessage);
                        optionPane.setOptionType(JOptionPane.YES_NO_CANCEL_OPTION);
                        optionPane.setIcon(dialogCloseIcon);
                        dialogFactory.showDialog(optionPane, dialogCloseTitle, true);

                        int result = JOptionPane.CANCEL_OPTION;
                        if (!JOptionPane.UNINITIALIZED_VALUE.equals(optionPane.getValue()))
                        {
                            result = ((Integer) optionPane.getValue()).intValue();
                        }

                        if (result == JOptionPane.YES_OPTION)
                        {
                            final String filename = graphFile.getFilename();
                            if (filename == null)
                            {
                                graphFile.saveToNewLocation();
                                userPreferencesService.addRecentFile(graphFile);
                            }
                            if (filename != null)
                            {
                                graphFile.save();
                            }
                            if (!graphFile.isSaveRequired())
                            {
                                mainFrame.closeTabWithWorkspace(workspace);
                                userPreferencesService.removeOpenedFile(graphFile);
                            }
                        }
                        if (result == JOptionPane.NO_OPTION)
                        {
                            mainFrame.closeTabWithWorkspace(workspace);
                            userPreferencesService.removeOpenedFile(graphFile);
                        }
                    }
                    if (!graphFile.isSaveRequired())
                    {
                        mainFrame.closeTabWithWorkspace(workspace);
                        userPreferencesService.removeOpenedFile(graphFile);
                    }
                    final List<IWorkspace> workspaceList = mainFrame.getWorkspaceList();
                    if (workspaceList.size() == 0)
                    {
                        mainFrame.requestFocus();
                    }
                }
            }
        });
    }

    /**
     * Init open menu entry
     */
    private void initFileOpenItem()
    {
        this.fileOpenItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent event)
            {
                IFile selectedFile = null;
                try
                {
                    final ExtensionFilter[] filters = fileNamingService.getFileFilters();
                    final IFileReader fileOpener = fileChooserService.chooseAndGetFileReader(filters);
                    if (fileOpener == null)
                    {
                        // Action cancelled by user
                        return;
                    }
                    selectedFile = fileOpener.getFileDefinition();
                    final IGraphFile graphFile = new GraphFile(selectedFile);
                    final IWorkspace workspace = new Workspace(graphFile);
                    mainFrame.addWorkspace(workspace);
                    userPreferencesService.addOpenedFile(graphFile);
                    userPreferencesService.addRecentFile(graphFile);
                }
                catch (final StreamException se)
                {
                    dialogFactory.showErrorDialog(dialogOpenFileIncompatibilityMessage);
                }
                catch (final Exception e)
                {
                    dialogFactory.showErrorDialog(dialogOpenFileErrorMessage + " : " + e.getMessage());
                }
            }
        });
        if (this.fileChooserService == null) this.fileOpenItem.setEnabled(false);
    }

    /**
     * Init new menu entry
     */
    public void initFileNewMenu()
    {
        final List<IDiagramPlugin> diagramPlugins = this.pluginRegistry.getDiagramPlugins();

        // Step 1 : sort diagram plugins by categories and names
        final SortedMap<String, SortedSet<IDiagramPlugin>> diagramPluginsSortedByCategory = new TreeMap<String, SortedSet<IDiagramPlugin>>();
        for (final IDiagramPlugin aDiagramPlugin : diagramPlugins)
        {
            final String category = aDiagramPlugin.getCategory();
            if (!diagramPluginsSortedByCategory.containsKey(category))
            {
                final SortedSet<IDiagramPlugin> newSortedSet = new TreeSet<IDiagramPlugin>(new Comparator<IDiagramPlugin>()
                {
                    @Override
                    public int compare(final IDiagramPlugin o1, final IDiagramPlugin o2)
                    {
                        final String n1 = o1.getName();
                        final String n2 = o2.getName();
                        return n1.compareTo(n2);
                    }
                });
                diagramPluginsSortedByCategory.put(category, newSortedSet);
            }
            final SortedSet<IDiagramPlugin> aSortedSet = diagramPluginsSortedByCategory.get(category);
            aSortedSet.add(aDiagramPlugin);
        }
        // Step 2 : populate menu entry
		
        for (final String aCategory : diagramPluginsSortedByCategory.keySet())
        {
            final String categoryName = aCategory.replaceFirst("[0-9]*\\.", "");
            final JMenu categorySubMenu = new JMenu(categoryName);
            Dimension preferredSize = categorySubMenu.getPreferredSize();
            preferredSize = new Dimension((int) preferredSize.getWidth() + 30, (int) preferredSize.getHeight());
            categorySubMenu.setPreferredSize(preferredSize);
            fileNewMenu.add(categorySubMenu);
            final SortedSet<IDiagramPlugin> diagramPluginsByCategory = diagramPluginsSortedByCategory.get(aCategory);
            for (final IDiagramPlugin aDiagramPlugin : diagramPluginsByCategory)
            {
                String name = aDiagramPlugin.getName();
                name = name.replaceFirst("[0-9]*\\.", "");
                final JMenuItem item = new JMenuItem(name);
                final ImageIcon sampleDiagramImage = getSampleDiagramImage(aDiagramPlugin);
                if (sampleDiagramImage != null)
                {
                    item.setRolloverIcon(sampleDiagramImage);
                }
                item.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(final ActionEvent event)
                    {
                        final Class<? extends IGraph> graphClass = aDiagramPlugin.getGraphClass();
                        final IGraphFile graphFile = new GraphFile(graphClass);
                        final IWorkspace diagramPanel = new Workspace(graphFile);
                        String name = aDiagramPlugin.getName();
                        name = name.replaceFirst("[0-9]*\\.", "");
                        name = unsavedPrefix + " " + name.toLowerCase();
                        diagramPanel.setTitle(name);
                        mainFrame.addWorkspace(diagramPanel);
					}
				});
				categorySubMenu.add( item );
			}
		}
	}

   
    /**
     * Init recent menu entry
     */
    public void initFileRecentMenu()
    {
        // Set entries on startup
        refreshFileRecentMenu();
        // Refresh recent files list each time the global file menu gets the focus
        this.addFocusListener(new FocusListener()
        {

            @Override
            public void focusGained(final FocusEvent e)
            {
                refreshFileRecentMenu();
            }

            @Override
            public void focusLost(final FocusEvent e)
            {
                // Nothing to do
            }

        });
        if (this.fileChooserService == null || (this.fileChooserService != null && this.fileChooserService.isWebStart()))
        {
            this.fileRecentMenu.setEnabled(false);
        }
    }

    /**
     * Updates file recent menu
     */
    private void refreshFileRecentMenu()
    {
        fileRecentMenu.removeAll();
        for (final IFile aFile : userPreferencesService.getRecentFiles())
        {
            final String name = aFile.getFilename();
            final JMenuItem item = new JMenuItem(name);
            fileRecentMenu.add(item);
            item.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(final ActionEvent event)
                {
                    try
                    {
                        final IGraphFile graphFile = new GraphFile(aFile);
                        final IWorkspace workspace = new Workspace(graphFile);
                        mainFrame.addWorkspace(workspace);
                        userPreferencesService.addOpenedFile(aFile);
                        userPreferencesService.addRecentFile(aFile);
                    }
                    catch (final Exception e)
                    {
                        dialogFactory.showErrorDialog(dialogOpenFileErrorMessage + " : " + e.getMessage());
                        userPreferencesService.removeOpenedFile(aFile);
                    }
                }
            });
        }
    }

    /**
     * @param diagramPlugin
     * @return an image exported from the sample diagram file attached to each plugin or null if no one exists
     * @throws IOException
     */
    private ImageIcon getSampleDiagramImage(final IDiagramPlugin diagramPlugin)
    {
        try
        {
            final String sampleFilePath = diagramPlugin.getSampleFilePath();
            final InputStream resourceAsStream = getClass().getResourceAsStream("/" + sampleFilePath);
            if (resourceAsStream == null)
            {
                return null;
            }
            final IGraph graph = this.filePersistenceService.read(resourceAsStream);
            final BufferedImage image = FileExportService.getImage(graph);

            final JLabel label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            label.setIcon(new ImageIcon(image));
            label.setSize(new Dimension(600, 550));
            label.setBackground(Color.WHITE);
            label.setOpaque(true);
            final Dimension size = label.getSize();
            final BufferedImage image2 = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
            final Graphics2D g2 = image2.createGraphics();
            label.paint(g2);
            return new ImageIcon(image2);
        }
        catch (final Exception e)
        {
            // Failed to load sample. It doesn"t matter.
            return null;
        }
    }

    /**
     * The file chooser to use with with menu
     */
    @InjectedBean
    private IFileChooserService fileChooserService;

    /**
     * Application stopper
     */
    private final ApplicationStopper stopper = new ApplicationStopper();

    /**
     * Plugin registry
     */
    @InjectedBean
    private PluginRegistry pluginRegistry;

    /**
     * DialogBox handler
     */
    @InjectedBean
    private DialogFactory dialogFactory;

    /**
     * Access to user preferences
     */
    @InjectedBean
    private UserPreferencesService userPreferencesService;

    /**
     * File services
     */
    @InjectedBean
    private FileNamingService fileNamingService;

    /**
     * Service to convert IGraph to XML content (and XML to IGraph of course)
     */
    @InjectedBean
    private IFilePersistenceService filePersistenceService;

    /**
     * Application main frame
     */
    private final MainFrame mainFrame;

    @ResourceBundleBean(key = "file.new")
    private JMenu fileNewMenu;

    @ResourceBundleBean(key = "file.open")
    private JMenuItem fileOpenItem;

    @ResourceBundleBean(key = "file.recent")
    private JMenu fileRecentMenu;

    @ResourceBundleBean(key = "file.close")
    private JMenuItem fileCloseItem;

    @ResourceBundleBean(key = "file.save")
    private JMenuItem fileSaveItem;

    @ResourceBundleBean(key = "file.save_as")
    private JMenuItem fileSaveAsItem;

    @ResourceBundleBean(key = "file.save_to_google_drive")
    private JMenuItem fileSaveToGoogleDrive;

    @ResourceBundleBean(key = "file.export_to_pdf")
    private JMenuItem fileExportToPdfItem;

    @ResourceBundleBean(key = "file.export_to_image")
    private JMenuItem fileExportToImageItem;

    @ResourceBundleBean(key = "file.export_to_clipboard")
    private JMenuItem fileExportToClipBoardItem;

    @ResourceBundleBean(key = "file.export_to_java")
    private JMenuItem fileExportToJavaItem;

    @ResourceBundleBean(key = "file.export_to_python")
    private JMenuItem fileExportToPythonItem;

    @ResourceBundleBean(key = "file.export")
    private JMenu fileExportMenu;

    @ResourceBundleBean(key = "file.print")
    private JMenuItem filePrintItem;

    @ResourceBundleBean(key = "file.exit")
    private JMenuItem fileExitItem;

    @ResourceBundleBean(key = "dialog.close.title")
    private String dialogCloseTitle;

    @ResourceBundleBean(key = "dialog.close.ok")
    private String dialogCloseMessage;

    @ResourceBundleBean(key = "dialog.close.icon")
    private ImageIcon dialogCloseIcon;

    @ResourceBundleBean(key = "dialog.open_file_failed.text")
    private String dialogOpenFileErrorMessage;

    @ResourceBundleBean(key = "dialog.open_file_content_incompatibility.text")
    private String dialogOpenFileIncompatibilityMessage;

    @ResourceBundleBean(key = "dialog.save_nothing_to_save.text")
    private String dialogSaveNothingToSaveMessage;

    @ResourceBundleBean(key = "dialog.google_drive_save_error.text")
    private String googleDriveSaveFileErrorMessage;

    @ResourceBundleBean(key = "workspace.unsaved_prefix")
    private String unsavedPrefix;

    @ResourceBundleBean(key = "file.export.error")
    private String fileExportError;

    @ResourceBundleBean(key = "file.export.error.message")
    private String fileExportErrorMessage;

}
