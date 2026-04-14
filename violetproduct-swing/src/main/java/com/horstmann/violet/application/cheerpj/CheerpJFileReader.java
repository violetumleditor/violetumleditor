package com.horstmann.violet.application.cheerpj;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.persistence.IFileReader;

/**
 * File reader that reads diagram files from CheerpJ's virtual filesystem.
 * For freshly-opened files (JS→Java transfer) use directory "/str".
 * For re-reading previously saved files use directory "/files".
 */
public class CheerpJFileReader implements IFileReader {

    private final String directory;
    private final String filename;

    public CheerpJFileReader(String directory, String filename) {
        this.directory = directory;
        this.filename = filename;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(new File(directory, filename));
    }

    @Override
    public IFile getFileDefinition() {
        return new IFile() {
            @Override
            public String getDirectory() {
                return directory;
            }

            @Override
            public String getFilename() {
                return filename;
            }
        };
    }
}
