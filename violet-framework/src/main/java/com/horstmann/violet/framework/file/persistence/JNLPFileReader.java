package com.horstmann.violet.framework.file.persistence;

import com.horstmann.violet.framework.file.IFile;
import java.io.IOException;
import java.io.InputStream;
import javax.jnlp.FileContents;

public class JNLPFileReader implements IFileReader
{
    
    public JNLPFileReader(FileContents contents) {
        this.contents = contents;
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return contents.getInputStream();
    }

    @Override
    public IFile getFileDefinition() throws IOException
    {
        final String name = contents.getName();
        return new IFile() {
            @Override
            public String getDirectory()
            {
                return null;
            }

            @Override
            public String getFilename()
            {
                return name;
            }
        };
    }
    
    
    private FileContents contents;




}
