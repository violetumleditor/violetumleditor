package com.horstmann.violet.application.cheerpj;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.horstmann.violet.application.gui.MainFrame;
import com.horstmann.violet.application.menu.MenuFactory;
import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.file.IGraphFile;
import com.horstmann.violet.framework.file.chooser.IFileChooserService;
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
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.Workspace;

@ResourceBundleBean(resourceReference = MenuFactory.class)
public class CheerpJFileMenu extends JMenu {

    private static final String FILES_DIRECTORY = "/files";

    private final MainFrame mainFrame;

    @ResourceBundleBean(key = "file.import")
    private JMenuItem fileImportItem;

    @ResourceBundleBean(key = "file.export_to_diagram")
    private JMenuItem fileExportToDiagramItem;

    @ResourceBundleBean(key = "file.new")
    private JMenu fileNewMenu;

    @ResourceBundleBean(key = "file.open")
    private JMenuItem fileOpenItem;

    @ResourceBundleBean(key = "file.close")
    private JMenuItem fileCloseItem;

    @ResourceBundleBean(key = "file.save")
    private JMenuItem fileSaveItem;

    @ResourceBundleBean(key = "file.save_as")
    private JMenuItem fileSaveAsItem;

    @ResourceBundleBean(key = "file.export")
    private JMenu fileExportMenu;

    @ResourceBundleBean(key = "file.export_to_image")
    private JMenuItem fileExportToImageItem;

    @ResourceBundleBean(key = "file.export_to_clipboard")
    private JMenuItem fileExportToClipboardItem;

    @ResourceBundleBean(key = "file.export_to_pdf")
    private JMenuItem fileExportToPdfItem;

    @ResourceBundleBean(key = "file.print")
    private JMenuItem filePrintItem;

    @ResourceBundleBean(key = "dialog.close.title")
    private String dialogCloseTitle;

    @ResourceBundleBean(key = "dialog.close.ok")
    private String dialogCloseMessage;

    @ResourceBundleBean(key = "dialog.open_file_failed.text")
    private String dialogOpenFileErrorMessage;

    @ResourceBundleBean(key = "workspace.unsaved_prefix")
    private String unsavedPrefix;

    @ResourceBundleBean(key = "file.export.error")
    private String fileExportError;

    @ResourceBundleBean(key = "file.export.error.message")
    private String fileExportErrorMessage;

    @InjectedBean
    private PluginRegistry pluginRegistry;

    @InjectedBean
    private FileNamingService fileNamingService;

    @InjectedBean
    private IFilePersistenceService filePersistenceService;

    @InjectedBean
    private DialogFactory dialogFactory;

    @InjectedBean
    private IFileChooserService fileChooserService;

    @ResourceBundleBean(key = "file")
    public CheerpJFileMenu(MainFrame mainFrame) {
        ResourceBundleInjector.getInjector().inject(this);
        BeanInjector.getInjector().inject(this);
        this.mainFrame = mainFrame;
        createMenu();
    }

    private void createMenu() {
        initFileNewMenu();
        initOpenItem();
        initCloseItem();
        initSaveItem();
        initSaveAsItem();
        initImportItem();
        initExportMenu();
        initPrintItem();

        this.add(this.fileNewMenu);
        this.add(this.fileOpenItem);
        this.add(this.fileCloseItem);
        this.add(this.fileSaveItem);
        this.add(this.fileSaveAsItem);
        this.add(this.fileImportItem);
        this.add(this.fileExportMenu);
        this.add(this.filePrintItem);
    }

    public JMenu getFileNewMenu() {
        return this.fileNewMenu;
    }

