package com.horstmann.violet.framework.file;

public interface IFile
{

    public abstract String getFilename();

    public abstract String getDirectory();

    /**
     * @return true if this file is using a temporary auto-generated filename and has not yet been
     *         explicitly named by the user. Defaults to false for plain file references.
     */
    public default boolean isTemporaryFilename()
    {
        return false;
    }

}