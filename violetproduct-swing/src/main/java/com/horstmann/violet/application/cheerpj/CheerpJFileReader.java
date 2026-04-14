package com.horstmann.violet.application.cheerpj;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.persistence.IFileReader;

/**
 * File reader that reads diagram files from the CheerpJ /files/ virtual filesystem.
 * Files are placed there by JavaScript (index.html) before Java reads them.
 */
public class CheerpJFileReader implements IFileReader {

    private static final String FILES_DIRECTORY = "/files";

    private final String filename;

    public CheerpJFileReader(String filename) {
        this.filename = filename;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(new File(FILES_DIRECTORY, filename));
    }

    @Override
    public IFile getFileDefinition() {
        return new IFile() {
            @Override
            public String getDirectory() {
                return FILES_DIRECTORY;
            }

            @Override
            public String getFilename() {
                return filename;
            }
        };
    }
}
