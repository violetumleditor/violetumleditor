package com.horstmann.violet.application.cheerpj;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.horstmann.violet.application.menu.FileMenu;
import com.horstmann.violet.application.menu.MenuFactory;
import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.file.GraphFile;
import com.horstmann.violet.framework.file.IFile;
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
import com.horstmann.violet.framework.userpreferences.UserPreferencesService;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.Workspace;

@ResourceBundleBean(resourceReference = MenuFactory.class)
public class CheerpJFileMenuCustomizerCommand {

	private static final String FILES_DIRECTORY = "/files";

    private FileMenu fileMenu;

    @ResourceBundleBean(key = "file.import")
    private JMenu fileImportMenu;

    @ResourceBundleBean(key = "file.import_from_diagram")
    private JMenuItem fileImportFromDiagramItem;

    @ResourceBundleBean(key = "file.export_to_diagram")
    private JMenuItem fileExportToDiagramItem;

    @ResourceBundleBean(key = "dialog.open_file_failed.text")
    private String dialogOpenFileErrorMessage;

    @ResourceBundleBean(key = "dialog.open_from_storage.title")
    private String dialogOpenFromStorageTitle;

    @ResourceBundleBean(key = "dialog.open_from_storage.empty")
    private String dialogOpenFromStorageEmptyMessage;

    @InjectedBean
    private FileNamingService fileNamingService;

    @InjectedBean
    private IFilePersistenceService filePersistenceService;

    @InjectedBean
    private DialogFactory dialogFactory;

    @InjectedBean
    private IFileChooserService fileChooserService;

    @InjectedBean
    private UserPreferencesService userPreferencesService;

    public CheerpJFileMenuCustomizerCommand(FileMenu fileMenu) {
        ResourceBundleInjector.getInjector().inject(this);
        BeanInjector.getInjector().inject(this);
        this.fileMenu = fileMenu;
    }

	public FileMenu execute() {
        // Change print action to use CheerpJ printing
        for (ActionListener listener : this.fileMenu.getFilePrintItem().getActionListeners()) {
            this.fileMenu.getFilePrintItem().removeActionListener(listener);
        }
        this.fileMenu.getFilePrintItem().addActionListener(e -> runPrintWorkspace());

        // Override the "Open" action to pick a diagram already stored in the browser datastore (/files).
        // Files from the local drive are brought in through "Import from > Diagram file".
        for (ActionListener listener : this.fileMenu.getFileOpenItem().getActionListeners()) {
            this.fileMenu.getFileOpenItem().removeActionListener(listener);
        }
        this.fileMenu.getFileOpenItem().addActionListener(e -> runOpenFromBrowserStorage());

        // Hide the "Exit" menu item
        this.fileMenu.getFileExitItem().setVisible(false);

        // Add "Import from" submenu with "Diagram file" item
        this.fileImportMenu.add(this.fileImportFromDiagramItem);
        this.fileImportFromDiagramItem.addActionListener(e -> runImportFromLocalDrive());
        this.fileMenu.add(this.fileImportMenu, 1);

        // Add "Export to Diagram" menu item
        this.fileMenu.getFileExportMenu().add(this.fileExportToDiagramItem);
        this.fileExportToDiagramItem.addActionListener(e -> runExportToDiagram());
		
		return this.fileMenu;
    }

	private boolean runOpenFromBrowserStorage() {
		try {
			List<String> filenames = listStoredDiagramFilenames();
			if (filenames.isEmpty()) {
				dialogFactory.showInformationDialog(dialogOpenFromStorageEmptyMessage);
				return false;
			}
			String filename = chooseStoredDiagramFilename(filenames);
			if (filename == null) {
				// Action cancelled by user
				return false;
			}
			IFileReader fileReader = new CheerpJFileReader(FILES_DIRECTORY, filename);
			IWorkspace alreadyOpened = findOpenedWorkspace(fileReader.getFileDefinition());
			if (alreadyOpened != null) {
				this.fileMenu.getMainFrame().setActiveWorkspace(alreadyOpened);
				return true;
			}
			GraphFile graphFile = new GraphFile(fileReader.getFileDefinition());
			IWorkspace workspace = new Workspace(graphFile);
			this.fileMenu.getMainFrame().addWorkspace(workspace);
			this.userPreferencesService.addOpenedFile(graphFile);
			this.userPreferencesService.addRecentFile(graphFile);
			return true;
		} catch (Exception e) {
			dialogFactory.showErrorDialog(dialogOpenFileErrorMessage + " : " + e.getMessage());
			return false;
		}
	}

