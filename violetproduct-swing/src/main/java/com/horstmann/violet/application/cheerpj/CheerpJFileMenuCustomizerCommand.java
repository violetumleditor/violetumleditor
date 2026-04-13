package com.horstmann.violet.application.cheerpj;

import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JMenuItem;

import com.horstmann.violet.application.gui.MainFrame;
import com.horstmann.violet.application.menu.FileMenu;
import com.horstmann.violet.application.menu.MenuFactory;
import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.file.IGraphFile;
import com.horstmann.violet.framework.file.chooser.IFileChooserService;
import com.horstmann.violet.framework.file.naming.ExtensionFilter;
import com.horstmann.violet.framework.file.naming.FileNamingService;
import com.horstmann.violet.framework.file.persistence.IFilePersistenceService;
import com.horstmann.violet.framework.file.persistence.IFileReader;
import com.horstmann.violet.framework.file.persistence.IFileWriter;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.Workspace;


@ResourceBundleBean(resourceReference = MenuFactory.class)
public class CheerpJFileMenuCustomizerCommand {

	private static final String FILES_DIRECTORY = "/files";

    private FileMenu fileMenu;

    @ResourceBundleBean(key = "file.import")
    private JMenuItem fileImportItem;

    @ResourceBundleBean(key = "file.export_to_diagram")
    private JMenuItem fileExportToDiagramItem;

    @ResourceBundleBean(key = "dialog.open_file_failed.text")
    private String dialogOpenFileErrorMessage;

    @InjectedBean
    private FileNamingService fileNamingService;

    @InjectedBean
    private IFilePersistenceService filePersistenceService;

    @InjectedBean
    private DialogFactory dialogFactory;

    @InjectedBean
    private IFileChooserService fileChooserService;

    public CheerpJFileMenuCustomizerCommand(FileMenu fileMenu) {
        ResourceBundleInjector.getInjector().inject(this);
        this.fileMenu = fileMenu;
    }

	public FileMenu execute() {
        // Change print action to use CheerpJ printing
        for (ActionListener listener : this.fileMenu.getFilePrintItem().getActionListeners()) {
            this.fileMenu.getFilePrintItem().removeActionListener(listener);
        }
        this.fileMenu.getFilePrintItem().addActionListener(e -> runPrintWorkspace());

        // Hide the "Exit" menu item
        this.fileMenu.getFileExitItem().setVisible(false);

        // Add "Import" menu item
        this.fileMenu.add(this.fileImportItem, 1);
        this.fileImportItem.addActionListener(e -> runImportFromLocalDrive());

        // Add "Export to Diagram" menu item
        this.fileMenu.getFileExportMenu().add(this.fileExportToDiagramItem);
        this.fileExportToDiagramItem.addActionListener(e -> runExportToDiagram());
		
		return this.fileMenu;
    }

    private boolean runImportFromLocalDrive() {
		try {
			ExtensionFilter[] filters = fileNamingService.getFileFilters();
			IFileReader fileReader = fileChooserService.chooseAndGetFileReader(filters);
			if (fileReader == null) {
				return false;
			}
			InputStream in = fileReader.getInputStream();
			byte[] content = in.readAllBytes();
			in.close();
			String filename = normalizeDiagramFilename(fileReader.getFileDefinition().getFilename());
			if (filename == null) {
				filename = "diagram.violet.html";
			}
			saveDiagramToFilesMount(filename, content);
			IGraph graph = readGraph(content, filePersistenceService);
			CheerpJStorageGraphFile graphFile = new CheerpJStorageGraphFile(graph, filename);
			IWorkspace workspace = new Workspace(graphFile);
			this.fileMenu.getMainFrame().addWorkspace(workspace);
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

	private void saveDiagramToFilesMount(String filename, byte[] content) throws IOException {
		Path path = Path.of(FILES_DIRECTORY, filename);
		if (path.getParent() != null) {
			Files.createDirectories(path.getParent());
		}
		Files.write(path, content);
	}

	private IGraph readGraph(byte[] content, IFilePersistenceService filePersistenceService) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(content);
		return filePersistenceService.read(in);
	}

}
