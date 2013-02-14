package com.horstmann.violet.framework.file;

import java.io.File;
import java.io.IOException;


/**
 * Represents a file on the local hard disk
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class LocalFile implements IFile
{

    /**
     * Construct an instance from a java file
     * 
     * @param fullPath
     * @throws IOException is not found on disk
     */
    public LocalFile(File file) throws IOException
    {
        init(file.getAbsolutePath());
    }

    /**
     * Builds an instance from an IFile
     * 
     * @param aFile
     * @throws IOException if not found on disk
     */
    public LocalFile(IFile aFile) throws IOException
    {
        String fullPath = aFile.getDirectory() + File.separator + aFile.getFilename();
        init(fullPath);
    }

    private void init(String fullPath) throws IOException
    {
        File aFile = new File(fullPath);
        if (!aFile.exists() || (!aFile.exists() && !aFile.isFile()))
        {
            throw new IOException("Unable to locate file");
        }
        this.directory = aFile.getParent();
        this.filename = aFile.getName();
    }
    
    /**
     * @return a real file instance
     */
    public File toFile() {
        String fullPath = this.directory + File.separator + this.filename;
        return new File(fullPath);
    }

    @Override
    public String getDirectory()
    {
        return this.directory;
    }

    @Override
    public String getFilename()
    {
        return this.filename;
    }

    private String directory;

    private String filename;

}
