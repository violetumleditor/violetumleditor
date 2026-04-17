package com.horstmann.violet.framework.file.persistence;

import java.io.File;

/**
 * Standard Java filesystem implementation of {@link IFileDeleter}.
 */
public class JFileDeleter implements IFileDeleter
{
    public JFileDeleter(File file)
    {
        this.file = file;
    }

    @Override
    public void delete()
    {
        this.file.delete();
    }

    private final File file;
}