    public void initFileNewMenu() {
        this.fileNewMenu.removeAll();
        List<IDiagramPlugin> diagramPlugins = this.pluginRegistry.getDiagramPlugins();

        SortedMap<String, SortedSet<IDiagramPlugin>> diagramPluginsSortedByCategory = new TreeMap<String, SortedSet<IDiagramPlugin>>();
        for (final IDiagramPlugin aDiagramPlugin : diagramPlugins) {
            String category = aDiagramPlugin.getCategory();
            if (!diagramPluginsSortedByCategory.containsKey(category)) {
                SortedSet<IDiagramPlugin> newSortedSet = new TreeSet<IDiagramPlugin>(new Comparator<IDiagramPlugin>() {
                    @Override
                    public int compare(IDiagramPlugin o1, IDiagramPlugin o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                diagramPluginsSortedByCategory.put(category, newSortedSet);
            }
            diagramPluginsSortedByCategory.get(category).add(aDiagramPlugin);
        }

        for (String aCategory : diagramPluginsSortedByCategory.keySet()) {
            String categoryName = aCategory.replaceFirst("[0-9]*\\.", "");
            JMenu categorySubMenu = new JMenu(categoryName);
            Dimension preferredSize = categorySubMenu.getPreferredSize();
            preferredSize = new Dimension((int) preferredSize.getWidth() + 30, (int) preferredSize.getHeight());
            categorySubMenu.setPreferredSize(preferredSize);
            this.fileNewMenu.add(categorySubMenu);

            SortedSet<IDiagramPlugin> diagramPluginsByCategory = diagramPluginsSortedByCategory.get(aCategory);
            for (final IDiagramPlugin aDiagramPlugin : diagramPluginsByCategory) {
                String name = aDiagramPlugin.getName().replaceFirst("[0-9]*\\.", "");
                JMenuItem item = new JMenuItem(name);
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        createNewWorkspace(aDiagramPlugin);
                    }
                });
                categorySubMenu.add(item);
            }
        }
    }

    private void createNewWorkspace(IDiagramPlugin diagramPlugin) {
        try {
            IGraph graph = diagramPlugin.getGraphClass().getDeclaredConstructor().newInstance();
            CheerpJStorageGraphFile graphFile = new CheerpJStorageGraphFile(graph, null);
            IWorkspace workspace = new Workspace(graphFile);
            String name = diagramPlugin.getName().replaceFirst("[0-9]*\\.", "");
            workspace.setTitle(this.unsavedPrefix + " " + name.toLowerCase());
            this.mainFrame.addWorkspace(workspace);
        } catch (Exception e) {
            this.dialogFactory.showErrorDialog(e.getMessage());
        }
    }

