package com.horstmann.violet.framework.file.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.LocalFile;

public class JFileReader implements IFileReader
{

    public JFileReader(File f) throws FileNotFoundException
    {
        this.f = f; 
        this.in = new FileInputStream(f);
    }

    @Override
    public InputStream getInputStream()
    {
        return in;
    }
    
    @Override
    public IFile getFileDefinition() throws IOException
    {
        return new LocalFile(this.f);
    }

    private InputStream in;

    private File f;


}
