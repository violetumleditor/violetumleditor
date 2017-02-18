package com.horstmann.violet.framework.file;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.file.chooser.IFileChooserService;
import com.horstmann.violet.framework.file.export.FileExportService;
import com.horstmann.violet.framework.file.naming.ExtensionFilter;
import com.horstmann.violet.framework.file.naming.FileNamingService;
import com.horstmann.violet.framework.file.persistence.IFilePersistenceService;
import com.horstmann.violet.framework.file.persistence.IFileReader;
import com.horstmann.violet.framework.file.persistence.IFileWriter;
import com.horstmann.violet.framework.file.persistence.JFileWriter;
import com.horstmann.violet.framework.google.drive.GoogleDriveAgent;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.framework.printer.PrintEngine;
import com.horstmann.violet.product.diagram.abstracts.IGraph;

public class GraphFile implements IGraphFile
{
    /**
     * Creates a new graph file with a new graph instance
     *
     * @param graphClass
     */
    public GraphFile(Class<? extends IGraph> graphClass)
    {
        ResourceBundleInjector.getInjector().inject(this);
        BeanInjector.getInjector().inject(this);
        try
        {
			this.graph = graphClass.newInstance();
        }
        catch (Exception e)
        {
            DialogFactory.getInstance().showErrorDialog(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructs a graph file from an existing file
     *
     * @param file
     */
    public GraphFile(IFile file) throws IOException
    {
        ResourceBundleInjector.getInjector().inject(this);
        BeanInjector.getInjector().inject(this);
        IFileReader fileOpener = fileChooserService.getFileReader(file);
        if (fileOpener == null)
        {
            throw new IOException("Open file action cancelled by user");
        }
        InputStream in = fileOpener.getInputStream();
        if (in != null)
        {
			this.graph = this.filePersistenceService.read(in);
	
        }
        else
        {
            throw new IOException("Unable to read file " + fileOpener.getFileDefinition().getFilename() + " from location " +
                    fileOpener.getFileDefinition().getDirectory());
        }
    }


    @Override
    public IGraph getGraph()
    {
        return this.graph;
    }

    @Override
    public String getFilename()
    {
        return this.currentFilename;
    }

    @Override
    public String getDirectory()
    {
        return this.currentDirectory;
    }

    @Override
    public void setSaveRequired()
    {
        this.isSaveRequired = true;
        fireGraphModified();
    }

    @Override
    public boolean isSaveRequired()
    {
        return this.isSaveRequired;
    }

    /**
     * Indicates if this file is new
     *
     * @return b
     */
    private boolean isNewFile()
    {
        if (this.currentFilename == null && this.currentDirectory == null)
        {
            return true;
        }
        return false;
    }

    @Override
    public void save()
    {
        if (this.isNewFile())
        {
            saveToNewLocation();
            return;
        }
        try
        {
            IFileWriter fileSaver = getFileSaver(false);
            OutputStream outputStream = fileSaver.getOutputStream();
            this.filePersistenceService.write(this.graph, outputStream);
            this.isSaveRequired = false;
            fireGraphSaved();
            this.currentFilename = fileSaver.getFileDefinition().getFilename();
            this.currentDirectory = fileSaver.getFileDefinition().getDirectory();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
	@Override
	public void autoSave(String fileDirectory) {
		try {

			if (this.autoSaveFileName == null) {
					this.autoSaveFileName =  new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + ".html";
				}
			
				if ((autoSaveFile == null) || ((autoSaveFile != null) && (!autoSaveFile.getAbsolutePath().contains(fileDirectory) == false))) {
				 	this.autoSaveFile = new File(fileDirectory + File.separator + this.autoSaveFileName);
				 	autoSaveFile.createNewFile();
				}
			
			if (autoSaveFile.exists()) {
			    JFileWriter jfilewriter = new JFileWriter(autoSaveFile);
				this.filePersistenceService.write(this.graph, jfilewriter.getOutputStream());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void removeBackup() {
		if ((autoSaveFile != null) && (autoSaveFile.exists())) {
			autoSaveFile.delete();
		}
	}

    @Override
    public void saveToGoogleDrive() throws GeneralSecurityException, IOException
    {
        save();

        final GoogleDriveAgent googleDriveAgent = new GoogleDriveAgent();
        googleDriveAgent.saveFile(String.format("%s/%s", currentDirectory, currentFilename));
    }

    @Override
    public void saveToNewLocation()
    {
        try
        {
            IFileWriter fileSaver = getFileSaver(true);
            if (fileSaver == null)
            {
                // This appends when the action is cancelled
                return;
            }
            OutputStream outputStream = fileSaver.getOutputStream();
            this.filePersistenceService.write(this.graph, outputStream);
            this.isSaveRequired = false;
            this.currentFilename = fileSaver.getFileDefinition().getFilename();
            this.currentDirectory = fileSaver.getFileDefinition().getDirectory();
            fireGraphSaved();
        }
        catch (IOException e1)
        {
            String message = MessageFormat.format(fileExportErrorMessage, e1.getMessage());
            JOptionPane.showMessageDialog(null, message, fileExportError, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Returns a IFileSaver instance. Then, this object allows to save graph content. If the graph has never been saved, the
     * FileChooserService<br/>
     * will open a dialog box to select a location. If not, the returned IFileSaver will automatically be bound to the last saving
     * location.<br/>
     * You can also force the FileChooserService to open the dialog box with the given argument.<br/>
     *
     * @param isAskedForNewLocation if true, then the FileChooser will open a dialog box to allow to choice a new location
     * @return f
     */
    private IFileWriter getFileSaver(boolean isAskedForNewLocation)
    {
        try
        {
            if (isAskedForNewLocation)
            {
                ExtensionFilter extensionFilter = this.fileNamingService.getExtensionFilter(this.graph);
                ExtensionFilter[] array =
                {
                    extensionFilter
                };
                return this.fileChooserService.chooseAndGetFileWriter(array);
            }
            return this.fileChooserService.getFileWriter(this);
        }
        catch (IOException e1)
        {
            String message = MessageFormat.format(fileExportErrorMessage, e1.getMessage());
            JOptionPane.showMessageDialog(null, message, fileExportError, JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    @Override
    public void addListener(IGraphFileListener listener)
    {
        synchronized (listeners)
        {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(IGraphFileListener listener)
    {
        synchronized (listeners)
        {
            listeners.remove(listener);
        }
    }

    /**
     * Sends an event to listeners each time the graph is modified
     */
    private void fireGraphModified()
    {
        synchronized (listeners)
        {
            for (IGraphFileListener listener : listeners)
            {
                listener.onFileModified();
            }
        }
    }

    /**
     * Sends an event to listeners when the graph has been saved
     */
    private void fireGraphSaved()
    {
        synchronized (listeners)
        {
            for (IGraphFileListener listener : listeners)
            {
                listener.onFileSaved();
            }
        }
    }

    @Override
    public void exportToClipboard()
    {
        FileExportService.exportToclipBoard(this.graph);
        JOptionPane optionPane = new JOptionPane();
        optionPane.setIcon(this.clipBoardDialogIcon);
        optionPane.setMessage(this.clipBoardDialogMessage);
        optionPane.setName(this.clipBoardDialogTitle);
        this.dialogFactory.showDialog(optionPane, this.clipBoardDialogTitle, true);
    }

    @Override
    public void exportImage(OutputStream out, String format)
    {
        if (!ImageIO.getImageWritersByFormatName(format).hasNext())
        {
            MessageFormat formatter = new MessageFormat(this.exportImageErrorMessage);
            String message = formatter.format(new Object[]
                    {
                            format
                    });
            JOptionPane optionPane = new JOptionPane();
            optionPane.setMessage(message);
            this.dialogFactory.showDialog(optionPane, this.exportImageDialogTitle, true);
            return;
        }
        try
        {

            try
            {
                ImageIO.write(FileExportService.getImage(this.graph), format, out);
            }
            finally
            {
                out.close();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void exportToPdf(OutputStream out)
    {
        FileExportService.exportToPdf(graph, out);
    }

    @Override
    public void exportToPrinter()
    {
        PrintEngine engine = new PrintEngine(this.graph);
        engine.start();
    }
    
    @Override
	public void autoSaveSettingsWasChanged()
    {
		this.autoSaveFile = null;
	}
    

    private IGraph graph;

    /**
     * Needed to identify the physical file used to save the graph
     */
    private String currentFilename;

    /**
     * Needed to identify the physical file used to save the graph
     */
    private String currentDirectory;

    private boolean isSaveRequired = false;

    @ResourceBundleBean(key = "dialog.export_to_clipboard.icon")
    private ImageIcon clipBoardDialogIcon;

    @ResourceBundleBean(key = "dialog.export_to_clipboard.title")
    private String clipBoardDialogTitle;

    @ResourceBundleBean(key = "dialog.export_to_clipboard.ok")
    private String clipBoardDialogMessage;

    @ResourceBundleBean(key = "dialog.error.unsupported_image")
    private String exportImageErrorMessage;

    @ResourceBundleBean(key = "dialog.error.title")
    private String exportImageDialogTitle;

    @InjectedBean
    private IFileChooserService fileChooserService;

    @InjectedBean
    private FileNamingService fileNamingService;

    @InjectedBean
    private IFilePersistenceService filePersistenceService;

    @ResourceBundleBean(key = "file.export.error.message")
    private String fileExportErrorMessage;

    @ResourceBundleBean(key = "file.export.error")
    private String fileExportError;

    @InjectedBean
    private DialogFactory dialogFactory;

    private List<IGraphFileListener> listeners = new ArrayList<IGraphFileListener>();

    private File autoSaveFile;
    private String autoSaveFileName;

}
