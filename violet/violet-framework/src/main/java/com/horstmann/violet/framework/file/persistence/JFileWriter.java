package com.horstmann.violet.framework.file.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.LocalFile;
/**
 * Standard Java FileSaver implementation
 * 
 * @author Alexandre de Pellegrin
 *
 */
public class JFileWriter implements IFileWriter
{

    public JFileWriter(File f) throws FileNotFoundException
    {
        this.f = f;
        this.out = new FileOutputStream(f);
    }

    
    @Override
    public OutputStream getOutputStream()
    {
        return out;
    }

    @Override
    public IFile getFileDefinition() throws IOException
    {
        return new LocalFile(this.f);
    }
    

    private File f;
    private OutputStream out;

}
