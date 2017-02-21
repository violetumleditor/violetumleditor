package com.horstmann.violet.framework.file.persistence;

import com.horstmann.violet.framework.file.IFile;
import java.io.IOException;
import java.io.OutputStream;
import javax.jnlp.FileContents;

public class JNLPFileWriter implements IFileWriter
{

    public JNLPFileWriter(FileContents contents) {
        this.contents = contents;
    }

   
    @Override
    public OutputStream getOutputStream() throws IOException
    {
        boolean isOverwriteAllowed = true;
        return contents.getOutputStream(isOverwriteAllowed);
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