    private void initOpenItem() {
        clearActionListeners(this.fileOpenItem);
        this.fileOpenItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                openFromFilesMount();
            }
        });
    }

    private void initImportItem() {
        clearActionListeners(this.fileImportItem);
        this.fileImportItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                importFromLocalDrive();
            }
        });
    }

    private void initSaveItem() {
        clearActionListeners(this.fileSaveItem);
        this.fileSaveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCurrentWorkspace(false);
            }
        });
    }

    private void initSaveAsItem() {
        clearActionListeners(this.fileSaveAsItem);
        this.fileSaveAsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCurrentWorkspace(true);
            }
        });
    }

    private void initCloseItem() {
        clearActionListeners(this.fileCloseItem);
        this.fileCloseItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeCurrentWorkspace();
            }
        });
    }

    private void initPrintItem() {
        clearActionListeners(this.filePrintItem);
        this.filePrintItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IWorkspace workspace = mainFrame.getActiveWorkspace();
                if (workspace == null) {
                    return;
                }
                try {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    workspace.getGraphFile().exportToPdf(out);
                    out.close();
                    CheerpJInterfaceService.openPdfPrintTab(out.toByteArray());
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        });
    }

    private void initExportMenu() {
        this.fileExportMenu.removeAll();
        initExportToImageItem();
        initExportToClipboardItem();
        initExportToPdfItem();
        initExportToDiagramItem();

        this.fileExportMenu.add(this.fileExportToImageItem);
        this.fileExportMenu.add(this.fileExportToClipboardItem);
        this.fileExportMenu.add(this.fileExportToPdfItem);
        this.fileExportMenu.add(this.fileExportToDiagramItem);
    }

    private void initExportToImageItem() {
        clearActionListeners(this.fileExportToImageItem);
        this.fileExportToImageItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IWorkspace workspace = mainFrame.getActiveWorkspace();
                if (workspace == null) {
                    return;
                }
                try {
                    ExtensionFilter[] exportFilters = fileNamingService.getImageExtensionFilters();
                    IFileWriter fileSaver = fileChooserService.chooseAndGetFileWriter(exportFilters);
                    OutputStream out = fileSaver.getOutputStream();
                    String filename = fileSaver.getFileDefinition().getFilename();
                    for (ExtensionFilter exportFilter : exportFilters) {
                        String extension = exportFilter.getExtension();
                        if (filename.toLowerCase().endsWith(extension.toLowerCase())) {
                            String format = extension.replace(".", "");
                            workspace.getGraphFile().exportImage(out, format);
                            break;
                        }
                    }
                    out.close();
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        });
    }

    private void initExportToClipboardItem() {
        clearActionListeners(this.fileExportToClipboardItem);
        this.fileExportToClipboardItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IWorkspace workspace = mainFrame.getActiveWorkspace();
                if (workspace == null) {
                    return;
                }
                workspace.getGraphFile().exportToClipboard();
            }
        });
    }

    private void initExportToPdfItem() {
        clearActionListeners(this.fileExportToPdfItem);
        this.fileExportToPdfItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IWorkspace workspace = mainFrame.getActiveWorkspace();
                if (workspace == null) {
                    return;
                }
                try {
                    IFileWriter fileSaver = fileChooserService.chooseAndGetFileWriter(fileNamingService.getPdfExtensionFilter());
                    OutputStream out = fileSaver.getOutputStream();
                    workspace.getGraphFile().exportToPdf(out);
                    out.close();
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        });
    }

    private void initExportToDiagramItem() {
        clearActionListeners(this.fileExportToDiagramItem);
        this.fileExportToDiagramItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IWorkspace workspace = mainFrame.getActiveWorkspace();
                if (workspace == null) {
                    return;
                }
                try {
                    IGraphFile graphFile = workspace.getGraphFile();
                    if (graphFile == null) {
                        return;
                    }
                    // If workspace has unsaved changes, save first
                    if (graphFile.isSaveRequired()) {
                        if (!saveCurrentWorkspace(false)) {
                            return;
                        }
                    }
                    // If file is not stored in browser local storage, prompt Save As so it can be exported
                    String filename = resolveStorageFilename(graphFile);
                    if (filename == null) {
                        if (!saveCurrentWorkspace(true)) {
                            return;
                        }
                        // Try to get a usable filename: prefer storage filename, otherwise workspace title
                        filename = resolveStorageFilename(workspace.getGraphFile());
                        if (filename == null) {
                            filename = workspace.getTitle();
                        }
                    }

                    IFileWriter writer = new CheerpJDownloadFileWriter(filename);
                    OutputStream out = writer.getOutputStream();
                    filePersistenceService.write(workspace.getGraphFile().getGraph(), out);
                    out.close();
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        });
    }

    private void closeCurrentWorkspace() {
        IWorkspace workspace = this.mainFrame.getActiveWorkspace();
        if (workspace == null) {
            return;
        }
        IGraphFile graphFile = workspace.getGraphFile();
        if (graphFile.isSaveRequired()) {
            int result = JOptionPane.showConfirmDialog(this.mainFrame, this.dialogCloseMessage, this.dialogCloseTitle,
                    JOptionPane.YES_NO_CANCEL_OPTION);
            if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                return;
            }
            if (result == JOptionPane.YES_OPTION && !saveCurrentWorkspace(false)) {
                return;
            }
        }
        this.mainFrame.removeWorkspace(workspace);
    }

    private boolean saveCurrentWorkspace(boolean saveAs) {
        IWorkspace workspace = this.mainFrame.getActiveWorkspace();
        if (workspace == null) {
            return false;
        }
        IGraphFile graphFile = workspace.getGraphFile();
        String filename = resolveStorageFilename(graphFile);
        if (saveAs || filename == null) {
            String proposal = filename == null ? "diagram.violet.html" : filename;
            String input = JOptionPane.showInputDialog(this.mainFrame, "File name", proposal);
            if (input == null) {
                return false;
            }
            filename = normalizeDiagramFilename(input);
        }

        try {
            if (graphFile instanceof CheerpJStorageGraphFile) {
                CheerpJStorageGraphFile cheerpJGraphFile = (CheerpJStorageGraphFile) graphFile;
                cheerpJGraphFile.setStorageFilename(filename);
                cheerpJGraphFile.save();
            } else {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                this.filePersistenceService.write(graphFile.getGraph(), out);
                out.close();
                saveDiagramToFilesMount(filename, out.toByteArray());
            }
            workspace.setTitle(filename);
            return true;
        } catch (Exception e) {
            showError(e);
            return false;
        }
    }

    private String resolveStorageFilename(IGraphFile graphFile) {
        if (graphFile == null) {
            return null;
        }
        if (!FILES_DIRECTORY.equals(graphFile.getDirectory())) {
            return null;
        }
        return normalizeDiagramFilename(graphFile.getFilename());
    }

    private String normalizeDiagramFilename(String filename) {
        if (filename == null) {
            return null;
        }
        String trimmed = filename.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        // Accept the filename as provided (keep any extension the user supplied)
        return trimmed;
    }

    private void openFromFilesMount() {
        try {
            String[] filenames = listFilesMountDiagrams();
            if (filenames == null || filenames.length == 0) {
                this.dialogFactory.showErrorDialog(this.dialogOpenFileErrorMessage + " : no diagram in /files");
                return;
            }
            Arrays.sort(filenames);
            String selected = (String) JOptionPane.showInputDialog(this.mainFrame, "Open diagram from /files", "Open",
                    JOptionPane.PLAIN_MESSAGE, null, filenames, filenames[0]);
            if (selected == null) {
                return;
            }
            byte[] content = Files.readAllBytes(Path.of(FILES_DIRECTORY, selected));
            if (content == null || content.length == 0) {
                this.dialogFactory.showErrorDialog(this.dialogOpenFileErrorMessage + " : empty diagram");
                return;
            }
            IGraph graph = readGraph(content);
            CheerpJStorageGraphFile graphFile = new CheerpJStorageGraphFile(graph, selected);
            IWorkspace workspace = new Workspace(graphFile);
            this.mainFrame.addWorkspace(workspace);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void importFromLocalDrive() {
        try {
            ExtensionFilter[] filters = this.fileNamingService.getFileFilters();
            IFileReader fileReader = this.fileChooserService.chooseAndGetFileReader(filters);
            if (fileReader == null) {
                return;
            }
            InputStream in = fileReader.getInputStream();
            byte[] content = in.readAllBytes();
            in.close();
            String filename = normalizeDiagramFilename(fileReader.getFileDefinition().getFilename());
            if (filename == null) {
                filename = "diagram.violet.html";
            }
            saveDiagramToFilesMount(filename, content);
            IGraph graph = readGraph(content);
            CheerpJStorageGraphFile graphFile = new CheerpJStorageGraphFile(graph, filename);
            IWorkspace workspace = new Workspace(graphFile);
            this.mainFrame.addWorkspace(workspace);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void saveDiagramToFilesMount(String filename, byte[] content) throws IOException {
        Path path = Path.of(FILES_DIRECTORY, filename);
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path, content);
    }

    private String[] listFilesMountDiagrams() {
        File directory = new File(FILES_DIRECTORY);
        if (!directory.exists() || !directory.isDirectory()) {
            return new String[0];
        }
        File[] files = directory.listFiles(File::isFile);
        if (files == null || files.length == 0) {
            return new String[0];
        }
        String[] names = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            names[i] = files[i].getName();
        }
        return names;
    }

    private IGraph readGraph(byte[] content) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(content);
        return this.filePersistenceService.read(in);
    }

    private void clearActionListeners(JMenuItem item) {
        for (ActionListener listener : item.getActionListeners()) {
            item.removeActionListener(listener);
        }
    }

    private void showError(Exception e) {
        String message = MessageFormat.format(this.fileExportErrorMessage, e.getMessage());
        JOptionPane.showMessageDialog(this.mainFrame, message, this.fileExportError, JOptionPane.ERROR_MESSAGE);
    }
}