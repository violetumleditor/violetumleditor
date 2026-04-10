package com.horstmann.violet.application.cheerpj;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.horstmann.violet.framework.file.IGraphFile;
import com.horstmann.violet.framework.file.IGraphFileListener;
import com.horstmann.violet.framework.file.export.FileExportService;
import com.horstmann.violet.framework.file.persistence.IFilePersistenceService;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.printer.PrintEngine;
import com.horstmann.violet.product.diagram.abstracts.IGraph;

public class CheerpJStorageGraphFile implements IGraphFile {

    private static final String STORAGE_DIRECTORY = "browser-localstorage";
    private static final String DEFAULT_FILENAME = "diagram.violet.html";

    private final IGraph graph;
    private String storageFilename;
    private boolean saveRequired;
    private final List<IGraphFileListener> listeners = new ArrayList<IGraphFileListener>();

    @InjectedBean
    private IFilePersistenceService filePersistenceService;

    public CheerpJStorageGraphFile(IGraph graph, String storageFilename) {
        BeanInjector.getInjector().inject(this);
        this.graph = graph;
        this.storageFilename = sanitizeFilename(storageFilename);
        this.saveRequired = false;
    }

    @Override
    public IGraph getGraph() {
        return this.graph;
    }

    @Override
    public void setSaveRequired() {
        this.saveRequired = true;
        fireGraphModified();
    }

    @Override
    public boolean isSaveRequired() {
        return this.saveRequired;
    }

    @Override
    public void save() {
        if (this.storageFilename == null) {
            this.storageFilename = DEFAULT_FILENAME;
        }
        doSave();
    }

    @Override
    public void saveToNewLocation() {
        if (this.storageFilename == null) {
            this.storageFilename = DEFAULT_FILENAME;
        }
        doSave();
    }

    public void setStorageFilename(String filename) {
        this.storageFilename = sanitizeFilename(filename);
    }

    private void doSave() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            this.filePersistenceService.write(this.graph, out);
            out.close();
            CheerpJInterfaceService.saveLocalStorageDiagram(this.storageFilename, out.toByteArray());
            this.saveRequired = false;
            fireGraphSaved();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addListener(IGraphFileListener listener) {
        synchronized (this.listeners) {
            this.listeners.add(listener);
        }
    }

    @Override
    public void removeListener(IGraphFileListener listener) {
        synchronized (this.listeners) {
            this.listeners.remove(listener);
        }
    }

    private void fireGraphModified() {
        synchronized (this.listeners) {
            for (IGraphFileListener listener : this.listeners) {
                listener.onFileModified();
            }
        }
    }

    private void fireGraphSaved() {
        synchronized (this.listeners) {
            for (IGraphFileListener listener : this.listeners) {
                listener.onFileSaved();
            }
        }
    }

    @Override
    public void exportToClipboard() {
        FileExportService.exportToclipBoard(this.graph);
    }

    @Override
    public void exportImage(OutputStream out, String format) {
        try {
            try {
                ImageIO.write(FileExportService.getImage(this.graph), format, out);
            } finally {
                out.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void exportToPdf(OutputStream out) {
        FileExportService.exportToPdf(this.graph, out);
    }

    @Override
    public void exportToPrinter() {
        PrintEngine engine = new PrintEngine(this.graph);
        engine.start();
    }

    @Override
    public String getFilename() {
        return this.storageFilename;
    }

    @Override
    public String getDirectory() {
        return STORAGE_DIRECTORY;
    }

    private String sanitizeFilename(String filename) {
        if (filename == null) {
            return null;
        }
        String trimmed = filename.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        // Keep the original filename as provided, without forcing .violet.html suffix
        return trimmed;
    }
}