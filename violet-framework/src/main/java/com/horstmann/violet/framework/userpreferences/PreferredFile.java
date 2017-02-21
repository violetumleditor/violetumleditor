package com.horstmann.violet.framework.userpreferences;

import com.horstmann.violet.framework.file.IFile;
import java.io.IOException;

/**
 * This class allows to wrap a file definition (composed by <br/>
 * a filename and its location which is called directory) <br/>
 * into a string to be stored into user local preferences.
 * 
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class PreferredFile implements IFile
{

    /**
     * Creates a wrapper from a filename and its directory
     * 
     * @param filename
     * @param directory
     */
    public PreferredFile(String directory, String filename)
    {
        this.filename = filename;
        this.directory = directory;
    }

    /**
     * Constructs an instance from a generic IFile. Allows to wrap an IFile into a PreferredFile instance
     * 
     * @param aFile
     */
    public PreferredFile(IFile aFile)
    {
        this.filename = aFile.getFilename();
        this.directory = aFile.getDirectory();
    }

    /**
     * Creates a wrapper from a string from user preferences
     * 
     * @param userPreferenceString
     * @throws IOException if unable to parse input String
     */
    public PreferredFile(String userPreferenceString) throws IOException
    {
        String[] strings = userPreferenceString.split(PreferencesConstant.PATH_SEPARATOR.toString());
        if (strings.length != 2)
        {
            throw new IOException("Unable to parse file path from user preferences");
        }
        this.directory = strings[0];
        this.filename = strings[1];
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.preference.IFile#getFilename()
     */
    public String getFilename()
    {
        return filename;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.preference.IFile#getDirectory()
     */
    public String getDirectory()
    {
        return directory;
    }

    @Override
    public String toString()
    {
        return this.directory + PreferencesConstant.PATH_SEPARATOR.toString() + this.filename;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((filename == null) ? 0 : filename.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PreferredFile other = (PreferredFile) obj;
        if (filename == null)
        {
            if (other.filename != null) return false;
        }
        else if (!filename.equals(other.filename)) return false;
        return true;
    }

    /**
     * The file name
     */
    private String filename;

    /**
     * Its location
     */
    private String directory;

}