	/**
	 * @return names of the diagram files kept in the browser datastore, most recently modified first
	 */
	private List<String> listStoredDiagramFilenames() {
		List<String> result = new ArrayList<String>();
		File[] files = new File(FILES_DIRECTORY).listFiles(File::isFile);
		if (files == null) {
			return result;
		}
		Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
		ExtensionFilter[] filters = fileNamingService.getFileFilters();
		for (File aFile : files) {
			String name = aFile.getName();
			for (ExtensionFilter aFilter : filters) {
				String extension = aFilter.getExtension();
				if (extension != null && name.toLowerCase().endsWith(extension.toLowerCase())) {
					result.add(name);
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Shows a modal list of stored diagrams (double-click or OK to open)
	 *
	 * @return the selected filename, or null if cancelled
	 */
	private String chooseStoredDiagramFilename(List<String> filenames) {
		JList<String> list = new JList<String>(filenames.toArray(new String[0]));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(400, 250));

		JOptionPane optionPane = new JOptionPane(scrollPane, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && list.getSelectedValue() != null) {
					optionPane.setValue(JOptionPane.OK_OPTION);
				}
			}
		});
		dialogFactory.showDialog(optionPane, dialogOpenFromStorageTitle, true);

		Object value = optionPane.getValue();
		if (!(value instanceof Integer) || ((Integer) value).intValue() != JOptionPane.OK_OPTION) {
			return null;
		}
		return list.getSelectedValue();
	}

	private IWorkspace findOpenedWorkspace(IFile file) {
		for (IWorkspace aWorkspace : this.fileMenu.getMainFrame().getWorkspaceList()) {
			IGraphFile graphFile = aWorkspace.getGraphFile();
			if (file.getFilename().equals(graphFile.getFilename()) && file.getDirectory().equals(graphFile.getDirectory())) {
				return aWorkspace;
			}
		}
		return null;
	}

	private boolean runImportFromLocalDrive() {
		try {
			ExtensionFilter[] filters = fileNamingService.getFileFilters();
			IFileReader fileReader = fileChooserService.chooseAndGetFileReader(filters);
			GraphFile graphFile = new GraphFile(fileReader.getFileDefinition());
			IWorkspace workspace = new Workspace(graphFile);
			this.fileMenu.getMainFrame().addWorkspace(workspace);
			this.userPreferencesService.addOpenedFile(graphFile);
			this.userPreferencesService.addRecentFile(graphFile);
			return true;
		} catch (Exception e) {
			dialogFactory.showErrorDialog(dialogOpenFileErrorMessage + " : " + e.getMessage());
			return false;
		}
	}

	private boolean runExportToDiagram() {
		IWorkspace workspace = this.fileMenu.getMainFrame().getActiveWorkspace();
		if (workspace == null) {
			return false;
		}
		try {
			IGraphFile graphFile = workspace.getGraphFile();
			if (graphFile == null) {
				return false;
			}
            this.fileMenu.getFileSaveItem().doClick();
			if (graphFile.isSaveRequired()) {
				return false;
			}
			String filename = resolveStorageFilename(graphFile);
			IFileWriter writer = new CheerpJDownloadFileWriter(filename);
			OutputStream out = writer.getOutputStream();
			this.filePersistenceService.write(workspace.getGraphFile().getGraph(), out);
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean runPrintWorkspace() {
		IWorkspace workspace = this.fileMenu.getMainFrame().getActiveWorkspace();
		if (workspace == null) {
			return false;
		}
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			workspace.getGraphFile().exportToPdf(out);
			out.close();
			CheerpJInterfaceService.openPdfPrintTab(out.toByteArray());
			return true;
		} catch (Exception e) {
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
		return trimmed;
	}

}
