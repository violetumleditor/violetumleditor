package com.horstmann.violet.application.jni;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.persistence.IFileReader;

/**
 * File reader for files uploaded through browser file dialog in CheerpJ mode.
 */
public class CheerpJFileReader implements IFileReader {

    private final String filename;
    private final byte[] fileData;

    public CheerpJFileReader(String filename, byte[] fileData) {
        this.filename = filename;
        this.fileData = fileData;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(fileData);
    }

    @Override
    public IFile getFileDefinition() {
        return new IFile() {
            @Override
            public String getDirectory() {
                return "browser";
            }

            @Override
            public String getFilename() {
                return filename;
            }
        };
    }
}
