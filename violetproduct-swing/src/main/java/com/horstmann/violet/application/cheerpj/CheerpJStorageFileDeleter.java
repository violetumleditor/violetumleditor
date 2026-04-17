package com.horstmann.violet.application.cheerpj;

import java.io.File;

import com.horstmann.violet.framework.file.persistence.IFileDeleter;

/**
 * Deletes a file from CheerpJ's virtual filesystem mount at /files.
 */
public class CheerpJStorageFileDeleter implements IFileDeleter
{
    private static final String STORAGE_DIRECTORY = "/files";

    public CheerpJStorageFileDeleter(String filename)
    {
        this.filename = filename;
    }

    @Override
    public void delete()
    {
        new File(STORAGE_DIRECTORY, this.filename).delete();
    }

    private final String filename;
}
