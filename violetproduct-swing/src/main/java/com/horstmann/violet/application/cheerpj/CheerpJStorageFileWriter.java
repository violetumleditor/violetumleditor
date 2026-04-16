package com.horstmann.violet.application.cheerpj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.persistence.IFileWriter;

/**
 * Writes a diagram file directly into CheerpJ's virtual filesystem mount at /files.
 */
public class CheerpJStorageFileWriter implements IFileWriter {

    private static final String STORAGE_DIRECTORY = "/files";

    private final String filename;

    public CheerpJStorageFileWriter(String filename) {
        this.filename = filename;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new FileOutputStream(new File(STORAGE_DIRECTORY, filename));
    }

    @Override
    public IFile getFileDefinition() {
        return new IFile() {
            @Override
            public String getDirectory() {
                return STORAGE_DIRECTORY;
            }

            @Override
            public String getFilename() {
                return filename;
            }
        };
    }
}
